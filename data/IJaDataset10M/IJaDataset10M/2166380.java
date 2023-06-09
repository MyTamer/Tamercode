package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.*;

@SuppressWarnings("nls")
public final class TQuotec extends Token {

    public TQuotec() {
        super.setText("\"");
    }

    public TQuotec(int line, int pos) {
        super.setText("\"");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TQuotec(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTQuotec(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TQuotec text.");
    }
}
