package javax.microedition.lcdui;

/**
 * 
 * @author Andre Nijholt
 */
public class TextBox extends Screen {

    private final char[][] keyboard = { { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p' }, { 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 8 }, { 5, 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', 13 }, { ' ', ' ', ' ', ' ', ' ', ' ', '-', '!', '.', ' ' }, { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P' }, { 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 8 }, { 5, 'Z', 'X', 'C', 'V', 'B', 'N', 'M', ';', 13 }, { ' ', ' ', ' ', ' ', ' ', ' ', '-', '!', '.', ' ' }, { '(', ')', '"', '+', '*', '1', '2', '3', '@', '_' }, { '<', '>', '\'', '-', '/', '4', '5', '6', '#', 8 }, { '[', ']', '^', '|', '=', '7', '8', '9', '~', 13 }, { ' ', ' ', ' ', ' ', ' ', ' ', '0', '%', '&', '$' } };

    private final Image digits = new Image(8, 8, new byte[] { (byte) 0x02, (byte) 0x1f, (byte) 0x64, (byte) 0x52, (byte) 0x4c, (byte) 0xa8, (byte) 0xa8, (byte) 0x50 });

    private final Image chars = new Image(8, 8, new byte[] { (byte) 0x0e, (byte) 0x05, (byte) 0x0e, (byte) 0x7c, (byte) 0x54, (byte) 0x68, (byte) 0x90, (byte) 0x90 });

    private String text;

    private int maxSize;

    private int constraints;

    private char[] inputText = new char[16];

    private int inputIdx = 0;

    private int kSel;

    private int xSel;

    private int ySel;

    public TextBox(String title, String text, int maxSize, int constraints) {
        this.title = title;
        this.text = text;
        this.maxSize = maxSize;
        this.constraints = constraints;
    }

    public void setText(String text) {
        this.text = text;
        char[] caText = text.toCharArray();
        for (int i = 0; (i < caText.length) && (i < inputText.length); i++) {
            inputText[i] = caText[i];
        }
        inputIdx = caText.length;
    }

    public String getText() {
        return text;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setConstraints(int constraints) {
        this.constraints = constraints;
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == KEY_RIGHT) {
            xSel = (xSel + 1) % 10;
            repaint();
        } else if (keyCode == KEY_LEFT) {
            xSel = (xSel == 0) ? 9 : (xSel - 1);
            repaint();
        } else if (keyCode == KEY_BACK) {
            ySel = (ySel + 1) % 4;
            repaint();
        } else if (keyCode == KEY_ENTER) {
            if ((xSel == 0) && (ySel == 2) && ((kSel == 0) || (kSel == 4))) {
                kSel = (kSel == 4) ? 0 : 4;
            } else if ((xSel == 0) && (ySel == 3)) {
                kSel = (kSel == 8) ? 0 : 8;
            } else if ((xSel == 9) && (ySel == 1)) {
                if (inputIdx > 0) {
                    inputText[--inputIdx] = '\0';
                }
            } else if ((xSel == 9) && (ySel == 2)) {
                text = new String(inputText, 0, inputIdx);
                for (int i = 0; i < inputText.length; i++) {
                    inputText[i] = '\0';
                }
                inputIdx = 0;
                for (int i = 0; i < commands.size(); i++) {
                    callCommandListener();
                }
            } else if (inputIdx < maxSize) {
                inputText[inputIdx++] = keyboard[kSel + ySel][xSel];
                if (!checkConstraints()) {
                    inputText[--inputIdx] = '\0';
                }
            }
            repaint();
        }
    }

    public void paint(Graphics g) {
        int line = Display.CHAR_HEIGHT;
        int ch = Display.CHAR_WIDTH;
        if (title != null) {
            g.drawString(title, 0, 1 * line);
        }
        for (int i = 0; (i < inputIdx) && (inputText[i] > '\0'); i++) {
            g.drawChar(inputText[i], (i * ch), 2 * line, false);
        }
        g.drawRect(0, 31, 99, 32);
        g.drawLine(0, 39, 100, 39);
        g.drawLine(0, 47, 100, 47);
        g.drawLine(0, 55, 100, 55);
        g.drawLine(9, 32, 10, 99);
        g.drawLine(20, 32, 20, 99);
        g.drawLine(30, 32, 30, 99);
        g.drawLine(40, 32, 40, 99);
        g.drawLine(50, 32, 50, 99);
        g.drawLine(60, 32, 60, 99);
        g.drawLine(70, 32, 70, 99);
        g.drawLine(80, 32, 80, 99);
        g.drawLine(90, 32, 90, 99);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 4; y++) {
                g.drawChar(keyboard[kSel + y][x], (x * 10) + 2, (y + 4) * line, ((x == xSel) && (y == ySel)));
            }
        }
        g.drawImage((kSel == 8) ? chars : digits, 1, 56, (xSel == 0) && (ySel == 3));
    }

    private boolean checkConstraints() {
        if (constraints == TextField.ANY) {
            return true;
        }
        return false;
    }
}
