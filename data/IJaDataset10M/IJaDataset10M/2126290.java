package net.sourceforge.cruisecontrol.publishers.sfee;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.Assert;

public class SfeeTestUtils {

    private Object inMemorySfee;

    private boolean notUsingInMemory;

    void loadSfeeInMemory(String serverUrl, String username, String password) {
        try {
            Class inMemSfeeFactoryClass = Class.forName("com.vasoftware.sf.InMemorySfeeFactory");
            Method resetMethod = inMemSfeeFactoryClass.getMethod("reset", null);
            resetMethod.invoke(null, null);
            Method createMethod = inMemSfeeFactoryClass.getMethod("create", new Class[] { String.class, String.class, String.class });
            inMemorySfee = createMethod.invoke(null, new Object[] { serverUrl, username, password });
        } catch (NoSuchMethodException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (IllegalAccessException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (InvocationTargetException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (ClassNotFoundException e) {
            notUsingInMemory = true;
        }
    }

    void addProject(String projectName) {
        if (notUsingInMemory) {
            return;
        }
        try {
            Method addProjectMethod = inMemorySfee.getClass().getMethod("addProject", new Class[] { String.class });
            addProjectMethod.invoke(inMemorySfee, new Object[] { projectName });
        } catch (NoSuchMethodException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (IllegalAccessException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (InvocationTargetException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        }
    }

    void addTracker(String trackerName, String projectName) {
        if (notUsingInMemory) {
            return;
        }
        try {
            Method addTracker = inMemorySfee.getClass().getMethod("addTracker", new Class[] { String.class, String.class });
            addTracker.invoke(inMemorySfee, new Object[] { trackerName, projectName });
        } catch (NoSuchMethodException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (IllegalAccessException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (InvocationTargetException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        }
    }

    void createFolders(String projectName, String path) {
        if (notUsingInMemory) {
            return;
        }
        try {
            Method createFolders = inMemorySfee.getClass().getMethod("createFolders", new Class[] { String.class, String.class });
            createFolders.invoke(inMemorySfee, new Object[] { projectName, path });
        } catch (NoSuchMethodException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (IllegalAccessException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        } catch (InvocationTargetException e) {
            Assert.fail("Must be using the wrong version of the sfee soap stubs.");
        }
    }
}
