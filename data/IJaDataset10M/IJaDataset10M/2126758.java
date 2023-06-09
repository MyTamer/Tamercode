package org.codecover.eclipse.junit;

import org.codecover.eclipse.CodeCoverPlugin;
import org.codecover.eclipse.builder.CodeCoverClasspathProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;

/**
 * Is responsible to start a CodeCover JUnit Measurement launch out of the Editor
 * or the PackageExplorer by extending the popup or context menu.<br>
 * The final launch of the selected class, method a.s.o. will be launched by
 * {@link JUnitLaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)}.
 * 
 * @author Christoph Müller
 * @version 1.0 ($Id: JUnitLaunchShortcut.java 1 2007-12-12 17:37:26Z t-scheller $)
 * @see org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut
 */
public class JUnitLaunchShortcut extends org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut {

    @Override
    protected String getLaunchConfigurationTypeId() {
        return CodeCoverPlugin.JUNIT_LAUNCH_CONFIGURATION_ID;
    }

    @Override
    protected ILaunchConfigurationWorkingCopy createLaunchConfiguration(IJavaElement element) throws CoreException {
        ILaunchConfigurationWorkingCopy config = super.createLaunchConfiguration(element);
        CodeCoverClasspathProvider.setRunWithCodeCover(config, true);
        return config;
    }
}
