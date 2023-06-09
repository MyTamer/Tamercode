package de.juwimm.cms.gui.table;

import static de.juwimm.cms.common.Constants.*;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import de.juwimm.cms.vo.PictureSlimstValue;

/**
 * TableModel for list of all pictures in dialog for choosing a picture
 * 
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003, 2004, 2005</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="sascha-matthias.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id: PictureTableModel.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class PictureTableModel extends DefaultTableModel {

    private Vector<String> columnNames = new Vector<String>(5);

    public PictureTableModel() {
        super();
        columnNames.addElement(rb.getString("PanDocument.document.documentName"));
        columnNames.addElement(rb.getString("PanDocument.document.documentType"));
        columnNames.addElement(rb.getString("PanDocument.document.changedAt"));
        columnNames.addElement(rb.getString("panel.content.picture.height"));
        columnNames.addElement(rb.getString("panel.content.picture.width"));
    }

    public void addRow(PictureSlimstValue vo) {
        Object[] obj = new Object[7];
        if (vo.getPictureName() != null) {
            obj[0] = vo.getPictureName();
        } else {
            obj[0] = Integer.toString(vo.getPictureId());
        }
        obj[1] = vo.getMimeType();
        obj[2] = new Date(vo.getTimeStamp());
        obj[3] = vo.getHeight();
        obj[4] = vo.getWidth();
        obj[5] = vo.getPictureId();
        obj[6] = vo;
        addRow(obj);
    }

    public void addRow(Vector obj) {
        dataVector.addElement(obj);
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    public void addRows(PictureSlimstValue[] vos) {
        for (int i = 0; i < vos.length; i++) {
            addRow(vos[i]);
        }
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
        return dataVector.size();
    }

    public int getRowForPicture(Integer pictureId) {
        int retVal = -1;
        for (int i = 0; i < dataVector.size(); i++) {
            Object[] obj = (Object[]) dataVector.elementAt(i);
            if (((Integer) obj[5]).intValue() == pictureId.intValue()) {
                retVal = i;
                break;
            }
        }
        return retVal;
    }

    public String getColumnName(int col) {
        if (columnNames.size() >= col) {
            return (String) columnNames.elementAt(col);
        }
        return null;
    }

    public void setColumnName(int col, String name) {
        if (columnNames.size() == 0 || col >= columnNames.size()) {
            columnNames.addElement(name);
        } else {
            columnNames.setElementAt(name, col);
        }
    }

    public Object getValueAt(int row, int col) {
        try {
            return ((Object[]) dataVector.elementAt(row))[col];
        } catch (Exception ex) {
            return null;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        Object[] array = (Object[]) dataVector.elementAt(row);
        array[col] = value;
        fireTableCellUpdated(row, col);
    }

    public Class getColumnClass(int c) {
        try {
            return getValueAt(0, c).getClass();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void addRow(Object[] obj) {
        dataVector.addElement(obj);
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    public void insertRow(Object[] obj) {
        if (getRowCount() == 0) {
            dataVector.addElement(obj);
        } else {
            dataVector.insertElementAt(obj, 0);
        }
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    public void removeRow(int i) {
        dataVector.removeElementAt(i);
        this.fireTableRowsDeleted(i, i);
    }

    public Object[] getRow(int i) {
        return (Object[]) dataVector.elementAt(i);
    }
}
