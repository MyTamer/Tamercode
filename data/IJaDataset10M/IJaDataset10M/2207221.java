package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

public final class TUnknown extends Token {

    public TUnknown(String text) {
        setText(text);
    }

    public TUnknown(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TUnknown(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTUnknown(this);
    }
}
