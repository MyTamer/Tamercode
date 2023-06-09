package org.activebpel.rt.bpel.def.io.readers.def;

import java.util.ArrayList;

/**
 * An extension of the basic spec strategy key, this version represents a strategy that is
 * dynamically generated by the matcher when the to-spec is a 'expression' form.
 */
public class AeExpressionSpecStrategyKey extends AeSpecStrategyKey {

    /** The query's variable ref. */
    private String mVariableName;

    /** The query's (optional) part. */
    private String mPartName;

    /** The query language (optional). */
    private String mQueryLanguage;

    /** The query's (optional) relative path query. */
    private String mQuery;

    /**
    * Constructs a query spec strategy key.
    * 
    * @param aStrategyName
    * @param aVariableName
    * @param aPartName
    * @param aQueryLanguage
    * @param aQuery
    */
    public AeExpressionSpecStrategyKey(String aStrategyName, String aVariableName, String aPartName, String aQueryLanguage, String aQuery) {
        super(aStrategyName);
        setVariableName(aVariableName);
        setPartName(aPartName);
        setQueryLanguage(aQueryLanguage);
        setQuery(aQuery);
    }

    /**
    * @see org.activebpel.rt.bpel.def.io.readers.def.AeSpecStrategyKey#getStrategyArguments()
    */
    public Object[] getStrategyArguments() {
        ArrayList list = new ArrayList();
        list.add(getVariableName());
        if (getPartName() != null) list.add(getPartName());
        if (getQuery() != null) {
            list.add(getQuery());
            list.add(getQueryLanguage());
        }
        return list.toArray();
    }

    /**
    * @see org.activebpel.rt.bpel.def.io.readers.def.AeSpecStrategyKey#hasArguments()
    */
    public boolean hasArguments() {
        return true;
    }

    /**
    * @return Returns the query.
    */
    public String getQuery() {
        return mQuery;
    }

    /**
    * @param aQuery The query to set.
    */
    protected void setQuery(String aQuery) {
        mQuery = aQuery;
    }

    /**
    * @return Returns the partName.
    */
    public String getPartName() {
        return mPartName;
    }

    /**
    * @param aPartName The partName to set.
    */
    protected void setPartName(String aPartName) {
        mPartName = aPartName;
    }

    /**
    * @return Returns the variableName.
    */
    public String getVariableName() {
        return mVariableName;
    }

    /**
    * @param aVariableName The variableName to set.
    */
    protected void setVariableName(String aVariableName) {
        mVariableName = aVariableName;
    }

    /**
    * @return Returns the queryLanguage.
    */
    public String getQueryLanguage() {
        return mQueryLanguage;
    }

    /**
    * @param aQueryLanguage The queryLanguage to set.
    */
    protected void setQueryLanguage(String aQueryLanguage) {
        mQueryLanguage = aQueryLanguage;
    }
}
