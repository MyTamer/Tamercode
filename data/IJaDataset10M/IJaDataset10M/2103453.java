package uk.ac.ed.ph.snuggletex.dombuilding;

import uk.ac.ed.ph.snuggletex.internal.DOMBuilder;
import uk.ac.ed.ph.snuggletex.internal.SnuggleParseException;
import uk.ac.ed.ph.snuggletex.tokens.ArgumentContainerToken;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import org.w3c.dom.Element;

/**
 * Handles the LaTeX <tt>\\sqrt</tt> command, which generates either a <tt>msqrt</tt>
 * or <tt>mroot</tt> depending on whether an optional argument has been passed or not.
 *
 * @author  David McKain
 * @version $Revision: 525 $
 */
public final class MathRootHandler implements CommandHandler {

    public void handleCommand(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        ArgumentContainerToken optionalArgument = token.getOptionalArgument();
        ArgumentContainerToken requiredArgument = token.getArguments()[0];
        Element result;
        if (optionalArgument != null) {
            result = builder.appendMathMLElement(parentElement, "mroot");
            builder.handleMathTokensAsSingleElement(result, requiredArgument);
            builder.handleMathTokensAsSingleElement(result, optionalArgument);
        } else {
            result = builder.appendMathMLElement(parentElement, "msqrt");
            builder.handleMathTokensAsSingleElement(result, requiredArgument);
        }
    }
}
