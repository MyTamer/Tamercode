package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

public final class TDisplay extends Token {

    public TDisplay(String text) {
        setText(text);
    }

    public TDisplay(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TDisplay(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTDisplay(this);
    }
}
