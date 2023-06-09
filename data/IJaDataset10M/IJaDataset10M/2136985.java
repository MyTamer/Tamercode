package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

public final class TFormat extends Token {

    public TFormat(String text) {
        setText(text);
    }

    public TFormat(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TFormat(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTFormat(this);
    }
}
