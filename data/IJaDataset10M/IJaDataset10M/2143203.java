package es.caib.sistra.persistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import es.caib.sistra.model.*;

/**
 * SessionBean para mantener y consultar OrganoResponsable
 *
 * @ejb.bean
 *  name="sistra/persistence/OrganoResponsableFacade"
 *  jndi-name="es.caib.sistra.persistence.OrganoResponsableFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 */
public abstract class OrganoResponsableFacadeEJB extends HibernateEJB {

    /**
     * @ejb.create-method
     * @ejb.permission role-name="${role.sistra}"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name="${role.sistra}"
     */
    public OrganoResponsable obtenerOrganoResponsable(Long id) {
        Session session = getSession();
        try {
            OrganoResponsable organoResponsable = (OrganoResponsable) session.load(OrganoResponsable.class, id);
            Hibernate.initialize(organoResponsable.getTramites());
            return organoResponsable;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name="${role.sistra}"
     */
    public Long grabarOrganoResponsable(OrganoResponsable obj) {
        Session session = getSession();
        try {
            session.saveOrUpdate(obj);
            return obj.getCodigo();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name="${role.sistra}"
     */
    public List listarOrganoResponsables() {
        Session session = getSession();
        try {
            Query query = session.createQuery("FROM OrganoResponsable AS m ORDER BY m.descripcion ASC");
            query.setCacheable(true);
            return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission role-name="${role.sistra}"
     */
    public void borrarOrganoResponsable(Long id) {
        Session session = getSession();
        try {
            OrganoResponsable organoResponsable = (OrganoResponsable) session.load(OrganoResponsable.class, id);
            session.delete(organoResponsable);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
}
