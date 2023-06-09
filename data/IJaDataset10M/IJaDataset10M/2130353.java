package org.simpleframework.xml.load;

/**
 * The <code>Label</code> interface is used to describe an reference to
 * a field annotated with an XML schema annotation. Because each field
 * and annotation is different, there are different ways in which the
 * annotation can be accessed. This provides a uniform means for 
 * accessing the field annotation details and the field properties.
 * <p>
 * This also exposes a <code>Converter</code> object, which is used to
 * convert an XML node into a property that can be assigned to the 
 * annotated field. Each converter returned is specific to the label
 * and knows, based on the annotation, how to serialize the field.
 * 
 * @author Niall Gallagher
 */
interface Label {

    /**
    * This method returns a <code>Converter</code> which can be used to
    * convert an XML node into an object value and vice versa. The 
    * converter requires only the source object in order to perform
    * serialization or deserialization of the provided XML node.
    * 
    * @param root this is the source object for the serialization
    * 
    * @return this returns an object that is used for conversion
    */
    public Converter getConverter(Source root) throws Exception;

    /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @return returns the name that is used for the XML property
    */
    public String getName() throws Exception;

    /**
    * This returns the dependant type for the annotation. This type
    * is the type other than the annotated field or method type that
    * the label depends on. For the <code>ElementList</code> and 
    * the <code>ElementArray</code> this is the component type that
    * is deserialized individually and inserted into the container. 
    * 
    * @return this is the type that the annotation depends on
    */
    public Class getDependant() throws Exception;

    /**
    * This is used to either provide the entry value provided within
    * the annotation or compute a entry value. If the entry string
    * is not provided the the entry value is calculated as the type
    * of primitive the object is as a simplified class name.
    * 
    * @return this returns the name of the XML entry element used 
    */
    public String getEntry() throws Exception;

    /**
    * This is used to provide a configured empty value used when the
    * annotated value is null. This ensures that XML can be created
    * with required details regardless of whether values are null or
    * not. It also provides a means for sensible default values.
    * 
    * @return this returns the string to use for default values
    */
    public String getEmpty();

    /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistance. All contacts 
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the field that this label is representing
    */
    public Contact getContact();

    /**
    * This acts as a convinience method used to determine the type of
    * the field this represents. This is used when an object is written
    * to XML. It determines whether a <code>class</code> attribute
    * is required within the serialized XML element, that is, if the
    * class returned by this is different from the actual value of the
    * object to be serialized then that type needs to be remembered.
    *  
    * @return this returns the type of the field class
    */
    public Class getType();

    /**
    * This is used to acquire the name of the element or attribute
    * as taken from the annotation. If the element or attribute
    * explicitly specifies a name then that name is used for the
    * XML element or attribute used. If however no overriding name
    * is provided then the method or field is used for the name. 
    * 
    * @return returns the name of the annotation for the contact
    */
    public String getOverride();

    /**
    * This is used to determine whether the annotation requires it
    * and its children to be written as a CDATA block. This is done
    * when a primitive or other such element requires a text value
    * and that value needs to be encapsulated within a CDATA block.
    * 
    * @return this returns true if the element requires CDATA
    */
    public boolean isData();

    /**
    * Determines whether the XML attribute or element is required. 
    * This ensures that if an XML element is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */
    public boolean isRequired();

    /**
    * This is used to determine whether the label represents an
    * inline XML entity. The <code>ElementList</code> annotation
    * and the <code>Text</code> annotation represent inline 
    * items. This means that they contain no containing element
    * and so can not specify overrides or special attributes.
    * 
    * @return this returns true if the annotaiton is inline
    */
    public boolean isInline();

    /**
    * This is used to describe the annotation and method or field
    * that this label represents. This is used to provide error
    * messages that can be used to debug issues that occur when
    * processing a method. This should provide enough information
    * such that the problem can be isolated correctly. 
    * 
    * @return this returns a string representation of the label
    */
    public String toString();
}
