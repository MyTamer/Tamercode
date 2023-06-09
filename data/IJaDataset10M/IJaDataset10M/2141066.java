package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_CommissionRun
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_C_CommissionRun {

    /** TableName=C_CommissionRun */
    public static final String Table_Name = "C_CommissionRun";

    /** AD_Table_ID=436 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 1 - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(1);

    /** Column name C_CommissionRun_ID */
    public static final String COLUMNNAME_C_CommissionRun_ID = "C_CommissionRun_ID";

    /** Set Commission Run.
	  * Commission Run or Process
	  */
    public void setC_CommissionRun_ID(int C_CommissionRun_ID);

    /** Get Commission Run.
	  * Commission Run or Process
	  */
    public int getC_CommissionRun_ID();

    /** Column name C_Commission_ID */
    public static final String COLUMNNAME_C_Commission_ID = "C_Commission_ID";

    /** Set Commission.
	  * Commission
	  */
    public void setC_Commission_ID(int C_Commission_ID);

    /** Get Commission.
	  * Commission
	  */
    public int getC_Commission_ID();

    public I_C_Commission getC_Commission() throws Exception;

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

    /** Set Description.
	  * Optional short description of the record
	  */
    public void setDescription(String Description);

    /** Get Description.
	  * Optional short description of the record
	  */
    public String getDescription();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

    /** Set Document No.
	  * Document sequence number of the document
	  */
    public void setDocumentNo(String DocumentNo);

    /** Get Document No.
	  * Document sequence number of the document
	  */
    public String getDocumentNo();

    /** Column name GrandTotal */
    public static final String COLUMNNAME_GrandTotal = "GrandTotal";

    /** Set Grand Total.
	  * Total amount of document
	  */
    public void setGrandTotal(BigDecimal GrandTotal);

    /** Get Grand Total.
	  * Total amount of document
	  */
    public BigDecimal getGrandTotal();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

    /** Set Processed.
	  * The document has been processed
	  */
    public void setProcessed(boolean Processed);

    /** Get Processed.
	  * The document has been processed
	  */
    public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

    /** Set Process Now	  */
    public void setProcessing(boolean Processing);

    /** Get Process Now	  */
    public boolean isProcessing();

    /** Column name StartDate */
    public static final String COLUMNNAME_StartDate = "StartDate";

    /** Set Start Date.
	  * First effective day (inclusive)
	  */
    public void setStartDate(Timestamp StartDate);

    /** Get Start Date.
	  * First effective day (inclusive)
	  */
    public Timestamp getStartDate();
}
