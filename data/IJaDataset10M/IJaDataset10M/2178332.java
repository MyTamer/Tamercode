package siseor.tests;

import junit.textui.TestRunner;
import siseor.Description;
import siseor.SiseorFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Description</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DescriptionTest extends MetadataTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(DescriptionTest.class);
    }

    /**
	 * Constructs a new Description test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DescriptionTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Description test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Description getFixture() {
        return (Description) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(SiseorFactory.eINSTANCE.createDescription());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
    @Override
    protected void tearDown() throws Exception {
        setFixture(null);
    }
}
