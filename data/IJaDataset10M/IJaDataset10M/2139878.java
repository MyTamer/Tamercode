package com.familywebscape.ootpj.db.auto;

/** Class _TypeHitter was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _TypeHitter extends org.objectstyle.cayenne.CayenneDataObject {

    public static final String TYPE_HITTER_DESC_PROPERTY = "typeHitterDesc";

    public static final String TYPE_HITTER_ID_PK_COLUMN = "TYPE_HITTER_ID";

    public void setTypeHitterDesc(String typeHitterDesc) {
        writeProperty("typeHitterDesc", typeHitterDesc);
    }

    public String getTypeHitterDesc() {
        return (String) readProperty("typeHitterDesc");
    }
}
