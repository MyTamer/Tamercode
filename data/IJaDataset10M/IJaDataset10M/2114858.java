package net.sf.jpasecurity.security.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sf.jpasecurity.ExceptionFactory;
import net.sf.jpasecurity.configuration.AccessRule;
import net.sf.jpasecurity.configuration.DefaultExceptionFactory;
import net.sf.jpasecurity.jpql.compiler.JpqlCompiler;
import net.sf.jpasecurity.jpql.parser.JpqlAccessRule;
import net.sf.jpasecurity.mapping.Alias;
import net.sf.jpasecurity.mapping.MappingInformation;
import net.sf.jpasecurity.mapping.TypeDefinition;

/**
 * This compiler compiles access rules
 * @author Arne Limburg
 */
public class AccessRulesCompiler extends JpqlCompiler {

    private ExceptionFactory exceptionFactory;

    public AccessRulesCompiler(MappingInformation mappingInformation) {
        this(mappingInformation, new DefaultExceptionFactory());
    }

    public AccessRulesCompiler(MappingInformation mappingInformation, ExceptionFactory exceptionFactory) {
        super(mappingInformation, exceptionFactory);
    }

    public Collection<AccessRule> compile(JpqlAccessRule rule) {
        Set<TypeDefinition> typeDefinitions = getAliasDefinitions(rule);
        if (typeDefinitions.isEmpty()) {
            throw exceptionFactory.createRuntimeException("Access rule has no alias specified: " + rule.toString());
        }
        Alias alias = typeDefinitions.iterator().next().getAlias();
        for (TypeDefinition typeDefinition : typeDefinitions) {
            if (!typeDefinition.getAlias().equals(alias)) {
                String message = "An access rule must have exactly one alias specified, found " + alias + " and " + typeDefinition.getAlias() + ": " + rule.toString();
                throw exceptionFactory.createRuntimeException(message);
            }
        }
        Set<String> namedParameters = getNamedParameters(rule);
        if (!namedParameters.isEmpty()) {
            throw exceptionFactory.createRuntimeException("Named parameters are not allowed for access rules");
        }
        if (!getPositionalParameters(rule).isEmpty()) {
            throw exceptionFactory.createRuntimeException("Positional parameters are not allowed for access rules");
        }
        Set<AccessRule> accessRules = new HashSet<AccessRule>();
        for (TypeDefinition typeDefinition : typeDefinitions) {
            accessRules.add(new AccessRule(rule, typeDefinition));
        }
        return Collections.unmodifiableSet(accessRules);
    }
}
