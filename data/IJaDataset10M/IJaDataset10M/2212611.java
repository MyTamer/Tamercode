package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

/** Generated Interface for CM_Chat
 *  @author Trifon Trifonov (generated) 
 *  @version Release 3.3.1t
 */
public interface I_CM_Chat {

    /** TableName=CM_Chat */
    public static final String Table_Name = "CM_Chat";

    /** AD_Table_ID=876 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

    /** Set Table.
	  * Database Table information
	  */
    public void setAD_Table_ID(int AD_Table_ID);

    /** Get Table.
	  * Database Table information
	  */
    public int getAD_Table_ID();

    public I_AD_Table getAD_Table() throws Exception;

    /** Column name CM_ChatType_ID */
    public static final String COLUMNNAME_CM_ChatType_ID = "CM_ChatType_ID";

    /** Set Chat Type.
	  * Type of discussion / chat
	  */
    public void setCM_ChatType_ID(int CM_ChatType_ID);

    /** Get Chat Type.
	  * Type of discussion / chat
	  */
    public int getCM_ChatType_ID();

    public I_CM_ChatType getCM_ChatType() throws Exception;

    /** Column name CM_Chat_ID */
    public static final String COLUMNNAME_CM_Chat_ID = "CM_Chat_ID";

    /** Set Chat.
	  * Chat or discussion thread
	  */
    public void setCM_Chat_ID(int CM_Chat_ID);

    /** Get Chat.
	  * Chat or discussion thread
	  */
    public int getCM_Chat_ID();

    /** Column name ConfidentialType */
    public static final String COLUMNNAME_ConfidentialType = "ConfidentialType";

    /** Set Confidentiality.
	  * Type of Confidentiality
	  */
    public void setConfidentialType(String ConfidentialType);

    /** Get Confidentiality.
	  * Type of Confidentiality
	  */
    public String getConfidentialType();

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

    /** Column name ModerationType */
    public static final String COLUMNNAME_ModerationType = "ModerationType";

    /** Set Moderation Type.
	  * Type of moderation
	  */
    public void setModerationType(String ModerationType);

    /** Get Moderation Type.
	  * Type of moderation
	  */
    public String getModerationType();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

    /** Set Record ID.
	  * Direct internal record ID
	  */
    public void setRecord_ID(int Record_ID);

    /** Get Record ID.
	  * Direct internal record ID
	  */
    public int getRecord_ID();
}
