package net.sf.oval.guard;

import net.sf.oval.AbstractCheck;
import net.sf.oval.Validator;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

/**
 * @author Sebastian Thomschke
 */
public class PostCheck extends AbstractCheck {

    private static final long serialVersionUID = 1L;

    private String expression;

    private String language;

    private String old;

    public void configure(final Post constraintAnnotation) {
        setMessage(constraintAnnotation.message());
        setErrorCode(constraintAnnotation.errorCode());
        setSeverity(constraintAnnotation.severity());
        setExpression(constraintAnnotation.expr());
        setLanguage(constraintAnnotation.lang());
        setOld(constraintAnnotation.old());
        setProfiles(constraintAnnotation.profiles());
    }

    /**
	 * @return the condition
	 */
    public String getExpression() {
        return expression;
    }

    /**
	 * @return the language
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * @return the old
	 */
    public String getOld() {
        return old;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context, final Validator validator) throws OValException {
        throw new UnsupportedOperationException();
    }

    /**
	 * @param condition the condition to set
	 */
    public void setExpression(final String condition) {
        expression = condition;
    }

    /**
	 * @param language the language to set
	 */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
	 * @param old the old to set
	 */
    public void setOld(final String old) {
        this.old = old;
    }
}
