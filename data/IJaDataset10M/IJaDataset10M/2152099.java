package ejb.bprocess.reports;

import javax.ejb.*;

/**
 * Created Jul 28, 2005 3:41:08 PM
 * Code generated by the Sun ONE Studio EJB Builder
 * @author Administrator
 */
public interface DetailedCatalogueTransactionReportSession extends javax.ejb.EJBObject {

    public java.lang.String generateReport(java.lang.String libId, java.lang.String fdate, java.lang.String tdate) throws java.rmi.RemoteException;
}
