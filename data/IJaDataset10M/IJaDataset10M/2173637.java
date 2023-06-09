package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TEvar extends Token {

    public TEvar() {
        super.setText("evar");
    }

    public TEvar(int line, int pos) {
        super.setText("evar");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TEvar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTEvar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TEvar text.");
    }
}
