package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;

public class DriverTextG2d implements UDriver<Graphics2D> {

    private static void printFont() {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String fontNames[] = ge.getAvailableFontFamilyNames();
        final int j = fontNames.length;
        for (int i = 0; i < j; i++) {
            Log.info("Available fonts: " + fontNames[i]);
        }
    }

    public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
        final UText shape = (UText) ushape;
        final FontConfiguration fontConfiguration = shape.getFontConfiguration();
        final UFont font = fontConfiguration.getFont();
        if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
            final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d), font, shape.getText());
            final Color extended = mapper.getMappedColor(fontConfiguration.getExtendedColor());
            if (extended != null) {
                g2d.setColor(extended);
                g2d.setBackground(extended);
                g2d.fill(new Rectangle2D.Double(x, y - dim.getHeight() + 1.5, dim.getWidth(), dim.getHeight()));
            }
        }
        g2d.setFont(font.getFont());
        g2d.setColor(mapper.getMappedColor(fontConfiguration.getColor()));
        g2d.drawString(shape.getText(), (float) x, (float) y);
        if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
            final HtmlColor extended = fontConfiguration.getExtendedColor();
            if (extended != null) {
                g2d.setColor(mapper.getMappedColor(extended));
            }
            final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d), font, shape.getText());
            final int ypos = (int) (y + 2.5);
            g2d.setStroke(new BasicStroke((float) 1.3));
            g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
            g2d.setStroke(new BasicStroke());
        }
        if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
            final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d), font, shape.getText());
            final int ypos = (int) (y + 2.5) - 1;
            final HtmlColor extended = fontConfiguration.getExtendedColor();
            if (extended != null) {
                g2d.setColor(mapper.getMappedColor(extended));
            }
            for (int i = (int) x; i < x + dim.getWidth() - 5; i += 6) {
                g2d.drawLine(i, ypos - 0, i + 3, ypos + 1);
                g2d.drawLine(i + 3, ypos + 1, i + 6, ypos - 0);
            }
        }
        if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
            final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d), font, shape.getText());
            final FontMetrics fm = g2d.getFontMetrics(font.getFont());
            final int ypos = (int) (y - fm.getDescent() - 0.5);
            final HtmlColor extended = fontConfiguration.getExtendedColor();
            if (extended != null) {
                g2d.setColor(mapper.getMappedColor(extended));
            }
            g2d.setStroke(new BasicStroke((float) 1.5));
            g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
            g2d.setStroke(new BasicStroke());
        }
    }

    public static Dimension2D calculateDimension(StringBounder stringBounder, UFont font, String text) {
        final Dimension2D rect = stringBounder.calculateDimension(font, text);
        double h = rect.getHeight();
        if (h < 10) {
            h = 10;
        }
        return new Dimension2DDouble(rect.getWidth(), h);
    }
}
