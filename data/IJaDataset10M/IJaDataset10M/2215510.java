package CH.ifa.draw.contrib.html;

import java.io.IOException;
import java.io.Serializable;
import CH.ifa.draw.util.StorableInput;
import CH.ifa.draw.util.StorableOutput;
import CH.ifa.draw.standard.TextHolder;

/**
 * TextAreaFigureContentProducer produces text contents from an existing
 * TextHolder figure<br>
 * It can either be specific if set for a specific figure, or generic, encoding
 * any color passed to the getContents method.<br>
 * The main usage of this producer is to embed a "master" or "shared" drawing
 * figure into other figures so that updating the master figure automatically
 * changes all dependent figures as well. Kind of a hot text snippet if you like<br>
 *
 * @author  Eduardo Francos - InContext
 * @created 30 avril 2002
 * @version <$CURRENT_VERSION$>
 */
public class TextHolderContentProducer extends AbstractContentProducer implements Serializable {

    private TextHolder myTextHolder;

    /**
	 * Constructor for the TextAreaFigureContentProducer object
	 */
    public TextHolderContentProducer() {
    }

    /**
	 *Constructor for the TextAreaFigureContentProducer object
	 *
	 * @param figure  Description of the Parameter
	 */
    public TextHolderContentProducer(TextHolder figure) {
        setTextHolder(figure);
    }

    /**
	 * Gets the text from the text figure
	 *
	 * @param context       Description of the Parameter
	 * @param ctxAttrName   Description of the Parameter
	 * @param ctxAttrValue  Description of the Parameter
	 * @return              The content value
	 */
    public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue) {
        TextHolder figure = (getTextHolder() != null) ? getTextHolder() : (TextHolder) ctxAttrValue;
        return figure.getText();
    }

    /**
	 * Writes the storable
	 *
	 * @param dw  the storable output
	 */
    public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeStorable(getTextHolder().getRepresentingFigure());
    }

    /**
	 * Writes the storable
	 *
	 * @param dr               the storable input
	 * @exception IOException  thrown by called methods
	 */
    public void read(StorableInput dr) throws IOException {
        super.read(dr);
        setTextHolder((TextHolder) dr.readStorable());
    }

    protected TextHolder getTextHolder() {
        return myTextHolder;
    }

    public void setTextHolder(TextHolder newFigure) {
        myTextHolder = newFigure;
    }
}
