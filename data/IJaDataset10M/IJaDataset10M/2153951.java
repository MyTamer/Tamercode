package freemarker.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.ClassUtil;
import freemarker.template.utility.StringUtil;

/**
 * A {@link TemplateClassResolver} that resolves only the classes whose name 
 * was specified in the constructor.
 */
public class OptInTemplateClassResolver implements TemplateClassResolver {

    private final Set allowedClasses;

    private final List trustedTemplatePrefixes;

    private final Set trustedTemplateNames;

    /**
     * Creates a new instance. 
     *
     * @param allowedClasses the {@link Set} of {@link String}-s that contains
     *     the full-qualified names of the allowed classes.
     *     Can be <code>null</code> (means not class is allowed).
     * @param trustedTemplates the {@link List} of {@link String}-s that contains
     *     template names (i.e., template root directory relative paths)
     *     and prefix patterns (like <code>"include/*"</code>) of templates
     *     for which {@link TemplateClassResolver#SAFER_RESOLVER} will be 
     *     used (which is not as safe as {@link OptInTemplateClassResolver}).
     *     The list items need not start with <code>"/"</code> (if they are, it
     *     will be removed). List items ending with <code>"*"</code> are treated
     *     as prefixes (i.e. <code>"foo*"</code> matches <code>"foobar"</code>,
     *     <code>"foo/bar/baaz"</code>, <code>"foowhatever/bar/baaz"</code>,
     *     etc.). The <code>"*"</code> has no special meaning anywhere else.
     *     The matched template name is the name (template root directory
     *     relative path) of the template that directly (lexically) contains the
     *     operation (like <code>?new</code>) that wants to get the class. Thus,
     *     if a trusted template includes a non-trusted template, the
     *     <code>allowedClasses</code> restriction will apply in the included
     *     template.
     *     This parameter can be <code>null</code> (means no trusted templates).
     */
    public OptInTemplateClassResolver(Set allowedClasses, List trustedTemplates) {
        this.allowedClasses = allowedClasses != null ? allowedClasses : Collections.EMPTY_SET;
        if (trustedTemplates != null) {
            trustedTemplateNames = new HashSet();
            trustedTemplatePrefixes = new ArrayList();
            Iterator it = trustedTemplates.iterator();
            while (it.hasNext()) {
                String li = (String) it.next();
                if (li.startsWith("/")) li = li.substring(1);
                if (li.endsWith("*")) {
                    trustedTemplatePrefixes.add(li.substring(0, li.length() - 1));
                } else {
                    trustedTemplateNames.add(li);
                }
            }
        } else {
            trustedTemplateNames = Collections.EMPTY_SET;
            trustedTemplatePrefixes = Collections.EMPTY_LIST;
        }
    }

    public Class resolve(String className, Environment env, Template template) throws TemplateException {
        String templateName = safeGetTemplateName(template);
        if (templateName != null && (trustedTemplateNames.contains(templateName) || hasMatchingPrefix(templateName))) {
            return TemplateClassResolver.SAFER_RESOLVER.resolve(className, env, template);
        } else {
            if (!allowedClasses.contains(className)) {
                throw new TemplateException("Instantiating " + className + " is not allowed in the " + "template for security reasons. (If you meet this problem " + "when using ?new in a template, you may want to look " + "at the \"" + Configurable.NEW_BUILTIN_CLASS_RESOLVER_KEY + "\" setting in the FreeMarker configuration.)", env);
            } else {
                try {
                    return ClassUtil.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new TemplateException(e, env);
                }
            }
        }
    }

    /**
     * Extract the template name from the template object which will be matched
     * against the trusted template names and pattern. 
     */
    protected String safeGetTemplateName(Template template) {
        if (template == null) return null;
        String name = template.getName();
        if (name == null) return null;
        String decodedName = name;
        if (decodedName.indexOf('%') != -1) {
            decodedName = StringUtil.replace(decodedName, "%2e", ".", false, false);
            decodedName = StringUtil.replace(decodedName, "%2E", ".", false, false);
            decodedName = StringUtil.replace(decodedName, "%2f", "/", false, false);
            decodedName = StringUtil.replace(decodedName, "%2F", "/", false, false);
            decodedName = StringUtil.replace(decodedName, "%5c", "\\", false, false);
            decodedName = StringUtil.replace(decodedName, "%5C", "\\", false, false);
        }
        int dotDotIdx = decodedName.indexOf("..");
        if (dotDotIdx != -1) {
            int before = dotDotIdx - 1 >= 0 ? decodedName.charAt(dotDotIdx - 1) : -1;
            int after = dotDotIdx + 2 < decodedName.length() ? decodedName.charAt(dotDotIdx + 2) : -1;
            if ((before == -1 || before == '/' || before == '\\') && (after == -1 || after == '/' || after == '\\')) {
                return null;
            }
        }
        return name.startsWith("/") ? name.substring(1) : name;
    }

    private boolean hasMatchingPrefix(String name) {
        for (int i = 0; i < trustedTemplatePrefixes.size(); i++) {
            String prefix = (String) trustedTemplatePrefixes.get(i);
            if (name.startsWith(prefix)) return true;
        }
        return false;
    }
}
