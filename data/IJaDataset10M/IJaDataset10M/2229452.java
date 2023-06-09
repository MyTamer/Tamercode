package org.omg.uml.foundation.core;

/**
 * A_powertype_powertypeRange association proxy interface.
 */
public interface APowertypePowertypeRange extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param powertype Value of the first association end.
     * @param powertypeRange Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Classifier powertype, org.omg.uml.foundation.core.Generalization powertypeRange);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param powertype Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Classifier getPowertype(org.omg.uml.foundation.core.Generalization powertypeRange);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param powertypeRange Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPowertypeRange(org.omg.uml.foundation.core.Classifier powertype);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param powertype Value of the first association end.
     * @param powertypeRange Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Classifier powertype, org.omg.uml.foundation.core.Generalization powertypeRange);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param powertype Value of the first association end.
     * @param powertypeRange Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Classifier powertype, org.omg.uml.foundation.core.Generalization powertypeRange);
}
