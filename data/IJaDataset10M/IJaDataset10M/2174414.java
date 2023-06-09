package org.pdfclown.documents.contents.xObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.PropertyList;
import org.pdfclown.documents.contents.layers.ILayerable;
import org.pdfclown.documents.contents.layers.LayerEntity;
import org.pdfclown.files.File;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfObjectWrapper;
import org.pdfclown.objects.PdfStream;

/**
  External graphics object whose contents are defined by a self-contained content stream, separate
  from the content stream in which it is used [PDF:1.6:4.7].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.1, 06/08/11
*/
@PDF(VersionEnum.PDF10)
public abstract class XObject extends PdfObjectWrapper<PdfStream> implements ILayerable {

    /**
    Wraps an external object reference into an external object.
    @param baseObject External object base object.
    @return External object associated to the reference.
  */
    public static XObject wrap(PdfDirectObject baseObject) {
        if (baseObject == null) return null;
        PdfName subtype = (PdfName) ((PdfStream) File.resolve(baseObject)).getHeader().get(PdfName.Subtype);
        if (subtype.equals(PdfName.Form)) return new FormXObject(baseObject); else if (subtype.equals(PdfName.Image)) return new ImageXObject(baseObject); else return null;
    }

    /**
    Creates a new external object inside the document.
  */
    protected XObject(Document context) {
        this(context, new PdfStream());
    }

    /**
    Creates a new external object inside the document.
  */
    protected XObject(Document context, PdfStream baseDataObject) {
        super(context, baseDataObject);
        baseDataObject.getHeader().put(PdfName.Type, PdfName.XObject);
    }

    /**
    Instantiates an existing external object.
  */
    protected XObject(PdfDirectObject baseObject) {
        super(baseObject);
    }

    /**
    Gets the mapping from external-object space to user space.
  */
    public abstract AffineTransform getMatrix();

    /**
    Gets the external object size.
  */
    public abstract Dimension2D getSize();

    /**
    Sets the external object size.
  */
    public abstract void setSize(Dimension2D value);

    @Override
    @PDF(VersionEnum.PDF15)
    public LayerEntity getLayer() {
        return (LayerEntity) PropertyList.wrap(getBaseDataObject().getHeader().get(PdfName.OC));
    }

    @Override
    public void setLayer(LayerEntity value) {
        getBaseDataObject().getHeader().put(PdfName.OC, value.getBaseObject());
    }
}
