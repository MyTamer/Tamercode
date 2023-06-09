package homura.hde.ext.font2d;

import homura.hde.core.scene.Text;
import homura.hde.ext.font3d.JmeText;
import homura.hde.ext.font3d.TextFactory;
import java.util.logging.Logger;

public class Text2D extends Text implements JmeText {

    private static final Logger logger = Logger.getLogger(Text2D.class.getName());

    private static final long serialVersionUID = -879022390423155765L;

    Font2D factory;

    public Text2D(Font2D factory, String text, float size, int flags) {
        super("Some2DText", text);
        this.factory = factory;
    }

    public void appendText(String text) {
        getText().append(text);
    }

    public TextFactory getFactory() {
        return factory;
    }

    public int getFlags() {
        logger.warning("Flags play no role on bitmapfonts yet.");
        return 0;
    }

    public float getSize() {
        logger.warning("Size of bitmap-fonts is not supported yet.");
        return 0;
    }

    public void setSize(float size) {
        logger.warning("Resizing of bitmap-fonts is not supported yet.");
    }

    public void setText(String text) {
        print(text);
    }
}
