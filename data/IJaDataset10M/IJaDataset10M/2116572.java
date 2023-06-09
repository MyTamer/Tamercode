package org.eclipsetrader.ui.internal.securities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipsetrader.core.feed.FeedProperties;

public class FeedPropertiesControl {

    private Composite container;

    private TreeViewer viewer;

    private FeedProperties properties;

    public class PropertiesLabelProvider extends LabelProvider implements ITableLabelProvider, IFontProvider {

        private Font containerFont;

        public PropertiesLabelProvider() {
            FontData[] fontData = Display.getDefault().getSystemFont().getFontData();
            if (fontData != null && fontData.length != 0) containerFont = new Font(Display.getDefault(), fontData[0].getName(), fontData[0].getHeight(), SWT.BOLD);
        }

        @Override
        public void dispose() {
            if (containerFont != null) containerFont.dispose();
            super.dispose();
        }

        @Override
        public String getText(Object element) {
            return getColumnText(element, 0);
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return element.toString();
                case 1:
                    {
                        if (element instanceof PropertyElement) {
                            String value = ((PropertyElement) element).getValue();
                            return value != null ? value : "<default>";
                        }
                        break;
                    }
            }
            return "";
        }

        public Font getFont(Object element) {
            if (element instanceof PropertyElementContainer) return containerFont;
            return null;
        }
    }

    public class PropertiesContentProvider implements ITreeContentProvider {

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof PropertyElementContainer) return ((PropertyElementContainer) parentElement).getChilds();
            return null;
        }

        public Object getParent(Object element) {
            if (element instanceof PropertyElement) return ((PropertyElement) element).getParent();
            return null;
        }

        public boolean hasChildren(Object element) {
            return element instanceof PropertyElementContainer;
        }

        public Object[] getElements(Object inputElement) {
            return (Object[]) inputElement;
        }
    }

    public FeedPropertiesControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        TreeColumnLayout tableLayout = new TreeColumnLayout();
        container.setLayout(tableLayout);
        viewer = new TreeViewer(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        TreeColumn treeColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
        treeColumn.setText("Property");
        tableLayout.setColumnData(treeColumn, new ColumnWeightData(35));
        treeColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
        treeColumn.setText("Value");
        tableLayout.setColumnData(treeColumn, new ColumnWeightData(65));
        viewer.setLabelProvider(new PropertiesLabelProvider());
        viewer.setContentProvider(new PropertiesContentProvider());
        viewer.setSorter(new ViewerSorter());
        viewer.setColumnProperties(new String[] { null, "value" });
        viewer.setCellEditors(new CellEditor[] { null, new TextCellEditor(viewer.getTree()) });
        viewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return (element instanceof PropertyElement) && "value".equals(property);
            }

            public Object getValue(Object element, String property) {
                if ((element instanceof PropertyElement) && "value".equals(property)) return ((PropertyElement) element).getValue() != null ? ((PropertyElement) element).getValue() : "";
                return null;
            }

            public void modify(Object object, String property, Object value) {
                PropertyElement element = (PropertyElement) (object instanceof TreeItem ? ((TreeItem) object).getData() : object);
                element.setValue("".equals(value) ? null : value.toString());
                viewer.update(element, new String[] { property });
            }
        });
        viewer.setInput(buildInput());
        viewer.expandAll();
    }

    public Control getControl() {
        return container;
    }

    public Tree getTree() {
        return viewer.getTree();
    }

    public Object getLayoutData() {
        return container.getLayoutData();
    }

    public void setLayoutData(Object layoutData) {
        container.setLayoutData(layoutData);
    }

    public class PropertyElementContainer {

        private String name;

        private List<PropertyElement> childs = new ArrayList<PropertyElement>();

        public PropertyElementContainer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public PropertyElement[] getChilds() {
            return childs.toArray(new PropertyElement[childs.size()]);
        }

        public PropertyElement createChild(String id, String name) {
            PropertyElement element = new PropertyElement(id, name, this);
            childs.add(element);
            return element;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public class PropertyElement {

        private String id;

        private String name;

        private String value;

        private PropertyElementContainer parent;

        public PropertyElement(String id, String name, PropertyElementContainer parent) {
            this.id = id;
            this.name = name;
            this.parent = parent;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public PropertyElementContainer getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected PropertyElementContainer[] buildInput() {
        Map<String, PropertyElementContainer> containers = new HashMap<String, PropertyElementContainer>();
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint("org.eclipsetrader.core.connectors");
        if (extensionPoint != null) {
            IConfigurationElement[] configElements = extensionPoint.getConfigurationElements();
            for (int j = 0; j < configElements.length; j++) {
                String name = configElements[j].getDeclaringExtension().getLabel();
                if (name == null || name.equals("")) name = configElements[j].getDeclaringExtension().getContributor().getName();
                if ("property".equals(configElements[j].getName())) {
                    PropertyElementContainer container = containers.get(name);
                    if (container == null) {
                        container = new PropertyElementContainer(name);
                        containers.put(name, container);
                    }
                    container.createChild(configElements[j].getAttribute("id"), configElements[j].getAttribute("name"));
                }
            }
        }
        return containers.values().toArray(new PropertyElementContainer[containers.values().size()]);
    }

    public void setProperties(FeedProperties properties) {
        this.properties = properties;
        PropertyElementContainer[] input = (PropertyElementContainer[]) viewer.getInput();
        for (PropertyElementContainer container : input) {
            for (PropertyElement element : container.getChilds()) element.setValue(properties != null ? properties.getProperty(element.getId()) : null);
        }
        viewer.refresh();
    }

    public FeedProperties getProperties() {
        if (properties == null) properties = new FeedProperties();
        if (properties != null) {
            PropertyElementContainer[] input = (PropertyElementContainer[]) viewer.getInput();
            for (PropertyElementContainer container : input) {
                for (PropertyElement element : container.getChilds()) properties.setProperty(element.getId(), element.getValue());
            }
        }
        return properties.getPropertyIDs().length != 0 ? properties : null;
    }
}
