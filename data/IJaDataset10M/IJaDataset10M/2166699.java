package de.abg.jreichert.serviceqos.ui.wizard;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.xtext.ui.wizard.IProjectInfo;
import org.eclipse.xtext.ui.wizard.XtextNewProjectWizard;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import com.google.inject.Inject;

public class ServiceQosNewProjectWizard extends XtextNewProjectWizard {

    private WizardNewProjectCreationPage mainPage;

    @Inject
    public ServiceQosNewProjectWizard(IProjectCreator projectCreator) {
        super(projectCreator);
        setWindowTitle("New ServiceQos Project");
    }

    /**
	 * Use this method to add pages to the wizard.
	 * The one-time generated version of this class will add a default new project page to the wizard.
	 */
    public void addPages() {
        mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
        mainPage.setTitle("ServiceQos Project");
        mainPage.setDescription("Create a new ServiceQos project.");
        addPage(mainPage);
    }

    /**
	 * Use this method to read the project settings from the wizard pages and feed them into the project info class.
	 */
    @Override
    protected IProjectInfo getProjectInfo() {
        de.abg.jreichert.serviceqos.ui.wizard.ServiceQosProjectInfo projectInfo = new de.abg.jreichert.serviceqos.ui.wizard.ServiceQosProjectInfo();
        projectInfo.setProjectName(mainPage.getProjectName());
        return projectInfo;
    }
}
