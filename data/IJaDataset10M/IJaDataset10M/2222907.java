package ejb.objectmodel.cataloguing.authorityFiles;

import javax.ejb.*;

/**
 * Created Aug 1, 2005 2:27:57 PM
 * Code generated by the Sun ONE Studio EJB Builder
 * @author Administrator
 */
public interface LocalMeeting_Name_AF_SeeAlso extends javax.ejb.EJBLocalObject {

    public abstract java.lang.Integer getMeeting_Name_Id();

    public abstract java.lang.Integer getLibrary_Id();

    public abstract java.lang.Integer getR_Meeting_Name_Id();

    public abstract java.lang.Integer getR_Library_Id();

    public abstract java.lang.String getType_Of_Relation();

    public abstract void setType_Of_Relation(java.lang.String type_Of_Relation);
}