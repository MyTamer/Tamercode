package org.python.modules.sre;

public class SRE_REPEAT {

    int count;

    int pidx;

    SRE_REPEAT prev;

    SRE_REPEAT(SRE_REPEAT prev) {
        this.prev = prev;
    }
}
