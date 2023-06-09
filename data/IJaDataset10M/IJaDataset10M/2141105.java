package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TUvar extends Token {

    public TUvar() {
        super.setText("uvar");
    }

    public TUvar(int line, int pos) {
        super.setText("uvar");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TUvar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTUvar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TUvar text.");
    }
}
