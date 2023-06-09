package net.sf.jpasecurity.jpql;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.jpasecurity.jpql.parser.Node;
import net.sf.jpasecurity.mapping.MappingInformation;
import net.sf.jpasecurity.mapping.Path;
import net.sf.jpasecurity.mapping.TypeDefinition;

/**
 * This class represents compiled JPQL statements.
 * It contains methods to access the structure of a JPQL statement.
 * @author Arne Limburg
 */
public class JpqlCompiledStatement extends JpqlStatementHolder {

    private List<Path> selectedPaths;

    private Set<TypeDefinition> typeDefinitions;

    private Set<String> namedParameters;

    public JpqlCompiledStatement(Node statement, List<Path> selectedPathes, Set<TypeDefinition> typeDefinitions, Set<String> namedParameters) {
        super(statement);
        this.selectedPaths = selectedPathes;
        this.typeDefinitions = typeDefinitions;
        this.namedParameters = namedParameters;
    }

    public JpqlCompiledStatement(Node statement) {
        this(statement, Collections.<Path>emptyList(), Collections.<TypeDefinition>emptySet(), Collections.<String>emptySet());
    }

    /**
     * Returns the paths of the select-clause.
     */
    public List<Path> getSelectedPaths() {
        return selectedPaths;
    }

    /**
     * Returns the types of the selected paths.
     * @param mappingInformation the mapping information to determine the types
     * @return the types
     * @see #getSelectedPaths()
     */
    public Map<Path, Class<?>> getSelectedTypes(MappingInformation mappingInformation) {
        Map<Path, Class<?>> selectedTypes = new HashMap<Path, Class<?>>();
        for (Path selectedPath : getSelectedPaths()) {
            selectedTypes.put(selectedPath, mappingInformation.getType(selectedPath, getTypeDefinitions()));
        }
        return selectedTypes;
    }

    /**
     * Returns the type-definitions of the from-clause and join-clauses.
     */
    public Set<TypeDefinition> getTypeDefinitions() {
        return typeDefinitions;
    }

    public Set<String> getNamedParameters() {
        return namedParameters;
    }

    public JpqlCompiledStatement clone() {
        return (JpqlCompiledStatement) super.clone();
    }
}
