package org.apache.velocity.runtime.directive;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.Renderable;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Directive that puts an unrendered AST block in the context
 * under the specified key, postponing rendering until the
 * reference is used and rendered.
 *
 * @author Andrew Tetlaw
 * @author Nathan Bubna
 * @version $Id: Define.java 686842 2008-08-18 18:29:31Z nbubna $
 */
public class Define extends Directive {

    private String key;

    private Node block;

    private Log log;

    private int maxDepth;

    private String definingTemplate;

    /**
     * Return name of this directive.
     */
    public String getName() {
        return "define";
    }

    /**
     * Return type of this directive.
     */
    public int getType() {
        return BLOCK;
    }

    /**
     *  simple init - get the key
     */
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
        super.init(rs, context, node);
        log = rs.getLog();
        maxDepth = rs.getInt(RuntimeConstants.DEFINE_DIRECTIVE_MAXDEPTH, 2);
        key = node.jjtGetChild(0).getFirstToken().image.substring(1);
        block = node.jjtGetChild(1);
        definingTemplate = context.getCurrentTemplateName();
    }

    /**
     * directive.render() simply makes an instance of the Block inner class
     * and places it into the context as indicated.
     */
    public boolean render(InternalContextAdapter context, Writer writer, Node node) {
        context.put(key, new Block(context, this));
        return true;
    }

    /**
     * Creates a string identifying the source and location of the block
     * definition, and the current template being rendered if that is
     * different.
     */
    protected String id(InternalContextAdapter context) {
        StrBuilder str = new StrBuilder(100).append("block $").append(key).append(" (defined in ").append(definingTemplate).append(" [line ").append(getLine()).append(", column ").append(getColumn()).append("])");
        if (!context.getCurrentTemplateName().equals(definingTemplate)) {
            str.append(" used in ").append(context.getCurrentTemplateName());
        }
        return str.toString();
    }

    /**
     * actual class placed in the context, holds the context and writer
     * being used for the render, as well as the parent (which already holds
     * everything else we need).
     */
    public static class Block implements Renderable {

        private InternalContextAdapter context;

        private Define parent;

        private int depth;

        public Block(InternalContextAdapter context, Define parent) {
            this.context = context;
            this.parent = parent;
        }

        /**
         *
         */
        public boolean render(InternalContextAdapter context, Writer writer) {
            try {
                depth++;
                if (depth > parent.maxDepth) {
                    parent.log.debug("Max recursion depth reached for " + parent.id(context));
                    depth--;
                    return false;
                } else {
                    parent.block.render(context, writer);
                    depth--;
                    return true;
                }
            } catch (IOException e) {
                String msg = "Failed to render " + parent.id(context) + " to writer";
                parent.log.error(msg, e);
                throw new RuntimeException(msg, e);
            } catch (VelocityException ve) {
                String msg = "Failed to render " + parent.id(context) + " due to " + ve;
                parent.log.error(msg, ve);
                throw ve;
            }
        }

        public String toString() {
            Writer stringwriter = new StringWriter();
            if (render(context, stringwriter)) {
                return stringwriter.toString();
            } else {
                return null;
            }
        }
    }
}
