package corina.prefs.components;

import java.awt.Font;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class FontRenderer extends JLabel implements TableCellRenderer {

    Border unselectedBorder = null;

    Border selectedBorder = null;

    boolean isBordered = true;

    public FontRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true);
    }

    private static final String getDescription(Font f) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(f.getFamily());
        sb.append("] ");
        sb.append(f.getName());
        sb.append(" ");
        if ((f.getStyle() & Font.PLAIN) != 0) {
            sb.append("plain,");
        }
        if ((f.getStyle() & Font.BOLD) != 0) {
            sb.append("bold,");
        }
        if ((f.getStyle() & Font.ITALIC) != 0) {
            sb.append("italic,");
        }
        sb.setLength(sb.length() - 1);
        sb.append(" ");
        sb.append(f.getSize2D());
        return sb.toString();
    }

    public Component getTableCellRendererComponent(JTable table, Object font, boolean isSelected, boolean hasFocus, int row, int column) {
        Font newFont = (Font) font;
        String text = getDescription(newFont);
        setText(text);
        setFont(newFont);
        setBackground(table.getBackground());
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        setToolTipText(text);
        return this;
    }
}
