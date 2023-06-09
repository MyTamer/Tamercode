package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TLoop extends Token {

    public TLoop() {
        super.setText("loop");
    }

    public TLoop(int line, int pos) {
        super.setText("loop");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLoop(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLoop(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLoop text.");
    }
}
