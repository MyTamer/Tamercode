package au.edu.qut.yawl.persistence.dao;

import java.io.File;
import java.util.List;
import au.edu.qut.yawl.elements.YSpecification;
import au.edu.qut.yawl.exceptions.YPersistenceException;
import au.edu.qut.yawl.persistence.StringProducer;
import au.edu.qut.yawl.persistence.StringProducerYAWL;
import au.edu.qut.yawl.persistence.dao.restrictions.LogicalRestriction;
import au.edu.qut.yawl.persistence.dao.restrictions.PropertyRestriction;
import au.edu.qut.yawl.persistence.dao.restrictions.LogicalRestriction.Operation;
import au.edu.qut.yawl.persistence.dao.restrictions.PropertyRestriction.Comparison;
import au.edu.qut.yawl.unmarshal.YMarshal;

public class TestSpecificationMemoryDAO extends AbstractHibernateDAOTestCase {

    YSpecification testSpec;

    protected void setUp() throws Exception {
        super.setUp();
        StringProducer spx = StringProducerYAWL.getInstance();
        File f = spx.getTranslatedFile("TestCompletedMappings.xml", true);
        testSpec = (YSpecification) YMarshal.unmarshalSpecifications(f.toURI().toString()).get(0);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDelete() throws YPersistenceException {
        DAO myDAO = getDAO();
        myDAO.save(testSpec);
        YSpecification spec = (YSpecification) myDAO.retrieve(YSpecification.class, testSpec.getDbID());
        assertNotNull(spec);
        myDAO.delete(spec);
        spec = (YSpecification) myDAO.retrieve(YSpecification.class, testSpec.getDbID());
        assertNull(spec);
    }

    public void testRetrieve() throws YPersistenceException {
        DAO myDAO = getDAO();
        myDAO.save(testSpec);
        Long pk = testSpec.getDbID();
        YSpecification spec = (YSpecification) myDAO.retrieve(YSpecification.class, pk);
        assertNotNull(spec);
    }

    public void testRetrieveByRestriction() throws YPersistenceException {
        DAO myDAO = getDAO();
        testSpec.setDocumentation("asdf test 1234");
        myDAO.save(testSpec);
        String specName = "TEST_URI" + Math.random() + "_" + System.currentTimeMillis();
        YSpecification spec2 = new YSpecification(specName);
        spec2.setDocumentation("asdfblahasdf");
        myDAO.save(spec2);
        List specs = myDAO.retrieveByRestriction(YSpecification.class, new LogicalRestriction(new PropertyRestriction("documentation", Comparison.EQUAL, "asdf test 1234"), Operation.AND, new PropertyRestriction("ID", Comparison.EQUAL, testSpec.getID())));
        assertNotNull(specs);
        assertEquals("wrong number of matching specs", 1, specs.size());
        specs = myDAO.retrieveByRestriction(YSpecification.class, new LogicalRestriction(new PropertyRestriction("documentation", Comparison.EQUAL, "asdfblahasdf"), Operation.AND, new PropertyRestriction("ID", Comparison.EQUAL, specName)));
        assertNotNull(specs);
        assertEquals("retrieved wrong number of specs", 1, specs.size());
        myDAO.delete(spec2);
        specs = myDAO.retrieveByRestriction(YSpecification.class, new LogicalRestriction(new PropertyRestriction("documentation", Comparison.NOT_EQUAL, "asdfblahasdf"), Operation.AND, new PropertyRestriction("ID", Comparison.EQUAL, specName)));
        assertNotNull(specs);
        assertEquals("retrieved wrong number of specs", 0, specs.size());
        spec2 = spec2.deepClone();
        myDAO.save(spec2);
        specs = myDAO.retrieveByRestriction(YSpecification.class, new PropertyRestriction("documentation", Comparison.LIKE, "asdf%1234"));
        assertNotNull(specs);
        assertTrue("" + specs.size(), specs.size() == 1);
        specs = myDAO.retrieveByRestriction(YSpecification.class, new PropertyRestriction("documentation", Comparison.LIKE, "asdf%"));
        assertNotNull(specs);
        assertTrue("" + specs.size(), specs.size() == 2);
    }

    public void testSave() throws YPersistenceException {
        DAO myDAO = getDAO();
        myDAO.save(testSpec);
        YSpecification spec2 = (YSpecification) myDAO.retrieve(YSpecification.class, testSpec.getDbID());
        assertNotNull(spec2);
    }

    public void testGetKey() throws YPersistenceException {
        DAO myDAO = getDAO();
        myDAO.save(testSpec);
        YSpecification spec = (YSpecification) myDAO.retrieve(YSpecification.class, testSpec.getDbID());
        assertEquals(spec.getID(), testSpec.getID());
    }
}
