package ejb.bprocess.queries;

import javax.ejb.*;

/**
 * Created Jun 17, 2004 3:00:35 PM
 * Code generated by the Sun ONE Studio EJB Builder
 * @author Administrator
 */
public class SearchbyRequesterSessionBean implements javax.ejb.SessionBean {

    private javax.ejb.SessionContext context;

    private ejb.bprocess.util.Utility utility = null;

    private ejb.bprocess.util.HomeFactory homeFactory = null;

    private ejb.bprocess.util.NewGenXMLGenerator newGenXMLGenerator = null;

    /**
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(javax.ejb.SessionContext aContext) {
        context = aContext;
        utility = ejb.bprocess.util.Utility.getInstance();
        homeFactory = ejb.bprocess.util.HomeFactory.getInstance();
        newGenXMLGenerator = ejb.bprocess.util.NewGenXMLGenerator.getInstance();
    }

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() {
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() {
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() {
    }

    /**
     * See section 7.10.3 of the EJB 2.0 specification
     */
    public void ejbCreate() {
    }

    public java.lang.String getRequesterDetails(java.lang.String xmlStr) {
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        org.jdom.Element root1 = new org.jdom.Element("Response");
        Integer libraryId = new Integer(root.getChildText("LibraryID").toString());
        String patronId = root.getChildText("PatronId");
        java.sql.Timestamp fromDate = utility.getInstance().getTimestamp(root.getChildText("FromDate"));
        java.sql.Timestamp toDate = utility.getInstance().getTimestamp(root.getChildText("ToDate"));
        try {
            java.util.Vector vector = new java.util.Vector();
            vector = ((ejb.objectmodel.acquisitions.LocalACQ_REQUESTHome) homeFactory.getInstance().getHome("ACQ_REQUEST")).getRequestOrderNoDetails(libraryId, patronId, fromDate, toDate);
            for (int i = 0; i < vector.size(); i += 5) {
                org.jdom.Element orderDetails = new org.jdom.Element("OrderNoDetails");
                org.jdom.Element requestId = new org.jdom.Element("RequestId");
                requestId.setText(utility.getTestedString(vector.elementAt(i)));
                orderDetails.addContent(requestId);
                org.jdom.Element orderno = new org.jdom.Element("OrderNo");
                orderno.setText(utility.getTestedString(vector.elementAt(i + 1).toString()));
                orderDetails.addContent(orderno);
                org.jdom.Element orderDate = new org.jdom.Element("OrderDate");
                java.sql.Timestamp orderdate = (java.sql.Timestamp) vector.elementAt(i + 2);
                orderDate.setText(String.valueOf(orderdate.getTime()));
                orderDetails.addContent(orderDate);
                org.jdom.Element dueDate = new org.jdom.Element("DueDate");
                java.sql.Timestamp duedate = (java.sql.Timestamp) vector.elementAt(i + 3);
                dueDate.setText(String.valueOf(duedate.getTime()));
                orderDetails.addContent(dueDate);
                Integer vendorId = new Integer(vector.elementAt(i + 4).toString());
                System.out.println("vendorId : " + vendorId);
                String vendorName = ((ejb.bprocess.acquisitions.LocalUtilityHome) homeFactory.getHome("Utility")).create().getVendorName(libraryId, vendorId);
                org.jdom.Element vendor = new org.jdom.Element("Vendor");
                vendor.setText(utility.getTestedString(vendorName));
                orderDetails.addContent(vendor);
                root1.addContent(orderDetails);
            }
        } catch (CreateException ex) {
            System.out.println("create exception while creating utility");
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        org.jdom.Document doc = new org.jdom.Document(root1);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is in EB:" + xmlStr);
        return xmlStr;
    }

    public java.lang.String getCopiesDetails(java.lang.String xmlStr) {
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        org.jdom.Element root1 = new org.jdom.Element("Response");
        Integer libraryId = new Integer(root.getChildText("LibraryID").toString());
        Integer requestId = new Integer(root.getChildText("RequestId").toString());
        String orderno = root.getChildText("OrderNo");
        try {
            java.util.Vector vector = new java.util.Vector();
            vector = ((ejb.objectmodel.acquisitions.LocalACQ_REQUESTHome) homeFactory.getInstance().getHome("ACQ_REQUEST")).getOrderNoCopiesDetails(libraryId, requestId, orderno);
            for (int i = 0; i < vector.size(); i += 9) {
                org.jdom.Element copiesDetails = new org.jdom.Element("CopiesDetails");
                org.jdom.Element requestId1 = new org.jdom.Element("RequestId");
                requestId1.setText(utility.getTestedString(vector.elementAt(i)));
                copiesDetails.addContent(requestId1);
                org.jdom.Element title = new org.jdom.Element("Title");
                title.setText(utility.getTestedString(vector.elementAt(i + 1)));
                copiesDetails.addContent(title);
                org.jdom.Element author = new org.jdom.Element("Author");
                if (!utility.getTestedString(vector.elementAt(i + 2)).equals("")) {
                    author.setText(utility.getTestedString(vector.elementAt(i + 2)));
                }
                copiesDetails.addContent(author);
                org.jdom.Element edition = new org.jdom.Element("Edition");
                edition.setText(utility.getTestedString(vector.elementAt(i + 3)));
                copiesDetails.addContent(edition);
                org.jdom.Element publisher = new org.jdom.Element("Publisher");
                publisher.setText(utility.getTestedString(vector.elementAt(i + 4)));
                copiesDetails.addContent(publisher);
                org.jdom.Element publishYear = new org.jdom.Element("PublishYear");
                publishYear.setText(utility.getTestedString(vector.elementAt(i + 5)));
                copiesDetails.addContent(publishYear);
                Integer LibraryId = new Integer(vector.elementAt(i + 6).toString());
                org.jdom.Element library = new org.jdom.Element("Library");
                library.setText(utility.getTestedString(LibraryId));
                copiesDetails.addContent(library);
                org.jdom.Element copies = new org.jdom.Element("OrderedCopies");
                copies.setText(utility.getTestedString(vector.elementAt(i + 7)));
                copiesDetails.addContent(copies);
                org.jdom.Element receivedcopies = new org.jdom.Element("ReceivedCopies");
                receivedcopies.setText(utility.getTestedString(vector.elementAt(i + 8)));
                copiesDetails.addContent(receivedcopies);
                root1.addContent(copiesDetails);
            }
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        org.jdom.Document doc = new org.jdom.Document(root1);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is in EB:" + xmlStr);
        return xmlStr;
    }

    public java.lang.String getBudgetDetails(java.lang.String xmlStr) {
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        org.jdom.Element root1 = new org.jdom.Element("Response");
        Integer libraryId = new Integer(root.getChildText("LibraryID").toString());
        Integer requestId = new Integer(root.getChildText("RequestId").toString());
        Integer budgetLibraryId = new Integer(0);
        try {
            Object[] objLocal = new Object[0];
            objLocal = ((ejb.objectmodel.acquisitions.LocalACQ_REQUEST_BUDGET_TRANSACTIONHome) homeFactory.getInstance().getHome("ACQ_REQUEST_BUDGET_TRANSACTION")).findByBudgetLibraryIdRequestId(libraryId, requestId).toArray();
            ejb.objectmodel.acquisitions.LocalACQ_REQUEST_BUDGET_TRANSACTION local = null;
            java.util.Hashtable ht = new java.util.Hashtable();
            java.util.HashSet hashSet = new java.util.HashSet();
            java.util.ArrayList arrList = new java.util.ArrayList();
            for (int i = 0; i < objLocal.length; i++) {
                local = (ejb.objectmodel.acquisitions.LocalACQ_REQUEST_BUDGET_TRANSACTION) objLocal[i];
                budgetLibraryId = local.getBudget_Library_Id();
                Integer budgetTaid = local.getBudget_Ta_Id();
                ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION local1 = null;
                Object object[] = new Object[0];
                object = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) homeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByBudgetTransactions(budgetLibraryId, budgetTaid).toArray();
                for (int j = 0; j < object.length; j++) {
                    local1 = (ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION) object[j];
                    hashSet.add(local1.getBudget_Id());
                    arrList.add(local1.getBudget_Ta_Id());
                }
            }
            java.util.Iterator i1 = hashSet.iterator();
            Integer budgetTransid = new Integer(0);
            int budgets = hashSet.size();
            int k = 0;
            while (i1.hasNext()) {
                String budgetid = i1.next().toString();
                budgetTransid = new Integer(arrList.get(k).toString());
                ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey accBudgetKey = new ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey();
                accBudgetKey.library_Id = budgetLibraryId;
                accBudgetKey.budget_Ta_Id = budgetTransid;
                ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION local4 = null;
                local4 = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) homeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByPrimaryKey(accBudgetKey);
                org.jdom.Element element = new org.jdom.Element("BudgetDetails");
                org.jdom.Element element7 = new org.jdom.Element("BudgetLibraryId");
                element7.setText(local4.getLibrary_Id().toString());
                element.addContent(element7);
                org.jdom.Element element8 = new org.jdom.Element("BudgetTaId");
                Integer budgettaid = budgetTransid;
                element8.setText(budgettaid.toString());
                element.addContent(element8);
                String commitorexpenditure = local4.getCommitted_Or_Expenditure();
                double transactionamount = 0.0;
                org.jdom.Element element1 = new org.jdom.Element("TransactionAmount");
                if (commitorexpenditure.equals("C")) {
                    java.math.BigDecimal transamount = local4.getTa_Amt();
                    double transamt = new Double(utility.getTestedString(transamount)).doubleValue();
                    java.text.DecimalFormat form = new java.text.DecimalFormat();
                    form.setMaximumFractionDigits(2);
                    form.setGroupingUsed(false);
                    String transationamt = form.format(transamt);
                    transactionamount = new Double(transationamt).doubleValue();
                    element1.setText(utility.getTestedString(transamount));
                    element.addContent(element1);
                }
                ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey budgetlibraryKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                budgetlibraryKey.library_Id = budgetLibraryId;
                budgetlibraryKey.budget_Id = budgetid;
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET local2 = null;
                local2 = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) homeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(budgetlibraryKey);
                org.jdom.Element element2 = new org.jdom.Element("BudgetHead");
                String budgetHead = local2.getBudget_Head();
                element2.setText(utility.getTestedString(budgetHead));
                element.addContent(element2);
                org.jdom.Element element3 = new org.jdom.Element("BudgetId");
                String budgetid1 = local2.getBudget_Id();
                element3.setText(utility.getTestedString(budgetid1));
                element.addContent(element3);
                Integer budgetSourceId = local2.getBudget_Source_Id();
                ejb.objectmodel.administration.ACC_BUDGET_SOURCEKey sourceKey = new ejb.objectmodel.administration.ACC_BUDGET_SOURCEKey();
                sourceKey.library_Id = budgetLibraryId;
                sourceKey.budget_Source_Id = budgetSourceId;
                ejb.objectmodel.administration.LocalACC_BUDGET_SOURCE localSource = null;
                localSource = ((ejb.objectmodel.administration.LocalACC_BUDGET_SOURCEHome) homeFactory.getInstance().getHome("ACC_BUDGET_SOURCE")).findByPrimaryKey(sourceKey);
                org.jdom.Element budgetSource = new org.jdom.Element("BudgetSource");
                budgetSource.setText(localSource.getSource_Name());
                element.addContent(budgetSource);
                org.jdom.Element element4 = new org.jdom.Element("BalanceAmount");
                java.math.BigDecimal price = local2.getBalance_Amt();
                element4.setText(utility.getTestedString(price));
                element.addContent(element4);
                org.jdom.Element element5 = new org.jdom.Element("CommittedAmount");
                java.math.BigDecimal committedamount = local2.getCommitted_Amt();
                element5.setText(utility.getTestedString(committedamount));
                element.addContent(element5);
                org.jdom.Element element6 = new org.jdom.Element("ExpenditureAmount");
                java.math.BigDecimal expenditureamount = local2.getExpenditure_Amt();
                element6.setText(utility.getTestedString(expenditureamount));
                element.addContent(element6);
                root1.addContent(element);
                k = k + 1;
            }
        } catch (javax.ejb.FinderException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        org.jdom.Document doc = new org.jdom.Document(root1);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("This is in EB:" + xmlStr);
        return xmlStr;
    }
}