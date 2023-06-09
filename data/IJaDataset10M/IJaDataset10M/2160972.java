package org.omg.uml.behavioralelements.collaborations;

/**
 * A_interaction_interactionInstanceSet association proxy interface.
 */
public interface AInteractionInteractionInstanceSet extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param interaction Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.Interaction interaction, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param interaction Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.collaborations.Interaction getInteraction(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param interactionInstanceSet Required value of the second association 
     * end.
     * @return Collection of related objects.
     */
    public java.util.Collection getInteractionInstanceSet(org.omg.uml.behavioralelements.collaborations.Interaction interaction);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param interaction Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.Interaction interaction, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param interaction Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.Interaction interaction, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);
}
