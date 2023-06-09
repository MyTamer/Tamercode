package gnu.kawa.xml;

import gnu.lists.*;
import gnu.mapping.*;

/** A procedure that implements the "response-header" function.
 * It is implemented by report an attribute objects.
 * Document-level attributes are otherwise not valid. */
public class MakeResponseHeader extends MethodProc {

    public static MakeResponseHeader makeResponseHeader = new MakeResponseHeader();

    public void apply(CallContext ctx) {
        String key = ctx.getNextArg().toString();
        Object val = ctx.getNextArg();
        ctx.lastArg();
        Consumer out = ctx.consumer;
        out.startAttribute(key);
        out.write(val.toString());
        out.endAttribute();
    }
}
