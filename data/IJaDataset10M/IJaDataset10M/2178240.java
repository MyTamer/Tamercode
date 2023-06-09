package org.dbgen.event;

/**
 * This class was generated by a SmartGuide.
 *
 */
public class RelationDeletedEvent extends java.util.EventObject {

    private org.dbgen.Relation relation;

    /**
   * TableAddedEvent constructor comment.
   * @param source java.lang.Object
   */
    public RelationDeletedEvent(Object source) {
        super(source);
    }

    /**
   * This method was created by a SmartGuide.
   * @param source java.lang.Object
   * @param table org.dbgen.Table
   */
    public RelationDeletedEvent(Object source, org.dbgen.Relation relation) {
        super(source);
        this.relation = relation;
    }

    /**
   * This method was created by a SmartGuide.
   * @return org.dbgen.Table
   */
    public org.dbgen.Relation getRelation() {
        return relation;
    }
}
