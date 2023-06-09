package org.j2eespider.templateeditor.velocityeditor.dialogs;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.j2eespider.templateeditor.velocityeditor.editors.ContextValue;
import org.j2eespider.templateeditor.velocityeditor.manager.ConfigurationManager;

/**
 * This class is based on project VelocityWebEdit with author permission.
 * http://velocitywebedit.sourceforge.net
 * 
 * @author joe.hudson, bruno.braga
 */
public class ContextValueDialog extends Dialog {

    private IResource resource;

    private ContextValue contextValue;

    private Text keyText;

    private Text valueText;

    private Text singleValueText;

    private Label singleLabel;

    private Button singleBrowse;

    public ContextValueDialog(Shell parentShell, ContextValue contextValue, IResource resource) {
        super(parentShell);
        this.resource = resource;
        this.contextValue = contextValue;
    }

    /**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
    protected void configureShell(Shell newShell) {
        newShell.setText("Context Object Configuration");
        super.configureShell(newShell);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout(3, false));
        Label label = new Label(composite, SWT.NULL);
        label.setText("Reference Name:");
        keyText = new Text(composite, SWT.BORDER);
        if (null != contextValue) {
            keyText.setText(contextValue.name);
            keyText.setEnabled(false);
        }
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 200;
        keyText.setLayoutData(gd);
        label = new Label(composite, SWT.NULL);
        label.setText("Object Class:");
        valueText = new Text(composite, SWT.BORDER);
        valueText.setEnabled(false);
        valueText.setBackground(new Color(null, 255, 255, 255));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = 250;
        valueText.setLayoutData(gd);
        if (null != contextValue && null != contextValue.objClass) valueText.setText(contextValue.objClass.getName());
        Button browse = new Button(composite, 8);
        browse.setText("Browse");
        browse.addMouseListener(new MouseListener() {

            public void mouseDown(MouseEvent e) {
                try {
                    IJavaProject javaProject = JavaCore.create(resource.getProject());
                    if (javaProject != null) {
                        org.eclipse.jdt.core.search.IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { javaProject });
                        SelectionDialog sd = JavaUI.createTypeDialog(getShell(), new ApplicationWindow(getShell()), searchScope, 2, false);
                        sd.open();
                        Object objects[] = sd.getResult();
                        if (objects != null && objects.length > 0) {
                            IType type = (IType) objects[0];
                            String fullyQualifiedName = type.getFullyQualifiedName('.');
                            valueText.setText(type.getFullyQualifiedName());
                            String[] interfaces = type.getSuperInterfaceNames();
                            boolean isList = false;
                            if ("java.lang.Object".equals(fullyQualifiedName)) isList = true; else {
                                for (int i = 0; i < interfaces.length; i++) {
                                    if (interfaces[i].equals("java.util.Collection") || interfaces[i].equals("java.util.List") || interfaces[i].equals("java.util.Set")) {
                                        isList = true;
                                        break;
                                    }
                                }
                            }
                            if (isList) {
                                singleBrowse.setEnabled(true);
                                singleLabel.setEnabled(true);
                                singleValueText.setEnabled(true);
                            } else {
                                singleBrowse.setEnabled(false);
                                singleLabel.setEnabled(false);
                                singleValueText.setEnabled(false);
                                singleValueText.setText("");
                            }
                        }
                    } else {
                        MessageDialog.openError(getShell(), "Java Project Error", "The project must be a java project.");
                    }
                } catch (JavaModelException _ex) {
                }
            }

            public void mouseDoubleClick(MouseEvent mouseevent) {
            }

            public void mouseUp(MouseEvent mouseevent) {
            }
        });
        boolean enabled = false;
        if (null != contextValue && null != contextValue.singularClass) enabled = true;
        singleLabel = new Label(composite, SWT.NULL);
        singleLabel.setEnabled(enabled);
        singleLabel.setText("Item Class:");
        singleValueText = new Text(composite, SWT.BORDER);
        singleValueText.setEnabled(enabled);
        singleValueText.setBackground(new Color(null, 255, 255, 255));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = 250;
        singleValueText.setLayoutData(gd);
        if (enabled) singleValueText.setText(contextValue.singularClass.getName());
        singleBrowse = new Button(composite, 8);
        singleBrowse.setEnabled(enabled);
        singleBrowse.setText("Browse");
        singleBrowse.addMouseListener(new MouseListener() {

            public void mouseDown(MouseEvent e) {
                try {
                    IJavaProject javaProject = JavaCore.create(resource.getProject());
                    if (javaProject != null) {
                        org.eclipse.jdt.core.search.IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { javaProject });
                        SelectionDialog sd = JavaUI.createTypeDialog(getShell(), new ApplicationWindow(getShell()), searchScope, 2, false);
                        sd.open();
                        Object objects[] = sd.getResult();
                        if (objects != null && objects.length > 0) {
                            IType type = (IType) objects[0];
                            singleValueText.setText(type.getFullyQualifiedName());
                        }
                    } else {
                        MessageDialog.openError(getShell(), "Java Project Error", "The project must be a java project.");
                    }
                } catch (JavaModelException _ex) {
                }
            }

            public void mouseDoubleClick(MouseEvent mouseevent) {
            }

            public void mouseUp(MouseEvent mouseevent) {
            }
        });
        return parent;
    }

    protected void okPressed() {
        try {
            String name = keyText.getText().trim();
            while (name.startsWith("$")) name = name.substring(1, name.length());
            if (name.length() == 0) MessageDialog.openError(getShell(), "Error", "You must choose the reference name");
            String className = valueText.getText().trim();
            if (className.length() == 0) MessageDialog.openError(getShell(), "Error", "You must choose the class name");
            String singularClassName = singleValueText.getText().trim();
            Class singularClass = null;
            if (null != singularClassName && singularClassName.trim().length() > 0) singularClass = ConfigurationManager.getInstance(resource.getProject()).getClass(singularClassName);
            if (className.length() == 0) singularClassName = null;
            contextValue = new ContextValue(name, ConfigurationManager.getInstance(resource.getProject()).getClass(className), singularClass);
            ConfigurationManager.getInstance(resource.getProject()).addContextValue(contextValue, resource);
        } catch (Exception e) {
            MessageDialog.openError(getShell(), "Error", e.getMessage());
        }
        super.okPressed();
    }
}
