package jp.eisbahn.eclipse.plugins.searchclipse.internal;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SiteListLabelProvidor extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        SiteConfig config = (SiteConfig) element;
        switch(columnIndex) {
            case 0:
                return config.getSiteName();
            case 1:
                return config.getUrlTemplate();
            case 2:
                return config.getEncodingName();
            default:
                return null;
        }
    }
}