package net.sf.cb2xml.sablecc.node;

import net.sf.cb2xml.sablecc.analysis.*;

public final class TRparen extends Token {

    public TRparen() {
        super.setText(")");
    }

    public TRparen(int line, int pos) {
        super.setText(")");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TRparen(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTRparen(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TRparen text.");
    }
}
