package org.waveprotocol.wave.client.editor.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.StyleInjector;
import org.waveprotocol.wave.client.editor.content.TransparentManager;
import org.waveprotocol.wave.model.document.util.FilteredView.Skip;
import java.util.ArrayList;
import java.util.List;

/**
 * Transparent node manager for diff annotations
 * Keeps track of all diff annotation nodes for a given document
 *
 * @author danilatos@google.com (Daniel Danilatos)
 */
public class DiffManager implements TransparentManager<Element> {

    private static final DiffManagerResources resources = GWT.create(DiffManagerResources.class);

    static {
        StyleInjector.inject(resources.css().getText(), true);
    }

    private static final String DIFF_KEY = NodeManager.getNextMarkerName("dt");

    /**
   * Type of diff annotation
   */
    public enum DiffType {

        /** insert/insertxml op */
        INSERT, /** delete op */
        DELETE
    }

    /**
   * Annotation element we're keeping track of
   */
    private final List<Element> elements = new ArrayList<Element>();

    /**
   * @param element
   * @return The annotation type of the given element, or null if it is not
   *   an element managed by this instance
   */
    public DiffType getDiffType(Element element) {
        return element == null || NodeManager.getTransparentManager(element) != this ? null : (DiffType) element.getPropertyObject(DIFF_KEY);
    }

    /**
   * Create a diff annotation element
   * @param type The type of change it will be annotating
   * @return The newly created element
   */
    public Element createElement(DiffType type) {
        SpanElement element = Document.get().createSpanElement();
        element.setPropertyObject(DIFF_KEY, type);
        NodeManager.setTransparentBackref(element, this);
        switch(type) {
            case INSERT:
                NodeManager.setTransparency(element, Skip.SHALLOW);
                break;
            case DELETE:
                NodeManager.setTransparency(element, Skip.DEEP);
                break;
        }
        styleElement(element, type);
        elements.add(element);
        return element;
    }

    /**
   * Apply styles and other rendering properties to an element to make it look
   * and behave like a diff marker, without actually registering it as such.
   *
   * @param element
   * @param type
   */
    public static void styleElement(Element element, DiffType type) {
        switch(type) {
            case INSERT:
                element.addClassName(resources.css().insert());
                break;
            case DELETE:
                element.addClassName(resources.css().delete());
                NodeManager.setTransparency(element, Skip.DEEP);
                element.setAttribute("contentEditable", "false");
                break;
        }
    }

    /**
   * Clear diffs from the document
   */
    public void clear() {
        TransparencyUtil.clear(elements);
    }

    /**
   * Clear diffs from a document that is being discarded
   * (All it does is clean up backreferences to assist garbage collection)
   */
    public void clearFast() {
        for (Element e : elements) {
            e.setPropertyObject(DIFF_KEY, null);
        }
        elements.clear();
    }

    /**
   * {@inheritDoc}
   */
    public Element needToSplit(Element transparentNode) {
        DiffType type = (DiffType) transparentNode.getPropertyObject(DIFF_KEY);
        if (type == null) {
            throw new IllegalArgumentException("No diff type known for given node");
        }
        return createElement(type);
    }
}
