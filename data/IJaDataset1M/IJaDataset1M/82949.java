package org.yawlfoundation.yawl.resourcing.jsf;

import org.jdom.Element;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;
import org.yawlfoundation.yawl.resourcing.ResourceManager;
import org.yawlfoundation.yawl.resourcing.jsf.dynform.DynFormFactory;
import org.yawlfoundation.yawl.util.JDOMUtil;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: Michael Adams
 * Creation Date: 1/09/2009
 */
public class FormViewer {

    private SessionBean _sb;

    private ResourceManager _rm;

    public FormViewer() {
        _rm = ResourceManager.getInstance();
    }

    public FormViewer(SessionBean sb) {
        this();
        setSessionBean(sb);
    }

    public SessionBean getSessionBean() {
        return _sb;
    }

    public void setSessionBean(SessionBean sb) {
        _sb = sb;
    }

    public String display(WorkItemRecord wir) {
        String result;
        if (wir.getCustomFormURL() != null) {
            result = showCustomForm(wir);
        } else {
            _sb.setDynFormType(ApplicationBean.DynFormType.tasklevel);
            DynFormFactory df = _sb.getDynFormFactoryInstance();
            df.setHeaderText("Edit Work Item: " + wir.getCaseID());
            df.setDisplayedWIR(wir);
            if (df.initDynForm("YAWL 2.0 - Edit Work Item")) {
                result = "showDynForm";
            } else {
                result = "<failure>Cannot view item contents - problem initialising " + "dynamic form from task specification. " + "Please see the log files for details.</failure>";
            }
        }
        return result;
    }

    public String postDisplay(WorkItemRecord wir) {
        String result = "<success/>";
        if (_sb.isWirEdit()) {
            result = postEditWIR(wir);
        } else if (_sb.isCustomFormPost()) {
            result = postCustomForm(wir);
        }
        return result;
    }

    private String showCustomForm(WorkItemRecord wir) {
        String result = "<failure>Unspecified form URI</failure>";
        String url = wir.getCustomFormURL();
        if (url != null) {
            _sb.setCustomFormPost(true);
            try {
                adjustSessionTimeout(wir);
                result = buildURI(wir);
            } catch (Exception e) {
                _sb.setCustomFormPost(false);
                result = "<failure>IO Exception attempting to display custom form: " + e.getMessage() + "</failure>";
            }
        }
        return result;
    }

    private String buildURI(WorkItemRecord wir) {
        StringBuilder redir = new StringBuilder(wir.getCustomFormURL());
        redir.append((redir.indexOf("?") == -1) ? "?" : "&").append("workitem=").append(wir.getID()).append("&participantid=").append(_sb.getParticipant().getID()).append("&handle=").append(_sb.getSessionhandle()).append("&source=").append(getSourceURI());
        return redir.toString();
    }

    private void adjustSessionTimeout(WorkItemRecord wir) {
        String rawValue = null;
        Element data = wir.getDataList();
        if (data != null) {
            rawValue = data.getChildText("ySessionTimeout");
        }
        if (rawValue != null) {
            try {
                int minutes = new Integer(rawValue);
                HttpSession session = _sb.getExternalSession();
                _sb.setDefaultSessionTimeoutValue(session.getMaxInactiveInterval());
                session.setMaxInactiveInterval(minutes * 60);
                _sb.setSessionTimeoutValueChanged(true);
            } catch (NumberFormatException nfe) {
            }
        }
    }

    private String getSourceURI() {
        String result = "";
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (context != null) {
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            if (request != null) {
                result = request.getRequestURL().toString();
            }
        }
        return result;
    }

    /** updates a workitem after editing on a dynamic form */
    private String postEditWIR(WorkItemRecord wir) {
        String result = "<success/>";
        if (_sb.isWirEdit()) {
            if (wir != null) {
                Element data = JDOMUtil.stringToElement(_sb.getDynFormFactoryInstance().getDataList());
                wir.setUpdatedData(data);
                _rm.getWorkItemCache().update(wir);
                if (_sb.isCompleteAfterEdit()) result = completeWorkItem(wir);
            } else {
                result = "<failure>Could not complete workitem. Check log for details.</failure>";
            }
        }
        return result;
    }

    /** takes necessary action after editing a custom form */
    private String postCustomForm(WorkItemRecord wir) {
        String result = "<success/>";
        if (_sb.isSessionTimeoutValueChanged()) {
            _sb.resetSessionTimeout();
            _sb.setSessionTimeoutValueChanged(false);
        }
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String complete = request.getParameter("complete");
        if ((complete != null) && complete.equalsIgnoreCase("true")) {
            result = completeWorkItem(wir);
        }
        return result;
    }

    private String completeWorkItem(WorkItemRecord wir) {
        String result = _rm.checkinItem(_sb.getParticipant(), wir, _sb.getSessionhandle());
        return _rm.successful(result) ? "<success/>" : result;
    }
}
