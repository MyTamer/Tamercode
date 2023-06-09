package org.jalgo.module.ebnf.gui.trans.explanations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.jalgo.main.util.Messages;

/**
 * This class represents a drawn terminal symbol of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ExpTerminal extends Explanation {

    private Font ebnffont;

    /**
	 * Inititalizes the element by getting the name of the symbol
	 * @param ebnfFont the EbnfFont
	 */
    public ExpTerminal(Font ebnfFont) {
        this.ebnffont = ebnfFont;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setColor(Color.BLACK);
        g2d.drawLine(140, 65, 210, 65);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(160, 50, 30, 30, 30, 30);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Courier", Font.PLAIN, 16));
        g2d.drawString("w", 170, 70);
        g2d.drawRoundRect(160, 50, 30, 30, 30, 30);
        g2d.setFont(ebnffont);
        g2d.drawString(Messages.getString("ebnf", "Trans.Sei") + " w ∈ ∑", 10, 30);
        g2d.drawString("• trans(w) =", 50, 70);
    }
}
