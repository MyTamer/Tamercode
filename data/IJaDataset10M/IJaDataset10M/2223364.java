package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.KeyNamePair;

/** Generated Model for CM_Ad
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_CM_Ad extends PO implements I_CM_Ad, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_CM_Ad(Properties ctx, int CM_Ad_ID, String trxName) {
        super(ctx, CM_Ad_ID, trxName);
    }

    /** Load Constructor */
    public X_CM_Ad(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
      */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID, get_TrxName());
        return poi;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_CM_Ad[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Actual Click Count.
		@param ActualClick 
		How many clicks have been counted
	  */
    public void setActualClick(int ActualClick) {
        set_Value(COLUMNNAME_ActualClick, Integer.valueOf(ActualClick));
    }

    /** Get Actual Click Count.
		@return How many clicks have been counted
	  */
    public int getActualClick() {
        Integer ii = (Integer) get_Value(COLUMNNAME_ActualClick);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Actual Impression Count.
		@param ActualImpression 
		How many impressions have been counted
	  */
    public void setActualImpression(int ActualImpression) {
        set_Value(COLUMNNAME_ActualImpression, Integer.valueOf(ActualImpression));
    }

    /** Get Actual Impression Count.
		@return How many impressions have been counted
	  */
    public int getActualImpression() {
        Integer ii = (Integer) get_Value(COLUMNNAME_ActualImpression);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_CM_Ad_Cat getCM_Ad_Cat() throws Exception {
        Class<?> clazz = MTable.getClass(I_CM_Ad_Cat.Table_Name);
        I_CM_Ad_Cat result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_CM_Ad_Cat) constructor.newInstance(new Object[] { getCtx(), new Integer(getCM_Ad_Cat_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Advertisement Category.
		@param CM_Ad_Cat_ID 
		Advertisement Category like Banner Homepage 
	  */
    public void setCM_Ad_Cat_ID(int CM_Ad_Cat_ID) {
        if (CM_Ad_Cat_ID < 1) throw new IllegalArgumentException("CM_Ad_Cat_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_CM_Ad_Cat_ID, Integer.valueOf(CM_Ad_Cat_ID));
    }

    /** Get Advertisement Category.
		@return Advertisement Category like Banner Homepage 
	  */
    public int getCM_Ad_Cat_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_CM_Ad_Cat_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Advertisement.
		@param CM_Ad_ID 
		An Advertisement is something like a banner
	  */
    public void setCM_Ad_ID(int CM_Ad_ID) {
        if (CM_Ad_ID < 1) throw new IllegalArgumentException("CM_Ad_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_CM_Ad_ID, Integer.valueOf(CM_Ad_ID));
    }

    /** Get Advertisement.
		@return An Advertisement is something like a banner
	  */
    public int getCM_Ad_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_CM_Ad_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_CM_Media getCM_Media() throws Exception {
        Class<?> clazz = MTable.getClass(I_CM_Media.Table_Name);
        I_CM_Media result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_CM_Media) constructor.newInstance(new Object[] { getCtx(), new Integer(getCM_Media_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Media Item.
		@param CM_Media_ID 
		Contains media content like images, flash movies etc.
	  */
    public void setCM_Media_ID(int CM_Media_ID) {
        if (CM_Media_ID < 1) throw new IllegalArgumentException("CM_Media_ID is mandatory.");
        set_Value(COLUMNNAME_CM_Media_ID, Integer.valueOf(CM_Media_ID));
    }

    /** Get Media Item.
		@return Contains media content like images, flash movies etc.
	  */
    public int getCM_Media_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_CM_Media_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Content HTML.
		@param ContentHTML 
		Contains the content itself
	  */
    public void setContentHTML(String ContentHTML) {
        if (ContentHTML != null && ContentHTML.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            ContentHTML = ContentHTML.substring(0, 2000);
        }
        set_Value(COLUMNNAME_ContentHTML, ContentHTML);
    }

    /** Get Content HTML.
		@return Contains the content itself
	  */
    public String getContentHTML() {
        return (String) get_Value(COLUMNNAME_ContentHTML);
    }

    /** Set Description.
		@param Description 
		Optional short description of the record
	  */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 255);
        }
        set_Value(COLUMNNAME_Description, Description);
    }

    /** Get Description.
		@return Optional short description of the record
	  */
    public String getDescription() {
        return (String) get_Value(COLUMNNAME_Description);
    }

    /** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
    public void setEndDate(Timestamp EndDate) {
        set_Value(COLUMNNAME_EndDate, EndDate);
    }

    /** Get End Date.
		@return Last effective date (inclusive)
	  */
    public Timestamp getEndDate() {
        return (Timestamp) get_Value(COLUMNNAME_EndDate);
    }

    /** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
    public void setHelp(String Help) {
        if (Help != null && Help.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            Help = Help.substring(0, 2000);
        }
        set_Value(COLUMNNAME_Help, Help);
    }

    /** Get Comment/Help.
		@return Comment or Hint
	  */
    public String getHelp() {
        return (String) get_Value(COLUMNNAME_Help);
    }

    /** Set Special AD Flag.
		@param IsAdFlag 
		Do we need to specially mention this ad?
	  */
    public void setIsAdFlag(boolean IsAdFlag) {
        set_Value(COLUMNNAME_IsAdFlag, Boolean.valueOf(IsAdFlag));
    }

    /** Get Special AD Flag.
		@return Do we need to specially mention this ad?
	  */
    public boolean isAdFlag() {
        Object oo = get_Value(COLUMNNAME_IsAdFlag);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Logging.
		@param IsLogged 
		Do we need to log the banner impressions and clicks? (needs much performance)
	  */
    public void setIsLogged(boolean IsLogged) {
        set_Value(COLUMNNAME_IsLogged, Boolean.valueOf(IsLogged));
    }

    /** Get Logging.
		@return Do we need to log the banner impressions and clicks? (needs much performance)
	  */
    public boolean isLogged() {
        Object oo = get_Value(COLUMNNAME_IsLogged);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Max Click Count.
		@param MaxClick 
		Maximum Click Count until banner is deactivated
	  */
    public void setMaxClick(int MaxClick) {
        set_Value(COLUMNNAME_MaxClick, Integer.valueOf(MaxClick));
    }

    /** Get Max Click Count.
		@return Maximum Click Count until banner is deactivated
	  */
    public int getMaxClick() {
        Integer ii = (Integer) get_Value(COLUMNNAME_MaxClick);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Max Impression Count.
		@param MaxImpression 
		Maximum Impression Count until banner is deactivated
	  */
    public void setMaxImpression(int MaxImpression) {
        set_Value(COLUMNNAME_MaxImpression, Integer.valueOf(MaxImpression));
    }

    /** Get Max Impression Count.
		@return Maximum Impression Count until banner is deactivated
	  */
    public int getMaxImpression() {
        Integer ii = (Integer) get_Value(COLUMNNAME_MaxImpression);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
    public void setName(String Name) {
        if (Name == null) throw new IllegalArgumentException("Name is mandatory.");
        if (Name.length() > 120) {
            log.warning("Length > 120 - truncated");
            Name = Name.substring(0, 120);
        }
        set_Value(COLUMNNAME_Name, Name);
    }

    /** Get Name.
		@return Alphanumeric identifier of the entity
	  */
    public String getName() {
        return (String) get_Value(COLUMNNAME_Name);
    }

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), getName());
    }

    /** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
    public void setStartDate(Timestamp StartDate) {
        if (StartDate == null) throw new IllegalArgumentException("StartDate is mandatory.");
        set_Value(COLUMNNAME_StartDate, StartDate);
    }

    /** Get Start Date.
		@return First effective day (inclusive)
	  */
    public Timestamp getStartDate() {
        return (Timestamp) get_Value(COLUMNNAME_StartDate);
    }

    /** Set Start Count Impression.
		@param StartImpression 
		For rotation we need a start count
	  */
    public void setStartImpression(int StartImpression) {
        set_Value(COLUMNNAME_StartImpression, Integer.valueOf(StartImpression));
    }

    /** Get Start Count Impression.
		@return For rotation we need a start count
	  */
    public int getStartImpression() {
        Integer ii = (Integer) get_Value(COLUMNNAME_StartImpression);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Target URL.
		@param TargetURL 
		URL for the Target
	  */
    public void setTargetURL(String TargetURL) {
        if (TargetURL != null && TargetURL.length() > 120) {
            log.warning("Length > 120 - truncated");
            TargetURL = TargetURL.substring(0, 120);
        }
        set_Value(COLUMNNAME_TargetURL, TargetURL);
    }

    /** Get Target URL.
		@return URL for the Target
	  */
    public String getTargetURL() {
        return (String) get_Value(COLUMNNAME_TargetURL);
    }

    /** Set Target Frame.
		@param Target_Frame 
		Which target should be used if user clicks?
	  */
    public void setTarget_Frame(String Target_Frame) {
        if (Target_Frame == null) throw new IllegalArgumentException("Target_Frame is mandatory.");
        if (Target_Frame.length() > 20) {
            log.warning("Length > 20 - truncated");
            Target_Frame = Target_Frame.substring(0, 20);
        }
        set_Value(COLUMNNAME_Target_Frame, Target_Frame);
    }

    /** Get Target Frame.
		@return Which target should be used if user clicks?
	  */
    public String getTarget_Frame() {
        return (String) get_Value(COLUMNNAME_Target_Frame);
    }
}
