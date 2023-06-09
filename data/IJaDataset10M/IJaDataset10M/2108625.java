package be.fedict.eid.dss.admin.portal.control.bean;

import be.fedict.eid.dss.admin.portal.control.AdminConstants;
import be.fedict.eid.dss.admin.portal.control.XmlStyleSheet;
import be.fedict.eid.dss.entity.XmlStyleSheetEntity;
import be.fedict.eid.dss.model.XmlStyleSheetManager;
import be.fedict.eid.dss.model.exception.ExistingXmlStyleSheetException;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import java.io.InputStream;
import java.util.List;

@Stateful
@Name("dssXmlStyleSheet")
@LocalBinding(jndiBinding = AdminConstants.ADMIN_JNDI_CONTEXT + "XmlStyleSheetBean")
public class XmlStyleSheetBean implements XmlStyleSheet {

    @Logger
    private Log log;

    private InputStream uploadedFile;

    private String revision;

    private String namespace;

    @DataModel
    private List<XmlStyleSheetEntity> dssXmlStyleSheetList;

    @DataModelSelection
    private XmlStyleSheetEntity selectedXmlStyleSheet;

    @EJB
    private XmlStyleSheetManager xmlStyleSheetManager;

    @In
    private FacesMessages facesMessages;

    @Remove
    @Destroy
    @Override
    public void destroy() {
        this.log.debug("destroy");
    }

    @Override
    public String add() {
        this.log.debug("add #0 #1", this.namespace, this.revision);
        if (null == this.uploadedFile) {
            this.facesMessages.addToControl("file", "missing XML schema");
            return null;
        }
        try {
            this.xmlStyleSheetManager.add(this.namespace, this.revision, this.uploadedFile);
        } catch (ExistingXmlStyleSheetException e) {
            this.facesMessages.addToControl("file", "existing XML schema");
            return null;
        }
        this.revision = null;
        initList();
        return "success";
    }

    @Override
    public void delete() {
        this.log.debug("delete: " + this.selectedXmlStyleSheet.getNamespace());
        this.xmlStyleSheetManager.delete(this.selectedXmlStyleSheet.getNamespace());
        initList();
    }

    @Override
    public InputStream getUploadedFile() {
        return this.uploadedFile;
    }

    @Override
    public void setUploadedFile(InputStream uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    @Override
    @Factory("dssXmlStyleSheetList")
    public void initList() {
        this.dssXmlStyleSheetList = this.xmlStyleSheetManager.getXmlStyleSheets();
    }

    @Override
    public String getRevision() {
        return this.revision;
    }

    @Override
    public void setRevision(String revision) {
        this.revision = revision;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
