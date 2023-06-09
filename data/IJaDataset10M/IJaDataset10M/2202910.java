package net.sourceforge.seqware.common.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.seqware.common.dao.SequencerRunDAO;
import net.sourceforge.seqware.common.model.Registration;
import net.sourceforge.seqware.common.model.SequencerRun;
import net.sourceforge.seqware.common.model.SequencerRunWizardDTO;
import net.sourceforge.seqware.common.model.SequencerRun;
import net.sourceforge.seqware.common.util.NullBeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SequencerRunDAOHibernate extends HibernateDaoSupport implements SequencerRunDAO {

    private Logger logger;

    public SequencerRunDAOHibernate() {
        super();
        logger = Logger.getLogger(SequencerRunDAOHibernate.class);
    }

    public void insert(SequencerRun sequencerRun) {
        this.getHibernateTemplate().save(sequencerRun);
        getSession().flush();
    }

    public void insert(SequencerRunWizardDTO sequencerRun) {
        this.getHibernateTemplate().save(sequencerRun);
        getSession().flush();
    }

    public void update(SequencerRun sequencerRun) {
        this.getHibernateTemplate().update(sequencerRun);
        getSession().flush();
    }

    public void delete(SequencerRun sequencerRun) {
        this.getHibernateTemplate().delete(sequencerRun);
    }

    public List<Integer> getProcStatuses(SequencerRun sequencerRun) {
        List<Integer> procStatuses = Arrays.asList(0, 0, 0);
        String subQuery = "select COUNT(status)  from Processing myproc,( " + "select child_id id from processing_root_to_leaf p " + "UNION ALL " + "select distinct parent_id id from processing_root_to_leaf p " + "UNION ALL " + "select processing_id id from processing_ius pr_i " + "inner join ius i on (i.ius_id = pr_i.ius_id)" + "inner join lane ln on (ln.lane_id = i.lane_id) " + "where ln.sequencer_run_id = ? and processing_id not in (select parent_id from processing_relationship) " + "UNION ALL " + "SELECT processing_id id FROM processing_sequencer_runs p_s_r " + "where p_s_r.sequencer_run_id = ? and processing_id not in (select parent_id from processing_relationship) " + "UNION ALL " + "SELECT processing_id id FROM processing_lanes p_l " + "inner join lane l on (p_l.lane_id = l.lane_id) " + "where l.sequencer_run_id = ? and processing_id not in (select parent_id from processing_relationship)) ";
        String query = "WITH RECURSIVE processing_root_to_leaf (child_id, parent_id) AS ( " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p " + "inner join processing_ius pr_i on (pr_i.processing_id = p.parent_id) " + "inner join ius i on (i.ius_id = pr_i.ius_id) " + "inner join lane ln on (ln.lane_id=i.lane_id)" + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "UNION " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p " + "inner join processing_sequencer_runs p_s_r on (p_s_r.processing_id = p.parent_id) " + "where p_s_r.sequencer_run_id = ? " + "UNION " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p inner join processing_lanes p_l on (p_l.processing_id = p.parent_id) " + "inner join lane l on (p_l.lane_id = l.lane_id) " + "where l.sequencer_run_id = ? " + "UNION ALL " + "SELECT p.child_id, rl.parent_id " + "FROM processing_root_to_leaf rl, processing_relationship p " + "WHERE p.parent_id = rl.child_id)  " + subQuery + "q where ( position(? in status) > 0) and myproc.processing_id=q.id " + "UNION ALL " + subQuery + "q where ( position(? in status) > 0 or position(? in status) > 0) and myproc.processing_id=q.id " + "UNION ALL " + subQuery + "q where ( position(? in status) > 0) and myproc.processing_id=q.id";
        List list = this.getSession().createSQLQuery(query).setInteger(0, sequencerRun.getSequencerRunId()).setInteger(1, sequencerRun.getSequencerRunId()).setInteger(2, sequencerRun.getSequencerRunId()).setInteger(3, sequencerRun.getSequencerRunId()).setInteger(4, sequencerRun.getSequencerRunId()).setInteger(5, sequencerRun.getSequencerRunId()).setString(6, "success").setInteger(7, sequencerRun.getSequencerRunId()).setInteger(8, sequencerRun.getSequencerRunId()).setInteger(9, sequencerRun.getSequencerRunId()).setString(10, "running").setString(11, "pending").setInteger(12, sequencerRun.getSequencerRunId()).setInteger(13, sequencerRun.getSequencerRunId()).setInteger(14, sequencerRun.getSequencerRunId()).setString(15, "failed").list();
        if (list.get(0) != null) {
            procStatuses.set(0, Integer.parseInt(list.get(0).toString()));
        }
        if (list.get(1) != null) {
            procStatuses.set(1, Integer.parseInt(list.get(1).toString()));
        }
        if (list.get(2) != null) {
            procStatuses.set(2, Integer.parseInt(list.get(2).toString()));
        }
        return procStatuses;
    }

    public Integer getProcessedCnt(SequencerRun sequencerRun) {
        Integer processedCount = 0;
        String query = "WITH RECURSIVE processing_root_to_leaf (child_id, parent_id) AS ( " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p " + "inner join processing_lanes l on (l.processing_id = p.parent_id) " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "UNION ALL " + "SELECT p.child_id, rl.parent_id " + "FROM processing_root_to_leaf rl, processing_relationship p " + "WHERE p.parent_id = rl.child_id) " + "select COUNT(status)  from Processing myproc, " + "(select child_id id from processing_root_to_leaf p " + "UNION ALL " + "select distinct parent_id id from processing_root_to_leaf p " + "UNION ALL " + "select processing_id id from processing_lanes l " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "and processing_id not in (select parent_id from processing_relationship)) q " + "where ( position(? in status) > 0) " + "and myproc.processing_id=q.id";
        List list = this.getSession().createSQLQuery(query).setInteger(0, sequencerRun.getSequencerRunId()).setInteger(1, sequencerRun.getSequencerRunId()).setString(2, "success").list();
        if (list.get(0) != null) {
            processedCount = Integer.parseInt(list.get(0).toString());
        }
        logger.debug("SUCCESS count =" + processedCount);
        return processedCount;
    }

    public Integer getProcessingCnt(SequencerRun sequencerRun) {
        Integer processingCount = 0;
        String query = "WITH RECURSIVE processing_root_to_leaf (child_id, parent_id) AS ( " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p " + "inner join processing_lanes l on (l.processing_id = p.parent_id) " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "UNION ALL " + "SELECT p.child_id, rl.parent_id " + "FROM processing_root_to_leaf rl, processing_relationship p " + "WHERE p.parent_id = rl.child_id) " + "select COUNT(status)  from Processing myproc, " + "(select child_id id from processing_root_to_leaf p " + "UNION ALL " + "select distinct parent_id id from processing_root_to_leaf p " + "UNION ALL " + "select processing_id id from processing_lanes l " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "and processing_id not in (select parent_id from processing_relationship)) q " + "where ( position(? in status) > 0 or position(? in status) > 0) " + "and myproc.processing_id=q.id";
        List list = this.getSession().createSQLQuery(query).setInteger(0, sequencerRun.getSequencerRunId()).setInteger(1, sequencerRun.getSequencerRunId()).setString(2, "running").setString(3, "pending").list();
        if (list.get(0) != null) {
            processingCount = Integer.parseInt(list.get(0).toString());
        }
        return processingCount;
    }

    public Integer getErrorCnt(SequencerRun sequencerRun) {
        Integer errorCount = 0;
        String query = "WITH RECURSIVE processing_root_to_leaf (child_id, parent_id) AS ( " + "SELECT p.child_id as child_id, p.parent_id " + "FROM processing_relationship p " + "inner join processing_lanes l on (l.processing_id = p.parent_id) " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "UNION ALL " + "SELECT p.child_id, rl.parent_id " + "FROM processing_root_to_leaf rl, processing_relationship p " + "WHERE p.parent_id = rl.child_id) " + "select COUNT(status)  from Processing myproc, " + "(select child_id id from processing_root_to_leaf p " + "UNION ALL " + "select distinct parent_id id from processing_root_to_leaf p " + "UNION ALL " + "select processing_id id from processing_lanes l " + "inner join lane ln on (ln.lane_id = l.lane_id) " + "inner join sequencer_run sr on (sr.sequencer_run_id = ln.sequencer_run_id) " + "where sr.sequencer_run_id = ? " + "and processing_id not in (select parent_id from processing_relationship)) q " + "where ( position(? in status) > 0) " + "and myproc.processing_id=q.id";
        List list = this.getSession().createSQLQuery(query).setInteger(0, sequencerRun.getSequencerRunId()).setInteger(1, sequencerRun.getSequencerRunId()).setString(2, "failed").list();
        if (list.get(0) != null) {
            errorCount = Integer.parseInt(list.get(0).toString());
        }
        return errorCount;
    }

    public List<SequencerRun> list(Registration registration, Boolean isAsc) {
        ArrayList<SequencerRun> sequencerRuns = new ArrayList<SequencerRun>();
        if (registration == null) {
            return sequencerRuns;
        }
        String query = "";
        Object[] parameters = { registration.getRegistrationId() };
        String sortValue = (!isAsc) ? "asc" : "desc";
        List list = null;
        if (registration.isLIMSAdmin()) {
            query = "select * from sequencer_run as sr order by sr.create_tstmp " + sortValue + ";";
            parameters = null;
            list = this.getSession().createSQLQuery(query).addEntity(SequencerRun.class).list();
        } else {
            query = "select * from sequencer_run as sr where sr.owner_id = ? order by sr.create_tstmp" + sortValue + ";";
            list = this.getSession().createSQLQuery(query).addEntity(SequencerRun.class).setInteger(0, registration.getRegistrationId()).list();
        }
        for (Object obj : list) {
            SequencerRun sr = (SequencerRun) obj;
            sequencerRuns.add(sr);
        }
        return sequencerRuns;
    }

    /**
     * Finds an instance of SequencerRun in the database by the SequencerRun
     * name.
     *
     * @param name name of the SequencerRun
     * @return SequencerRun or null if not found
     */
    public SequencerRun findByName(String name) {
        String query = "from SequencerRun as sequencerRun where sequencerRun.name = ?";
        SequencerRun sequencerRun = null;
        Object[] parameters = { name };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            sequencerRun = (SequencerRun) list.get(0);
        }
        return sequencerRun;
    }

    /**
     * Finds an instance of SequencerRun in the database by the SequencerRun ID.
     *
     * @param expID ID of the SequencerRun
     * @return SequencerRun or null if not found
     */
    public SequencerRun findByID(Integer expID) {
        String query = "from SequencerRun as sequencerRun where sequencerRun.sequencerRunId = ?";
        SequencerRun sequencerRun = null;
        Object[] parameters = { expID };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            sequencerRun = (SequencerRun) list.get(0);
        }
        return sequencerRun;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SequencerRun findBySWAccession(Integer swAccession) {
        String query = "from SequencerRun as sequencerRun where sequencerRun.swAccession = ?";
        SequencerRun sequencerRun = null;
        Object[] parameters = { swAccession };
        List<SequencerRun> list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            sequencerRun = (SequencerRun) list.get(0);
        }
        return sequencerRun;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SequencerRun> findByOwnerID(Integer registrationId) {
        String query = "from SequencerRun as sequencerRun where sequencerRun.owner.registrationId = ?";
        Object[] parameters = { registrationId };
        return this.getHibernateTemplate().find(query, parameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SequencerRun> findByCriteria(String criteria, boolean isCaseSens) {
        String queryStringCase = "from SequencerRun as sr where " + " sr.description like :description " + " or cast(sr.swAccession as string) like :sw " + " or sr.name like :name order by sr.description ";
        String queryStringICase = "from SequencerRun as sr where " + " lower(sr.description) like :description " + " or cast(sr.swAccession as string) like :sw " + " or lower(sr.name) like :name order by sr.description ";
        Query query = isCaseSens ? this.getSession().createQuery(queryStringCase) : this.getSession().createQuery(queryStringICase);
        if (!isCaseSens) {
            criteria = criteria.toLowerCase();
        }
        criteria = "%" + criteria + "%";
        query.setString("description", criteria);
        query.setString("sw", criteria);
        query.setString("name", criteria);
        List<SequencerRun> res = query.list();
        filterResult(res);
        return res;
    }

    /**
     * Filter WizardDTO classes here
     *
     * @param res
     */
    private void filterResult(List<SequencerRun> res) {
        Iterator<SequencerRun> iter = res.iterator();
        while (iter.hasNext()) {
            SequencerRun val = iter.next();
            if (val instanceof SequencerRunWizardDTO) {
                iter.remove();
            }
        }
    }

    @Override
    public SequencerRun updateDetached(SequencerRun sequencerRun) {
        SequencerRun dbObject = reattachSequencerRun(sequencerRun);
        try {
            BeanUtilsBean beanUtils = new NullBeanUtils();
            beanUtils.copyProperties(dbObject, sequencerRun);
            return (SequencerRun) this.getHibernateTemplate().merge(dbObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SequencerRun> list() {
        ArrayList<SequencerRun> sequencerRuns = new ArrayList<SequencerRun>();
        String query = "from SequencerRun as sr";
        List list = this.getHibernateTemplate().find(query);
        for (Object obj : list) {
            SequencerRun sr = (SequencerRun) obj;
            sequencerRuns.add(sr);
        }
        return sequencerRuns;
    }

    @Override
    public void update(Registration registration, SequencerRun sequencerRun) {
        SequencerRun dbObject = reattachSequencerRun(sequencerRun);
        Logger logger = Logger.getLogger(SequencerRunDAOHibernate.class);
        if (registration == null) {
            logger.error("SequencerRunDAOHibernate update registration is null");
        } else if (registration.isLIMSAdmin() || (sequencerRun.givesPermission(registration) && dbObject.givesPermission(registration))) {
            logger.info("Updating sequencer run object");
            update(sequencerRun);
        } else {
            logger.error("sequencerRunDAOHibernate update not authorized");
        }
    }

    @Override
    public void insert(Registration registration, SequencerRun sequencerRun) {
        Logger logger = Logger.getLogger(SequencerRunDAOHibernate.class);
        if (registration == null) {
            logger.error("SequencerRunDAOHibernate insert SequencerRun registration is null");
        } else if (registration.isLIMSAdmin() || sequencerRun.givesPermission(registration)) {
            logger.info("insert sequencer run object");
            insert(sequencerRun);
        } else {
            logger.error("sequencerRunDAOHibernate insert not authorized");
        }
    }

    @Override
    public void insert(Registration registration, SequencerRunWizardDTO sequencerRun) {
        Logger logger = Logger.getLogger(SequencerRunDAOHibernate.class);
        if (registration == null) {
            logger.error("SequencerRunDAOHibernate insert SequencerRunWizardDTO registration is null");
        } else if (registration.isLIMSAdmin() || sequencerRun.givesPermission(registration)) {
            logger.info("insert SequencerRunWizardDTO object");
            insert(sequencerRun);
        } else {
            logger.error("sequencerRunDAOHibernate insert not authorized");
        }
    }

    @Override
    public SequencerRun updateDetached(Registration registration, SequencerRun sequencerRun) {
        SequencerRun dbObject = reattachSequencerRun(sequencerRun);
        Logger logger = Logger.getLogger(SequencerRunDAOHibernate.class);
        if (registration == null) {
            logger.error("SequencerRunDAOHibernate updateDetached registration is null");
        } else if (registration.isLIMSAdmin() || dbObject.givesPermission(registration)) {
            logger.info("updateDetached SequencerRun object");
            return updateDetached(sequencerRun);
        } else {
            logger.error("sequencerRunDAOHibernate updateDetached not authorized");
        }
        return null;
    }

    private SequencerRun reattachSequencerRun(SequencerRun sequencerRun) throws IllegalStateException, DataAccessResourceFailureException {
        SequencerRun dbObject = sequencerRun;
        if (!getSession().contains(sequencerRun)) {
            dbObject = findByID(sequencerRun.getSequencerRunId());
        }
        return dbObject;
    }
}
