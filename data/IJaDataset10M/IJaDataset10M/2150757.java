package org.didicero.base.entity;

/**
 * Autogenerated POJO EJB class for TextNode containing the 
 * bulk of the entity implementation.
 *
 * This is autogenerated by AndroMDA using the EJB3
 * cartridge.
 *
 * DO NOT MODIFY this class.
 *
 * <p>
 * <html>
 * </p>
 * <p>
 * <head>
 * </p>
 * <p>
 * </head>
 * </p>
 * <p>
 * <body>
 * </p>
 * <p>
 * <p>
 * </p>
 * <p>
 * A translatable piece of text containing one or more words, like
 * a <a
 * href="mdel://_9_5_1_25e60543_1263220623000_32190_10560">Phrase</a>,
 * </p>
 * <p>
 * Q<a
 * href="mdel://_9_5_1_25e60543_1217004250250_475959_0">uestion</a>,
 * <a
 * href="mdel://_9_5_1_25e60543_1217004795484_744937_17">Answer</a>
 * </p>
 * <p>
 * , <a
 * href="mdel://_9_5_1_25e60543_1217157675468_745221_189">Meaning</a>
 * </p>
 * <p>
 * or <a
 * href="mdel://_9_5_1_25e60543_1217020536500_929556_503">Term</a>.
 * </p>
 * <p>
 * It can have at least one translation. A <a
 * href="mdel://_9_5_1_25e60543_1217005029062_343297_35">Textnode</a>
 * </p>
 * <p>
 * can have <a
 * href="mdel://_9_5_1_25e60543_1217099440625_244889_1119">relations</a>
 * </p>
 * <p>
 * to several other textnodes.
 * </p>
 * <p>
 * </p>
 * </p>
 * <p>
 * </body>
 * </p>
 * <p>
 * </html>
 * </p>
 *
 */
@javax.persistence.Entity
@javax.persistence.DiscriminatorValue("T")
@javax.persistence.NamedQuery(name = "TextNode.findAll", query = "select textNode from TextNode AS textNode")
public class TextNode extends org.didicero.base.entity.TextBase implements java.io.Serializable {

    private static final long serialVersionUID = -3216219876560964030L;

    private java.util.Set<org.didicero.base.entity.Relation> targetRelations = new java.util.TreeSet<org.didicero.base.entity.Relation>();

    private java.util.Set<org.didicero.base.entity.Relation> sourceRelations = new java.util.TreeSet<org.didicero.base.entity.Relation>();

    /**
     * Default empty constructor
     */
    public TextNode() {
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this class to the values provided by 
     * the parameters.
     *
     * @param text Value for the text property
     * @param lang Value for the lang property
     * @param guid Value for the guid property
     */
    public TextNode(java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) {
        setText(text);
        setLang(lang);
        setGuid(guid);
    }

    /**
     * Constructor with all POJO attribute values and CMR relations.
     *
     * @param text Value for the text property
     * @param lang Value for the lang property
     * @param guid Value for the guid property
     * @param targetRelations Value for the targetRelations relation
     * @param sourceRelations Value for the sourceRelations relation
     */
    public TextNode(java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid, java.util.Set<org.didicero.base.entity.Relation> targetRelations, java.util.Set<org.didicero.base.entity.Relation> sourceRelations) {
        setText(text);
        setLang(lang);
        setGuid(guid);
        setTargetRelations(targetRelations);
        setSourceRelations(sourceRelations);
    }

    /**
     * Cloneable implementation with all POJO attribute values and CMR relations.
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        TextNode aClone = new TextNode(this.getText(), this.getLang(), this.getGuid());
        aClone.setId(this.getId());
        if (this.isCascadeClone()) {
            for (org.didicero.base.entity.Relation element : this.getTargetRelations()) {
                aClone.getTargetRelations().add((org.didicero.base.entity.Relation) element.clone());
            }
            for (org.didicero.base.entity.Relation element : this.getSourceRelations()) {
                aClone.getSourceRelations().add((org.didicero.base.entity.Relation) element.clone());
            }
        }
        return aClone;
    }

    /**
     * Get the targetRelations Collection
     *
     * @return java.util.Set<org.didicero.base.entity.Relation>
     */
    @javax.persistence.OneToMany(mappedBy = "targetTextnode")
    public java.util.Set<org.didicero.base.entity.Relation> getTargetRelations() {
        return this.targetRelations;
    }

    /**
     * Set the targetRelations
     *
     * @param targetRelations
     */
    public void setTargetRelations(java.util.Set<org.didicero.base.entity.Relation> targetRelations) {
        this.targetRelations = targetRelations;
    }

    /**
     * Get the sourceRelations Collection
     *
     * @return java.util.Set<org.didicero.base.entity.Relation>
     */
    @javax.persistence.OneToMany(mappedBy = "sourceTextnode")
    public java.util.Set<org.didicero.base.entity.Relation> getSourceRelations() {
        return this.sourceRelations;
    }

    /**
     * Set the sourceRelations
     *
     * @param sourceRelations
     */
    public void setSourceRelations(java.util.Set<org.didicero.base.entity.Relation> sourceRelations) {
        this.sourceRelations = sourceRelations;
    }

    /**
     * Indicates if the argument is of the same type and all values are equal.
     *
     * @param object The target object to compare with
     * @return boolean True if both objects a 'equal'
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TextNode)) {
            return false;
        }
        final TextNode that = (TextNode) object;
        if (this.getId() == null || that.getId() == null || !this.getId().equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object
     *
     * @return int The hash code value
     */
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (getId() == null ? 0 : getId().hashCode());
        return hashCode;
    }

    /**
     * Returns a String representation of the object
     *
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     */
    public String toString() {
        return super.toString();
    }
}
