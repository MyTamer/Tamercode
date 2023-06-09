package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TWord extends Token {

    public TWord(String text) {
        setText(text);
    }

    public TWord(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TWord(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTWord(this);
    }
}
