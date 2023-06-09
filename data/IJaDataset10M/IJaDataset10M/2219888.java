package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jhotdraw.app.Application;
import org.jhotdraw.app.Project;

/**
 * ProjectPropertyAction.
 * 
 * @author Werner Randelshofer.
 * @version 1.0 June 18, 2006 Created.
 */
public class ProjectPropertyAction extends AbstractProjectAction {

    private String propertyName;

    private Class[] parameterClass;

    private Object propertyValue;

    private String setterName;

    private String getterName;

    private PropertyChangeListener projectListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == propertyName) {
                updateSelectedState();
            }
        }
    };

    /** Creates a new instance. */
    public ProjectPropertyAction(Application app, String propertyName, Object propertyValue) {
        this(app, propertyName, propertyValue.getClass(), propertyValue);
    }

    public ProjectPropertyAction(Application app, String propertyName, Class propertyClass, Object propertyValue) {
        super(app);
        this.propertyName = propertyName;
        this.parameterClass = new Class[] { propertyClass };
        this.propertyValue = propertyValue;
        setterName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        getterName = ((propertyClass == Boolean.TYPE || propertyClass == Boolean.class) ? "is" : "get") + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        updateSelectedState();
    }

    public void actionPerformed(ActionEvent evt) {
        Project p = getCurrentProject();
        try {
            p.getClass().getMethod(setterName, parameterClass).invoke(p, new Object[] { propertyValue });
        } catch (Throwable e) {
            InternalError error = new InternalError("Method invocation failed");
            error.initCause(e);
            throw error;
        }
    }

    protected void installProjectListeners(Project p) {
        super.installProjectListeners(p);
        p.addPropertyChangeListener(projectListener);
        updateSelectedState();
    }

    /**
     * Installs listeners on the project object.
     */
    protected void uninstallProjectListeners(Project p) {
        super.uninstallProjectListeners(p);
        p.removePropertyChangeListener(projectListener);
    }

    private void updateSelectedState() {
        boolean isSelected = false;
        Project p = getCurrentProject();
        if (p != null) {
            try {
                Object value = p.getClass().getMethod(getterName, (Class[]) null).invoke(p);
                isSelected = value == propertyValue || value != null && propertyValue != null && value.equals(propertyValue);
            } catch (Throwable e) {
                InternalError error = new InternalError("Method invocation failed");
                error.initCause(e);
                throw error;
            }
        }
        putValue(Actions.SELECTED_KEY, isSelected);
    }
}
