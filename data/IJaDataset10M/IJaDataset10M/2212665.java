package sbql4jx.lang.model.util;

import static sbql4jx.lang.model.SourceVersion.RELEASE_6;
import sbql4jx.annotation.processing.SupportedSourceVersion;
import sbql4jx.lang.model.SourceVersion;
import sbql4jx.lang.model.element.Element;
import sbql4jx.lang.model.element.ElementVisitor;
import sbql4jx.lang.model.element.UnknownElementException;

/**
 * A skeletal visitor of program elements with default behavior
 * appropriate for the {@link SourceVersion#RELEASE_6 RELEASE_6}
 * source version.
 *
 * <p> <b>WARNING:</b> The {@code ElementVisitor} interface
 * implemented by this class may have methods added to it in the
 * future to accommodate new, currently unknown, language structures
 * added to future versions of the Java&trade; programming language.
 * Therefore, methods whose names begin with {@code "visit"} may be
 * added to this class in the future; to avoid incompatibilities,
 * classes which extend this class should not declare any instance
 * methods with names beginning with {@code "visit"}.
 *
 * <p>When such a new visit method is added, the default
 * implementation in this class will be to call the {@link
 * #visitUnknown visitUnknown} method.  A new abstract element visitor
 * class will also be introduced to correspond to the new language
 * level; this visitor will have different default behavior for the
 * visit method in question.  When the new visitor is introduced, all
 * or portions of this visitor may be deprecated.
 *
 * @param <R> the return type of this visitor's methods.  Use {@link
 *            Void} for visitors that do not need to return results.
 * @param <P> the type of the additional parameter to this visitor's
 *            methods.  Use {@code Void} for visitors that do not need an
 *            additional parameter.
 *
 * @author Joseph D. Darcy
 * @author Scott Seligman
 * @author Peter von der Ah&eacute;
 * @since 1.6
 */
@SupportedSourceVersion(RELEASE_6)
public abstract class AbstractElementVisitor6<R, P> implements ElementVisitor<R, P> {

    /**
     * Constructor for concrete subclasses to call.
     */
    protected AbstractElementVisitor6() {
    }

    /**
     * Visits any program element as if by passing itself to that
     * element's {@link Element#accept accept} method.  The invocation
     * {@code v.visit(elem)} is equivalent to {@code elem.accept(v,
     * p)}.
     *
     * @param e  the element to visit
     * @param p  a visitor-specified parameter
     * @return a visitor-specified result
     */
    public final R visit(Element e, P p) {
        return e.accept(this, p);
    }

    /**
     * Visits any program element as if by passing itself to that
     * element's {@link Element#accept accept} method and passing
     * {@code null} for the additional parameter.  The invocation
     * {@code v.visit(elem)} is equivalent to {@code elem.accept(v,
     * null)}.
     *
     * @param e  the element to visit
     * @return a visitor-specified result
     */
    public final R visit(Element e) {
        return e.accept(this, null);
    }

    /**
     * {@inheritDoc}
     *
     * <p> The default implementation of this method in
     * {@code AbstractElementVisitor6} will always throw
     * {@code UnknownElementException}.
     * This behavior is not required of a subclass.
     *
     * @param e  the element to visit
     * @param p  a visitor-specified parameter
     * @return a visitor-specified result
     * @throws UnknownElementException
     *          a visitor implementation may optionally throw this exception
     */
    public R visitUnknown(Element e, P p) {
        throw new UnknownElementException(e, p);
    }
}
