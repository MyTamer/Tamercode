package org.adempiere.pipo.handler;

import java.util.Properties;
import javax.xml.transform.sax.TransformerHandler;
import org.adempiere.pipo.AbstractElementHandler;
import org.adempiere.pipo.Element;
import org.adempiere.pipo.exception.POSaveFailedException;
import org.compiere.model.MTask;
import org.compiere.model.X_AD_Task;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TaskElementHandler extends AbstractElementHandler {

    public void startElement(Properties ctx, Element element) throws SAXException {
        String elementValue = element.getElementValue();
        Attributes atts = element.attributes;
        log.info(elementValue + " " + atts.getValue("ADTaskNameID"));
        String entitytype = atts.getValue("EntityType");
        if (isProcessElement(ctx, entitytype)) {
            String name = atts.getValue("ADTaskNameID");
            int id = get_ID(ctx, "AD_Task", name);
            MTask m_Task = new MTask(ctx, id, getTrxName(ctx));
            int AD_Backup_ID = -1;
            String Object_Status = null;
            if (id > 0) {
                AD_Backup_ID = copyRecord(ctx, "AD_Task", m_Task);
                Object_Status = "Update";
            } else {
                Object_Status = "New";
                AD_Backup_ID = 0;
            }
            m_Task.setAccessLevel(atts.getValue("AccessLevel"));
            m_Task.setDescription(getStringValue(atts, "Description"));
            m_Task.setEntityType(atts.getValue("EntityType"));
            m_Task.setHelp(getStringValue(atts, "Help"));
            m_Task.setIsActive(atts.getValue("isActive") != null ? Boolean.valueOf(atts.getValue("isActive")).booleanValue() : true);
            m_Task.setName(name);
            m_Task.setOS_Command(getStringValue(atts, "OS_Command"));
            if (m_Task.save(getTrxName(ctx)) == true) {
                record_log(ctx, 1, m_Task.getName(), "Task", m_Task.get_ID(), AD_Backup_ID, Object_Status, "AD_Task", get_IDWithColumn(ctx, "AD_Table", "TableName", "AD_Task"));
            } else {
                record_log(ctx, 0, m_Task.getName(), "Task", m_Task.get_ID(), AD_Backup_ID, Object_Status, "AD_Task", get_IDWithColumn(ctx, "AD_Table", "TableName", "AD_Task"));
                throw new POSaveFailedException("Task");
            }
        } else {
            element.skip = true;
        }
    }

    public void endElement(Properties ctx, Element element) throws SAXException {
    }

    public void create(Properties ctx, TransformerHandler document) throws SAXException {
        int AD_Task_ID = Env.getContextAsInt(ctx, "AD_Task_ID");
        X_AD_Task m_Task = new X_AD_Task(ctx, AD_Task_ID, null);
        AttributesImpl atts = new AttributesImpl();
        createTaskBinding(atts, m_Task);
        document.startElement("", "", "task", atts);
        document.endElement("", "", "task");
    }

    private static AttributesImpl createTaskBinding(AttributesImpl atts, X_AD_Task m_Task) {
        String sql = null;
        String name = null;
        atts.clear();
        if (m_Task.getAD_Task_ID() > 0) {
            sql = "SELECT Name FROM AD_Task WHERE AD_Task_ID=?";
            name = DB.getSQLValueString(null, sql, m_Task.getAD_Task_ID());
            if (name != null) atts.addAttribute("", "", "ADTaskNameID", "CDATA", name); else atts.addAttribute("", "", "ADTaskNameID", "CDATA", "");
        } else {
            atts.addAttribute("", "", "ADTaskNameID", "CDATA", "");
        }
        atts.addAttribute("", "", "AccessLevel", "CDATA", (m_Task.getAccessLevel() != null ? m_Task.getAccessLevel() : ""));
        atts.addAttribute("", "", "Description", "CDATA", (m_Task.getDescription() != null ? m_Task.getDescription() : ""));
        atts.addAttribute("", "", "EntityType", "CDATA", (m_Task.getEntityType() != null ? m_Task.getEntityType() : ""));
        atts.addAttribute("", "", "Help", "CDATA", (m_Task.getHelp() != null ? m_Task.getHelp() : ""));
        atts.addAttribute("", "", "isActive", "CDATA", (m_Task.isActive() == true ? "true" : "false"));
        atts.addAttribute("", "", "Name", "CDATA", (m_Task.getName() != null ? m_Task.getName() : ""));
        atts.addAttribute("", "", "OS_Command", "CDATA", (m_Task.getOS_Command() != null ? m_Task.getOS_Command() : ""));
        return atts;
    }
}
