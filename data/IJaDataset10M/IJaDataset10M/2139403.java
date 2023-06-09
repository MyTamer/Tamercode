package org.hypergraphdb.viewer.visual;

import java.io.Serializable;
import org.hypergraphdb.viewer.phoebe.PEdgeView;

/**
 * This class is a replacement for the yFiles Arrow class.
 */
public class Arrow implements Serializable {

    public static final Arrow NONE = new Arrow("NONE");

    public static final Arrow BLACK_DIAMOND = new Arrow("BLACK_DIAMOND");

    public static final Arrow WHITE_DIAMOND = new Arrow("WHITE_DIAMOND");

    public static final Arrow COLOR_DIAMOND = new Arrow("COLOR_DIAMOND");

    public static final Arrow BLACK_DELTA = new Arrow("BLACK_DELTA");

    public static final Arrow WHITE_DELTA = new Arrow("WHITE_DELTA");

    public static final Arrow COLOR_DELTA = new Arrow("COLOR_DELTA");

    public static final Arrow BLACK_ARROW = new Arrow("BLACK_ARROW");

    public static final Arrow WHITE_ARROW = new Arrow("WHITE_ARROW");

    public static final Arrow COLOR_ARROW = new Arrow("COLOR_ARROW");

    public static final Arrow BLACK_T = new Arrow("BLACK_T");

    public static final Arrow WHITE_T = new Arrow("WHITE_T");

    public static final Arrow COLOR_T = new Arrow("COLOR_T");

    public static final Arrow BLACK_CIRCLE = new Arrow("BLACK_CIRCLE");

    public static final Arrow WHITE_CIRCLE = new Arrow("WHITE_CIRCLE");

    public static final Arrow COLOR_CIRCLE = new Arrow("COLOR_CIRCLE");

    String name;

    public Arrow(String name) {
        this.name = name;
    }

    public int getGinyArrow() {
        if (name.equals("WHITE_DIAMOND")) {
            return PEdgeView.WHITE_DIAMOND;
        } else if (name.equals("BLACK_DIAMOND")) {
            return PEdgeView.BLACK_DIAMOND;
        } else if (name.equals("COLOR_DIAMOND")) {
            return PEdgeView.EDGE_COLOR_DIAMOND;
        } else if (name.equals("WHITE_DELTA")) {
            return PEdgeView.WHITE_DELTA;
        } else if (name.equals("BLACK_DELTA")) {
            return PEdgeView.BLACK_DELTA;
        } else if (name.equals("COLOR_DELTA")) {
            return PEdgeView.EDGE_COLOR_DELTA;
        } else if (name.equals("WHITE_ARROW")) {
            return PEdgeView.WHITE_ARROW;
        } else if (name.equals("BLACK_ARROW")) {
            return PEdgeView.BLACK_ARROW;
        } else if (name.equals("COLOR_ARROW")) {
            return PEdgeView.EDGE_COLOR_ARROW;
        } else if (name.equals("WHITE_T")) {
            return PEdgeView.WHITE_T;
        } else if (name.equals("BLACK_T")) {
            return PEdgeView.BLACK_T;
        } else if (name.equals("COLOR_T")) {
            return PEdgeView.EDGE_COLOR_T;
        } else if (name.equals("WHITE_CIRCLE")) {
            return PEdgeView.WHITE_CIRCLE;
        } else if (name.equals("BLACK_CIRCLE")) {
            return PEdgeView.BLACK_CIRCLE;
        } else if (name.equals("COLOR_CIRCLE")) {
            return PEdgeView.EDGE_COLOR_CIRCLE;
        } else {
            return PEdgeView.NO_END;
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public static Arrow parseArrowText(String text) {
        String arrowtext = text.trim();
        if (arrowtext.equals("WHITE_DIAMOND")) {
            return Arrow.WHITE_DIAMOND;
        } else if (arrowtext.equals("BLACK_DIAMOND")) {
            return Arrow.BLACK_DIAMOND;
        } else if (arrowtext.equals("COLOR_DIAMOND")) {
            return Arrow.COLOR_DIAMOND;
        } else if (arrowtext.equals("WHITE_DELTA")) {
            return Arrow.WHITE_DELTA;
        } else if (arrowtext.equals("BLACK_DELTA")) {
            return Arrow.BLACK_DELTA;
        } else if (arrowtext.equals("COLOR_DELTA")) {
            return Arrow.COLOR_DELTA;
        } else if (arrowtext.equals("WHITE_ARROW")) {
            return Arrow.WHITE_ARROW;
        } else if (arrowtext.equals("BLACK_ARROW")) {
            return Arrow.BLACK_ARROW;
        } else if (arrowtext.equals("COLOR_ARROW")) {
            return Arrow.COLOR_ARROW;
        } else if (arrowtext.equals("WHITE_T")) {
            return Arrow.WHITE_T;
        } else if (arrowtext.equals("BLACK_T")) {
            return Arrow.BLACK_T;
        } else if (arrowtext.equals("COLOR_T")) {
            return Arrow.COLOR_T;
        } else if (arrowtext.equals("WHITE_CIRCLE")) {
            return Arrow.WHITE_CIRCLE;
        } else if (arrowtext.equals("BLACK_CIRCLE")) {
            return Arrow.BLACK_CIRCLE;
        } else if (arrowtext.equals("COLOR_CIRCLE")) {
            return Arrow.COLOR_CIRCLE;
        } else {
            return Arrow.NONE;
        }
    }
}
