package org.yawlfoundation.yawl.resourcing.jsf;

import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.rave.web.ui.component.Button;
import com.sun.rave.web.ui.component.Label;
import com.sun.rave.web.ui.component.Listbox;
import org.yawlfoundation.yawl.resourcing.resource.Participant;
import javax.faces.FacesException;

public class pfAddRemove extends AbstractFragmentBean {

    private int __placeholder;

    private void _init() throws Exception {
    }

    public pfAddRemove() {
    }

    protected ApplicationBean getApplicationBean() {
        return (ApplicationBean) getBean("ApplicationBean");
    }

    protected SessionBean getSessionBean() {
        return (SessionBean) getBean("SessionBean");
    }

    protected RequestBean getRequestBean() {
        return (RequestBean) getBean("RequestBean");
    }

    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("pfAddRemove Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    public void destroy() {
    }

    /*******************************************************************************/
    private Button btnUnselect = new Button();

    public Button getBtnUnselect() {
        return btnUnselect;
    }

    public void setBtnUnselect(Button b) {
        btnUnselect = b;
    }

    private Button btnSelect = new Button();

    public Button getBtnSelect() {
        return btnSelect;
    }

    public void setBtnSelect(Button b) {
        btnSelect = b;
    }

    private Listbox lbxOwns = new Listbox();

    public Listbox getLbxOwns() {
        return lbxOwns;
    }

    public void setLbxOwns(Listbox l) {
        lbxOwns = l;
    }

    private Listbox lbxAvailable = new Listbox();

    public Listbox getLbxAvailable() {
        return lbxAvailable;
    }

    public void setLbxAvailable(Listbox l) {
        lbxAvailable = l;
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        label1 = l;
    }

    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        label2 = l;
    }

    /********************************************************************************/
    private SessionBean _sb = getSessionBean();

    public String btnSelect_action() {
        String id = (String) lbxAvailable.getSelected();
        if (id != null) {
            _sb.selectResourceAttribute(id);
            populateLists(_sb.getActiveResourceAttributeTab(), _sb.getParticipantForCurrentMode());
        }
        return null;
    }

    public String btnUnselect_action() {
        String id = (String) lbxOwns.getSelected();
        if (id != null) {
            _sb.unselectResourceAttribute(id);
            populateLists(_sb.getActiveResourceAttributeTab(), _sb.getParticipantForCurrentMode());
        }
        return null;
    }

    public void populateLists(String selTab, Participant p) {
        if (p != null) {
            clearLists();
            if (selTab == null) selTab = "tabRoles";
            lbxAvailable.setItems(_sb.getFullResourceAttributeList(selTab));
            lbxOwns.setItems(_sb.getParticipantAttributeList(selTab, p));
        }
    }

    public void clearLists() {
        _sb.setAvailableResourceAttributes(null);
        lbxAvailable.setItems(null);
        lbxAvailable.setSelected(null);
        clearOwnsList();
    }

    public void clearOwnsList() {
        _sb.setOwnedResourceAttributes(null);
        lbxOwns.setItems(null);
        lbxOwns.setSelected(null);
    }

    public void populateAvailableList() {
        String selTab = _sb.getActiveResourceAttributeTab();
        if (selTab != null) selTab = "tabRoles";
        lbxAvailable.setItems(_sb.getFullResourceAttributeList(selTab));
    }
}
