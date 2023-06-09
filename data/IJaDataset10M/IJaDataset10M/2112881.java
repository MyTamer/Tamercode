package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.*;

@SuppressWarnings("nls")
public final class TCinclude extends Token {

    public TCinclude() {
        super.setText("\\include");
    }

    public TCinclude(int line, int pos) {
        super.setText("\\include");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TCinclude(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTCinclude(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TCinclude text.");
    }
}
