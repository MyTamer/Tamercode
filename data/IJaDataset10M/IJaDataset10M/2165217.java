package de.intarsys.pdf.content.common;

import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.intarsys.pdf.content.CSContent;
import de.intarsys.pdf.content.CSOperation;
import de.intarsys.pdf.content.CSOperators;
import de.intarsys.pdf.content.CSVirtualDevice;
import de.intarsys.pdf.content.ICSInterpreter;
import de.intarsys.pdf.content.IContentStreamProvider;
import de.intarsys.pdf.content.TextState;
import de.intarsys.pdf.cos.COSArray;
import de.intarsys.pdf.cos.COSConverter;
import de.intarsys.pdf.cos.COSDictionary;
import de.intarsys.pdf.cos.COSFixed;
import de.intarsys.pdf.cos.COSInteger;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.cos.COSObject;
import de.intarsys.pdf.cos.COSString;
import de.intarsys.pdf.font.PDFont;
import de.intarsys.pdf.font.PDFontTools;
import de.intarsys.pdf.pd.IResourcesProvider;
import de.intarsys.pdf.pd.PDColorSpace;
import de.intarsys.pdf.pd.PDExtGState;
import de.intarsys.pdf.pd.PDForm;
import de.intarsys.pdf.pd.PDImage;
import de.intarsys.pdf.pd.PDObject;
import de.intarsys.pdf.pd.PDPage;
import de.intarsys.pdf.pd.PDPattern;
import de.intarsys.pdf.pd.PDResources;
import de.intarsys.pdf.pd.PDShading;
import de.intarsys.pdf.pd.PDXObject;

/**
 * An implementation helping to create a PDF content stream using higher level
 * abstractions than the PDF operators.
 * <p>
 * This object deals with two abstractions:
 * <p>
 * The {@link CSContent}, a sequence of PDF operators. The {@link CSContent}
 * can be read from an existing object like a {@link PDPage} or a {@link PDForm}
 * or can be newly created, using the factory methods in {@link CSContent}.
 * <p>
 * The second abstraction is the {@link IResourcesProvider} for the
 * {@link PDResources} like Fonts, XObjects, ... that are referenced in the
 * {@link CSContent}. The resources of the provider are not accessed until a
 * resource relevant operation is created.
 * 
 * <p>
 * This implementation is (nearly, see AffineTransform) independent of AWT or
 * any other window toolkit.
 * 
 */
public class CSCreator extends CSVirtualDevice {

    /** a constant for the ease of circle creation with beziers */
    public static final float KAPPA = 0.5522847498f;

    /** local constants */
    private static final float THOUSAND = 1000f;

    public static final int VALUE_COLOR_PRECISION = 3;

    public static final int VALUE_COORDINATE_PRECISION = 3;

    public static final int VALUE_DASH_PRECISION = 2;

    public static final int VALUE_FACTOR_PRECISION = 4;

    public static final int VALUE_FLATNESS_PRECISION = 3;

    public static final int VALUE_FONT_PRECISION = 2;

    public static final int VALUE_GRAY_PRECISION = 3;

    public static final int VALUE_WIDTH_PRECISION = 3;

    /**
	 * Create a {@link CSCreator} on an existing {@link CSContent}. The content
	 * may be read from a PDPage or a PDForm or can be completely new.
	 * <p>
	 * This is the most basic factory method where you have the freedom to
	 * decide where the content comes from, where the resources come from and
	 * where the content will be used.
	 * 
	 * @param content
	 *            The content stream to write to.
	 * @param resourcesProvider
	 *            The provider for the {@link PDResources} that contain the
	 *            resources referenced in the CSContent.
	 * 
	 * @return The new {@link CSCreator}
	 */
    public static CSCreator createFromContent(CSContent content, IResourcesProvider resourcesProvider) {
        CSCreator result = new CSCreator(content, null, resourcesProvider);
        return result;
    }

    /**
	 * Create a {@link CSCreator} on an existing {@link CSContent} that will be
	 * read from the {@link IContentStreamProvider}. The old contents will be
	 * preserved, all operations are concatenated. The content is linked with
	 * the {@link IContentStreamProvider} and flushed to it when calling
	 * "flush".
	 * 
	 * @param provider
	 *            The provider whose content stream will be replaced with the
	 *            result of this {@link CSCreator}
	 * 
	 * @return The new {@link CSCreator}
	 */
    public static CSCreator createFromProvider(IContentStreamProvider provider) {
        CSCreator result = new CSCreator(provider.getContentStream(), provider, provider);
        return result;
    }

    /**
	 * Create a {@link CSCreator} on a newly created {@link CSContent}. The
	 * content is linked with the {@link IContentStreamProvider} and flushed to
	 * it when calling "flush".
	 * 
	 * @param provider
	 *            The provider whose content stream will be replaced with the
	 *            result of this {@link CSCreator}
	 * 
	 * @return The new {@link CSCreator}
	 */
    public static CSCreator createNew(IContentStreamProvider provider) {
        CSContent content = CSContent.createNew();
        CSCreator result = new CSCreator(content, provider, provider);
        return result;
    }

    /**
	 * Create a {@link CSCreator} on a newly created {@link CSContent}. The
	 * content is not linked with the provider, which is only used the a
	 * {@link IResourcesProvider}. The resulting content can be accessed using
	 * "getContent".
	 * 
	 * @param provider
	 *            The provider for resources within the new {@link CSContent}.
	 * 
	 * @return The new {@link CSCreator}
	 */
    public static CSCreator createNewDetached(IResourcesProvider provider) {
        CSContent content = CSContent.createNew();
        CSCreator result = new CSCreator(content, null, provider);
        return result;
    }

    /**
	 * A flag if state change results in an operation
	 */
    private boolean applyOperation = false;

    /** The underlying content stream represenation. */
    private final CSContent content;

    /**
	 * An object that will provide access to the content stream and the
	 * containing object.
	 */
    private final IContentStreamProvider contentStreamProvider;

    /**
	 * The resource dictionary for the objects referenced from the content
	 * stream
	 */
    private PDResources resources;

    /**
	 * An object that will provide resources for the content stream.
	 * <p>
	 * The provider is accessed only if it is needed by kind of operations
	 * performed, for example "textSetFont".
	 */
    private final IResourcesProvider resourcesProvider;

    /**
	 * A temporary buffer for strings to be rendered.
	 * 
	 * <p>
	 * String objects are kept here to optimize and use the rendereing of string
	 * arrays if possible.
	 * </p>
	 */
    private List strings;

    /**
	 * A buffer holding text runs that are positioned in continuous sequence so
	 * that we can optimze and keep the string together.
	 */
    private ByteArrayOutputStream textBuffer;

    /**
	 * Flag if we a re currently in text mode.
	 * 
	 * <p>
	 * Text mode is entered via "textBegin"
	 * </p>
	 */
    private boolean textMode = false;

    /**
	 * Create a {@link CSCreator}
	 * 
	 * @param content
	 *            The content stream to write to.
	 */
    protected CSCreator(CSContent content, IContentStreamProvider contentStreamProvider, IResourcesProvider resourcesProvider) {
        super();
        this.content = content;
        this.contentStreamProvider = contentStreamProvider;
        this.resourcesProvider = resourcesProvider;
        open(null);
    }

    @Override
    protected void basicSetNonStrokeColorSpace(PDColorSpace colorSpace) {
        super.basicSetNonStrokeColorSpace(colorSpace);
        applyOperation = true;
    }

    @Override
    protected void basicSetNonStrokeColorValues(float[] values) {
        super.basicSetNonStrokeColorValues(values);
        applyOperation = true;
    }

    @Override
    protected void basicSetStrokeColorSpace(PDColorSpace colorSpace) {
        super.basicSetStrokeColorSpace(colorSpace);
        applyOperation = true;
    }

    @Override
    protected void basicSetStrokeColorValues(float[] values) {
        super.basicSetStrokeColorValues(values);
        applyOperation = true;
    }

    protected void basicTextShow(byte[] text) {
        CSOperation operation = new CSOperation(CSOperators.CSO_Tj);
        operation.addOperand(COSString.create(text));
        for (int i = 0; i < text.length; i++) {
            textState.font.setCharUsed(text[i]);
        }
        getContent().addOperation(operation);
    }

    protected COSName checkResource(COSName resourceType, PDObject.MetaClass metaClass, COSName resourceName, PDObject resource) {
        if (getResources() == null) {
            throw new IllegalStateException("must have resource dictionary");
        }
        if (resourceName == null) {
            resourceName = getResources().createResource(resourceType, resource);
        } else {
            PDObject temp = getResources().getResource(resourceType, metaClass, resourceName);
            if (temp == null) {
                getResources().addResource(resourceType, resourceName, resource);
            } else {
                if (resource != temp) {
                    resourceName = getResources().createResource(resourceType, resource);
                }
            }
        }
        return resourceName;
    }

    @Override
    public void close() {
        flush();
        super.close();
    }

    @Override
    public void compatibilityBegin() {
        super.compatibilityBegin();
        CSOperation operation = new CSOperation(CSOperators.CSO_BX);
        getContent().addOperation(operation);
    }

    @Override
    public void compatibilityEnd() {
        super.compatibilityEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_EX);
        getContent().addOperation(operation);
    }

    /**
	 * Copy all operations from <code>otherContent</code> to <code>this</code>.
	 * <p>
	 * Currently no resources are copied for <code>otherContent</code>
	 * 
	 * @param otherContent
	 *            The source of the graphic operations.
	 */
    public void copy(CSContent otherContent) {
        int len = otherContent.size();
        for (int i = 0; i < len; i++) {
            CSOperation operation = otherContent.getOperation(i);
            operationAdd(operation);
        }
    }

    @Override
    public void doShading(COSName name, PDShading shading) {
        textEnd();
        name = checkResource(PDResources.CN_RT_Shading, PDShading.META, name, shading);
        CSOperation operation = new CSOperation(CSOperators.CSO_sh);
        operation.addOperand(name);
        getContent().addOperation(operation);
    }

    @Override
    public void doXObject(COSName name, PDXObject xObject) {
        textEnd();
        name = checkResource(PDResources.CN_RT_XObject, PDXObject.META, name, xObject);
        CSOperation operation = new CSOperation(CSOperators.CSO_Do);
        operation.addOperand(name);
        getContent().addOperation(operation);
    }

    /**
	 * Flush all pending operations on the stream.
	 * <p>
	 * This must be called before the <code>getContent</code> operation is
	 * valid.
	 */
    public void flush() {
        textEnd();
        if (getContentStreamProvider() != null) {
            getContentStreamProvider().setContentStream(getContent());
        }
    }

    /**
	 * The {@link CSContent} we are working on. After calling <code>flush</code>
	 * the {@link CSContent} contains all operations stemming from calls to
	 * this.
	 * 
	 * @return The {@link CSContent} we are working on.
	 */
    public CSContent getContent() {
        return content;
    }

    public IContentStreamProvider getContentStreamProvider() {
        return contentStreamProvider;
    }

    protected PDResources getResources() {
        if (resources == null) {
            resources = getResourcesProvider().getResources();
            if (resources == null) {
                resources = (PDResources) PDResources.META.createNew();
                getResourcesProvider().setResources(resources);
            }
        }
        return resources;
    }

    /**
	 * The {@link IResourcesProvider} associated with this.
	 * 
	 * @return The {@link IResourcesProvider} associated with this.
	 */
    public IResourcesProvider getResourcesProvider() {
        return resourcesProvider;
    }

    protected List getStrings() {
        return strings;
    }

    protected ByteArrayOutputStream getTextBuffer() {
        return textBuffer;
    }

    @Override
    public void inlineImage(PDImage img) {
    }

    /**
	 * Answer <code>true</code> if the actual font in the text state is equal
	 * to <code>queryFont</code> and <code>queryFontSize</code>.
	 * 
	 * @param queryFont
	 *            The font to query.
	 * @param queryFontSize
	 *            The font size to query.
	 * 
	 * @return true if equal
	 */
    protected boolean isFont(PDFont queryFont, float queryFontSize) {
        TextState tempTextState = textState;
        return ((tempTextState.font != null) && tempTextState.font.equals(queryFont)) && (tempTextState.fontSize == queryFontSize);
    }

    protected boolean isTextMode() {
        return textMode;
    }

    @Override
    public void markedContentBegin(COSName tag) {
        CSOperation operation = new CSOperation(CSOperators.CSO_BMC);
        operation.addOperand(tag.copyOptional());
        getContent().addOperation(operation);
    }

    @Override
    public void markedContentBeginProperties(COSName tag, COSName resourceName, COSDictionary properties) {
    }

    @Override
    public void markedContentEnd() {
        CSOperation operation = new CSOperation(CSOperators.CSO_EMC);
        getContent().addOperation(operation);
    }

    @Override
    public void markedContentPoint(COSName tag) {
        CSOperation operation = new CSOperation(CSOperators.CSO_MP);
        operation.addOperand(tag.copyOptional());
        getContent().addOperation(operation);
    }

    @Override
    public void markedContentPointProperties(COSName tag, COSName resourceName, COSDictionary properties) {
    }

    @Override
    public void open(ICSInterpreter pInterpreter) {
        super.open(pInterpreter);
        resetStrings();
    }

    protected void operationAdd(CSOperation operation) {
        getContent().addOperation(operation);
    }

    @Override
    public void pathClipEvenOdd() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_Wstar);
        getContent().addOperation(operation);
    }

    @Override
    public void pathClipNonZero() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_W);
        getContent().addOperation(operation);
    }

    @Override
    public void pathClose() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_h);
        getContent().addOperation(operation);
    }

    @Override
    public void pathCloseFillStrokeEvenOdd() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_bstar);
        getContent().addOperation(operation);
    }

    @Override
    public void pathCloseFillStrokeNonZero() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_b);
        getContent().addOperation(operation);
    }

    @Override
    public void pathCloseStroke() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_s);
        getContent().addOperation(operation);
    }

    @Override
    public void pathEnd() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_n);
        getContent().addOperation(operation);
    }

    @Override
    public void pathFillEvenOdd() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_fstar);
        getContent().addOperation(operation);
    }

    @Override
    public void pathFillNonZero() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_f);
        getContent().addOperation(operation);
    }

    @Override
    public void pathFillStrokeEvenOdd() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_Bstar);
        getContent().addOperation(operation);
    }

    @Override
    public void pathFillStrokeNonZero() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_B);
        getContent().addOperation(operation);
    }

    @Override
    public void pathStroke() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_S);
        getContent().addOperation(operation);
    }

    /**
	 * Draw a circle. The center of the circle is at <code>x</code>,
	 * <code>y</code> in user space. <code>r</code> defines the radius.
	 * 
	 * @param x
	 *            The x coordinate of the center.
	 * @param y
	 *            The y coordinate of the center.
	 * @param r
	 *            The radius
	 */
    public void penCircle(float x, float y, float r) {
        penEllipse(x, y, r, r);
    }

    @Override
    public void penCurveToC(float x1, float y1, float x2, float y2, float x3, float y3) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_c);
        operation.addOperand(COSFixed.create(x1));
        operation.addOperand(COSFixed.create(y1));
        operation.addOperand(COSFixed.create(x2));
        operation.addOperand(COSFixed.create(y2));
        operation.addOperand(COSFixed.create(x3));
        operation.addOperand(COSFixed.create(y3));
        getContent().addOperation(operation);
    }

    @Override
    public void penCurveToV(float x2, float y2, float x3, float y3) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_v);
        operation.addOperand(COSFixed.create(x2));
        operation.addOperand(COSFixed.create(y2));
        operation.addOperand(COSFixed.create(x3));
        operation.addOperand(COSFixed.create(y3));
        getContent().addOperation(operation);
    }

    @Override
    public void penCurveToY(float x1, float y1, float x3, float y3) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_y);
        operation.addOperand(COSFixed.create(x1));
        operation.addOperand(COSFixed.create(y1));
        operation.addOperand(COSFixed.create(x3));
        operation.addOperand(COSFixed.create(y3));
        getContent().addOperation(operation);
    }

    /**
	 * Draw an ellipse. The center of the ellipse is at <code>x</code>,
	 * <code>y</code> in user space. <code>rx</code> and <code>ry</code>
	 * define the radius in x and y direction respectively.
	 * 
	 * @param x
	 *            The x coordinate of the center.
	 * @param y
	 *            The y coordinate of the center.
	 * @param rx
	 *            The radius in x direction
	 * @param ry
	 *            The radius in y direction
	 */
    public void penEllipse(float x, float y, float rx, float ry) {
        textEnd();
        float rxkappa = rx * KAPPA;
        float rykappa = ry * KAPPA;
        penMoveTo(x + rx, y);
        penCurveToC(x + rx, y + rykappa, x + rxkappa, y + ry, x, y + ry);
        penCurveToC(x - rxkappa, y + ry, x - rx, y + rykappa, x - rx, y);
        penCurveToC(x - rx, y - rykappa, x - rxkappa, y - ry, x, y - ry);
        penCurveToC(x + rxkappa, y - ry, x + rx, y - rykappa, x + rx, y);
    }

    @Override
    public void penLineTo(float x, float y) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_l);
        operation.addOperand(COSFixed.create(x, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(y, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void penMoveTo(float x, float y) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_m);
        operation.addOperand(COSFixed.create(x, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(y, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void penRectangle(float x, float y, float w, float h) {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_re);
        operation.addOperand(COSFixed.create(x, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(y, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(w, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(h, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    protected void resetStrings() {
        strings = new ArrayList();
        resetTextBuffer();
    }

    protected void resetTextBuffer() {
        textBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void restoreState() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_Q);
        getContent().addOperation(operation);
        super.restoreState();
    }

    @Override
    public void saveState() {
        textEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_q);
        getContent().addOperation(operation);
        super.saveState();
    }

    @Override
    public void setExtendedState(COSName name, PDExtGState gstate) {
        streamEnd();
        name = checkResource(PDResources.CN_RT_ExtGState, PDExtGState.META, name, gstate);
        super.setExtendedState(name, gstate);
        CSOperation operation = new CSOperation(CSOperators.CSO_gs);
        operation.addOperand(name);
        getContent().addOperation(operation);
    }

    @Override
    public void setFlatnessTolerance(float flatness) {
        streamEnd();
        super.setFlatnessTolerance(flatness);
        CSOperation operation = new CSOperation(CSOperators.CSO_i);
        operation.addOperand(COSFixed.create(flatness, VALUE_FLATNESS_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setLineCap(int style) {
        streamEnd();
        super.setLineCap(style);
        CSOperation operation = new CSOperation(CSOperators.CSO_J);
        operation.addOperand(COSInteger.create(style));
        getContent().addOperation(operation);
    }

    /**
	 * Convenience method to access "setLineDash".
	 * 
	 * @param unitsOn
	 * @param unitsOff
	 * @param phase
	 */
    public void setLineDash(float unitsOn, float unitsOff, float phase) {
        setLineDash(new float[] { unitsOn, unitsOff }, phase);
    }

    @Override
    public void setLineDash(float[] pattern, float phase) {
        streamEnd();
        super.setLineDash(pattern, phase);
        CSOperation operation = new CSOperation(CSOperators.CSO_d);
        operation.addOperand(COSConverter.toCos(pattern));
        operation.addOperand(COSFixed.create(phase, VALUE_DASH_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setLineJoin(int style) {
        streamEnd();
        super.setLineJoin(style);
        CSOperation operation = new CSOperation(CSOperators.CSO_j);
        operation.addOperand(COSInteger.create(style));
        getContent().addOperation(operation);
    }

    @Override
    public void setLineWidth(float w) {
        if (graphicsState.lineWidth == w) {
            return;
        }
        streamEnd();
        super.setLineWidth(w);
        CSOperation operation = new CSOperation(CSOperators.CSO_w);
        operation.addOperand(COSFixed.create(w, VALUE_WIDTH_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setMiterLimit(float miterLimit) {
        streamEnd();
        super.setMiterLimit(miterLimit);
        CSOperation operation = new CSOperation(CSOperators.CSO_M);
        operation.addOperand(COSFixed.create(miterLimit));
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorCMYK(float c, float m, float y, float k) {
        c = Math.max(0, Math.min(1, c));
        m = Math.max(0, Math.min(1, m));
        y = Math.max(0, Math.min(1, y));
        k = Math.max(0, Math.min(1, k));
        super.setNonStrokeColorCMYK(c, m, y, k);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_k);
        operation.addOperand(COSFixed.create(c, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(m, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(y, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(k, VALUE_COLOR_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorGray(float gray) {
        super.setNonStrokeColorGray(gray);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_g);
        operation.addOperand(COSFixed.create(gray, VALUE_GRAY_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorRGB(float r, float g, float b) {
        r = Math.max(0, Math.min(1, r));
        g = Math.max(0, Math.min(1, g));
        b = Math.max(0, Math.min(1, b));
        super.setNonStrokeColorRGB(r, g, b);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_rg);
        operation.addOperand(COSFixed.create(r, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(g, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(b, VALUE_COLOR_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorSpace(COSName name, PDColorSpace colorSpace) {
        super.setNonStrokeColorSpace(name, colorSpace);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        name = checkResource(PDResources.CN_RT_ColorSpace, PDColorSpace.META, name, colorSpace);
        CSOperation operation = new CSOperation(CSOperators.CSO_cs);
        operation.addOperand(name.copyOptional());
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorValues(float[] values) {
        super.setNonStrokeColorValues(values);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_sc);
        for (int i = 0; i < values.length; i++) {
            operation.addOperand(COSFixed.create(values[i], VALUE_COLOR_PRECISION));
        }
        getContent().addOperation(operation);
    }

    @Override
    public void setNonStrokeColorValues(float[] values, COSName name, PDPattern pattern) {
        streamEnd();
        name = checkResource(PDResources.CN_RT_Pattern, PDPattern.META, name, pattern);
        graphicsState.nonStrokeColorValues = values;
        CSOperation operation = new CSOperation(CSOperators.CSO_scn);
        for (int i = 0; i < values.length; i++) {
            operation.addOperand(COSFixed.create(values[i], VALUE_COLOR_PRECISION));
        }
        if (name != null) {
            operation.addOperand(name.copyShallow());
        }
        getContent().addOperation(operation);
    }

    @Override
    public void setRenderingIntent(COSName intent) {
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_ri);
        operation.addOperand(intent.copyOptional());
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorCMYK(float c, float m, float y, float k) {
        c = Math.max(0, Math.min(1, c));
        m = Math.max(0, Math.min(1, m));
        y = Math.max(0, Math.min(1, y));
        k = Math.max(0, Math.min(1, k));
        super.setStrokeColorCMYK(c, m, y, k);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_K);
        operation.addOperand(COSFixed.create(c, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(m, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(y, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(k, VALUE_COLOR_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorGray(float gray) {
        super.setStrokeColorGray(gray);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_G);
        operation.addOperand(COSFixed.create(gray, VALUE_GRAY_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorRGB(float r, float g, float b) {
        r = Math.max(0, Math.min(1, r));
        g = Math.max(0, Math.min(1, g));
        b = Math.max(0, Math.min(1, b));
        super.setStrokeColorRGB(r, g, b);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_RG);
        operation.addOperand(COSFixed.create(r, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(g, VALUE_COLOR_PRECISION));
        operation.addOperand(COSFixed.create(b, VALUE_COLOR_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorSpace(COSName name, PDColorSpace colorSpace) {
        super.setStrokeColorSpace(name, colorSpace);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        name = checkResource(PDResources.CN_RT_ColorSpace, PDColorSpace.META, name, colorSpace);
        CSOperation operation = new CSOperation(CSOperators.CSO_CS);
        operation.addOperand(name);
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorValues(float[] values) {
        super.setStrokeColorValues(values);
        if (!applyOperation) {
            return;
        }
        applyOperation = false;
        streamEnd();
        CSOperation operation = new CSOperation(CSOperators.CSO_SC);
        for (int i = 0; i < values.length; i++) {
            operation.addOperand(COSFixed.create(values[i], VALUE_COLOR_PRECISION));
        }
        getContent().addOperation(operation);
    }

    @Override
    public void setStrokeColorValues(float[] values, COSName name, PDPattern pattern) {
        streamEnd();
        name = checkResource(PDResources.CN_RT_Pattern, PDPattern.META, name, pattern);
        graphicsState.strokeColorValues = values;
        CSOperation operation = new CSOperation(CSOperators.CSO_SCN);
        for (int i = 0; i < values.length; i++) {
            operation.addOperand(COSFixed.create(values[i], VALUE_COLOR_PRECISION));
        }
        if (name != null) {
            operation.addOperand(name.copyShallow());
        }
        getContent().addOperation(operation);
    }

    protected void setTextMode(boolean b) {
        textMode = b;
    }

    /**
	 * Close a currently open line.
	 */
    protected void streamEnd() {
        streamEndRun();
        int size = getStrings().size();
        if (size == 1) {
            Object element = getStrings().get(0);
            if (element instanceof byte[]) {
                byte[] bytes = (byte[]) element;
                basicTextShow(bytes);
            }
        } else {
            if (size > 1) {
                COSArray cosStrings = COSArray.create(size);
                for (Iterator i = getStrings().iterator(); i.hasNext(); ) {
                    Object element = i.next();
                    if (element instanceof byte[]) {
                        byte[] data = (byte[]) element;
                        COSString cosString = COSString.create(data);
                        cosStrings.add(cosString);
                    } else {
                        Integer move = (Integer) element;
                        COSInteger cosMove = COSInteger.create(move.intValue());
                        cosStrings.add(cosMove);
                    }
                }
                streamEndShow(cosStrings);
            }
        }
        resetStrings();
    }

    /**
	 * Close a currently open optimzed character sequence.
	 */
    protected void streamEndRun() {
        if (getTextBuffer().toByteArray().length > 0) {
            getStrings().add(getTextBuffer().toByteArray());
        }
        resetTextBuffer();
    }

    protected void streamEndShow(COSArray theStrings) {
        CSOperation operation = new CSOperation(CSOperators.CSO_TJ);
        operation.addOperand(theStrings);
        getContent().addOperation(operation);
        for (Iterator cosObjects = theStrings.iterator(); cosObjects.hasNext(); ) {
            COSObject cosObject = (COSObject) cosObjects.next();
            if (cosObject instanceof COSString) {
                byte[] text = ((COSString) cosObject).byteValue();
                for (int i = 0; i < text.length; i++) {
                    textState.font.setCharUsed(text[i]);
                }
            }
        }
    }

    @Override
    public void textBegin() {
        if (isTextMode()) {
            return;
        }
        super.textBegin();
        setTextMode(true);
        CSOperation operation = new CSOperation(CSOperators.CSO_BT);
        getContent().addOperation(operation);
    }

    @Override
    public void textEnd() {
        if (!isTextMode()) {
            return;
        }
        streamEnd();
        super.textEnd();
        setTextMode(false);
        CSOperation lastOper = getContent().getLastOperation();
        if ((lastOper != null) && lastOper.matchesOperator(CSOperators.CSO_BT)) {
            getContent().removeLastOperation();
            return;
        }
        CSOperation operation = new CSOperation(CSOperators.CSO_ET);
        getContent().addOperation(operation);
    }

    @Override
    public void textLineMove(float dx, float dy) {
        textBegin();
        streamEnd();
        super.textLineMove(dx, dy);
        CSOperation operation = new CSOperation(CSOperators.CSO_Td);
        operation.addOperand(COSFixed.create(dx, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(dy, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    /**
	 * Move the current text line by <code>dx</code>, <code>dy</code>. Set
	 * the current leading to the <code>dy</code> offset.
	 * <p>
	 * PDF graphics operator "TD"
	 * 
	 * @param dx
	 *            The x offset for the new glyph starting point.
	 * @param dy
	 *            The y offset for the new glyph starting point.
	 */
    public void textLineMoveSetLeading(float dx, float dy) {
        textBegin();
        streamEnd();
        super.textLineMove(dx, dy);
        super.textSetLeading(dy);
        CSOperation operation = new CSOperation(CSOperators.CSO_TD);
        operation.addOperand(COSFixed.create(dx, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(dy, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    public void textLineMoveTo(float x, float y) {
        AffineTransform tx = textState.lineTransform;
        textSetTransform((float) tx.getScaleX(), (float) tx.getShearY(), (float) tx.getShearX(), (float) tx.getScaleY(), x, y);
    }

    @Override
    public void textLineNew() {
        textBegin();
        streamEnd();
        super.textLineNew();
        CSOperation operation = new CSOperation(CSOperators.CSO_Tstar);
        getContent().addOperation(operation);
    }

    @Override
    public void textMove(float dx, float dy) {
        textBegin();
        AffineTransform tm = textState.transform;
        if (dy != 0) {
            textSetTransform((float) tm.getScaleX(), (float) tm.getShearY(), (float) tm.getShearX(), (float) tm.getScaleY(), (float) tm.getTranslateX() + dx, (float) tm.getTranslateY() + dy);
            return;
        }
        if (dx != 0) {
            int iDelta = (int) ((-dx * 1000) / textState.fontSize);
            if (iDelta != 0) {
                if (iDelta < -10000) {
                    textSetTransform((float) tm.getScaleX(), (float) tm.getShearY(), (float) tm.getShearX(), (float) tm.getScaleY(), (float) tm.getTranslateX() + dx, (float) tm.getTranslateY() + dy);
                } else {
                    streamEndRun();
                    getStrings().add(new Integer(iDelta));
                    tm.translate(dx, 0);
                }
            }
        }
    }

    @Override
    public void textMoveTo(float x, float y) {
        textBegin();
        AffineTransform tm = textState.transform;
        float dDeltaY = y - (float) tm.getTranslateY();
        if (dDeltaY != 0) {
            textSetTransform((float) tm.getScaleX(), (float) tm.getShearY(), (float) tm.getShearX(), (float) tm.getScaleY(), x, y);
            return;
        }
        float dDeltaX = x - (float) tm.getTranslateX();
        if (dDeltaX != 0) {
            int iDelta = (int) ((-dDeltaX * 1000) / textState.fontSize);
            if (iDelta != 0) {
                if (iDelta < -10000) {
                    textSetTransform((float) tm.getScaleX(), (float) tm.getShearY(), (float) tm.getShearX(), (float) tm.getScaleY(), x, y);
                } else {
                    streamEndRun();
                    getStrings().add(new Integer(iDelta));
                    textState.transform.translate(dDeltaX, 0);
                }
            }
        }
    }

    @Override
    public void textSetCharSpacing(float charSpace) {
        textBegin();
        streamEnd();
        super.textSetCharSpacing(charSpace);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tc);
        operation.addOperand(COSFixed.create(charSpace));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetFont(COSName name, PDFont font, float size) {
        if (getResources() == null) {
            throw new IllegalStateException("must have resource dictionary to set font");
        }
        textBegin();
        if (isFont(font, size)) {
            return;
        }
        streamEnd();
        name = checkResource(PDResources.CN_RT_Font, PDFont.META, name, font);
        super.textSetFont(name, font, size);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tf);
        operation.addOperand(name);
        operation.addOperand(COSFixed.create(size, VALUE_FONT_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetHorizontalScaling(float scale) {
        textBegin();
        streamEnd();
        super.textSetHorizontalScaling(scale);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tz);
        operation.addOperand(COSFixed.create(scale));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetLeading(float leading) {
        textBegin();
        streamEnd();
        super.textSetLeading(leading);
        CSOperation operation = new CSOperation(CSOperators.CSO_TL);
        operation.addOperand(COSFixed.create(leading));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetRenderingMode(int rendering) {
        textBegin();
        streamEnd();
        super.textSetRenderingMode(rendering);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tr);
        operation.addOperand(COSInteger.create(rendering));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetRise(float rise) {
        textBegin();
        streamEnd();
        super.textSetRise(rise);
        CSOperation operation = new CSOperation(CSOperators.CSO_Ts);
        operation.addOperand(COSFixed.create(rise));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetTransform(float a, float b, float c, float d, float e, float f) {
        textBegin();
        streamEnd();
        super.textSetTransform(a, b, c, d, e, f);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tm);
        operation.addOperand(COSFixed.create(a, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(b, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(c, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(d, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(e, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(f, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }

    @Override
    public void textSetWordSpacing(float wordSpace) {
        textBegin();
        streamEnd();
        super.textSetWordSpacing(wordSpace);
        CSOperation operation = new CSOperation(CSOperators.CSO_Tw);
        operation.addOperand(COSFixed.create(wordSpace));
        getContent().addOperation(operation);
    }

    @Override
    public void textShow(byte[] text, int offset, int length) {
        textBegin();
        getTextBuffer().write(text, offset, length);
        float width = PDFontTools.getGlyphWidthEncoded(textState.font, text, offset, length);
        width = (textState.fontSize * width) / THOUSAND;
        textState.transform.translate(width, 0);
    }

    @Override
    public void textT3SetGlyphWidth(float x, float y) {
        textBegin();
        streamEnd();
        super.textT3SetGlyphWidth(x, y);
        CSOperation operation = new CSOperation(CSOperators.CSO_d0);
        operation.addOperand(COSFixed.create(x));
        operation.addOperand(COSFixed.create(y));
        getContent().addOperation(operation);
    }

    @Override
    public void textT3SetGlyphWidthBB(float x, float y, float llx, float lly, float urx, float ury) {
        textBegin();
        streamEnd();
        super.textT3SetGlyphWidthBB(x, y, llx, lly, urx, ury);
        CSOperation operation = new CSOperation(CSOperators.CSO_d1);
        operation.addOperand(COSFixed.create(x));
        operation.addOperand(COSFixed.create(y));
        operation.addOperand(COSFixed.create(llx));
        operation.addOperand(COSFixed.create(lly));
        operation.addOperand(COSFixed.create(urx));
        operation.addOperand(COSFixed.create(ury));
        getContent().addOperation(operation);
    }

    @Override
    public void transform(float a, float b, float c, float d, float e, float f) {
        textEnd();
        super.transform(a, b, c, d, e, f);
        CSOperation operation = new CSOperation(CSOperators.CSO_cm);
        operation.addOperand(COSFixed.create(a, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(b, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(c, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(d, VALUE_FACTOR_PRECISION));
        operation.addOperand(COSFixed.create(e, VALUE_COORDINATE_PRECISION));
        operation.addOperand(COSFixed.create(f, VALUE_COORDINATE_PRECISION));
        getContent().addOperation(operation);
    }
}
