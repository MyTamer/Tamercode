package org.xmlpull.v1.builder.xpath.jaxen.function;

import org.xmlpull.v1.builder.xpath.jaxen.Context;
import org.xmlpull.v1.builder.xpath.jaxen.Function;
import org.xmlpull.v1.builder.xpath.jaxen.FunctionCallException;
import org.xmlpull.v1.builder.xpath.jaxen.Navigator;
import java.util.List;

/**
 * <p><b>4.1</b> <code><i>string</i> namespace-uri(<i>node-set?</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class NamespaceUriFunction implements Function {

    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 0) {
            return evaluate(context.getNodeSet(), context.getNavigator());
        }
        if (args.size() == 1) {
            return evaluate(args, context.getNavigator());
        }
        throw new FunctionCallException("namespace-uri() requires zero or one argument.");
    }

    public static String evaluate(List list, Navigator nav) {
        if (!list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof List) {
                return evaluate((List) first, nav);
            } else if (nav.isElement(first)) {
                return nav.getElementNamespaceUri(first);
            } else if (nav.isAttribute(first)) {
                return nav.getAttributeNamespaceUri(first);
            }
        }
        return "";
    }
}
