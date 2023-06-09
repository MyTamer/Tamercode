package org.makumba.providers;

import java.util.Map;
import org.makumba.DataDefinition;
import org.makumba.commons.NameResolver;

/**
 * Generates the SQL query to be executed against the DBMS, together with the transformed query parameters.
 * Implementations of this interface also take care of makumba-specific concerns regarding query parameters:
 * <ul>
 * <li>expansion of list/vector parameters</li>
 * <li>transformation of named parameters into (ordered) numbered parameters</li>
 * </ul>
 * 
 * @author Manuel Gay
 * @version $Id: SQLQueryGenerator.java,v 1.1 Mar 3, 2010 6:50:10 PM manu Exp $
 */
public interface SQLQueryGenerator {

    /**
     * Sets the arguments this generator should work with
     * 
     * @param arguments
     *            a Map<String, Object> of named arguments
     */
    public void setArguments(Map<String, Object> arguments);

    /**
     * Provides the SQL query to be executed on the DBMS, with expanded and transformed parameters
     * 
     * @param nr
     *            the {@link NameResolver} used to resolve database-level table and field names
     * @return the expanded SQL query String
     */
    public String getSQLQuery(NameResolver nr);

    /**
     * Provides the arguments necessary in order to execute the query on the DBMS, in the order following the one of the
     * query returned by {@link #getSQLQuery(NameResolver, Object)}
     * 
     * @return an object array containing the ordered parameter values
     */
    public Object[] getSQLQueryArguments();

    /**
     * Gets the types of the arguments
     * 
     * @return a DataDefinition with the types of all the arguments
     */
    public DataDefinition getSQLQueryArgumentTypes();

    /**
     * The number of arguments of the query
     */
    public int getSQLArgumentNumber();
}
