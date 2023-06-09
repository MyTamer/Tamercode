package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TCatch extends Token {

    public TCatch() {
        super.setText("catch");
    }

    public TCatch(int line, int pos) {
        super.setText("catch");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TCatch(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTCatch(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TCatch text.");
    }
}
