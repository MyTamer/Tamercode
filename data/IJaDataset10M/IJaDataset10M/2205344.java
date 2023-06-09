package es.caib.redose.admin.scheduler.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import es.caib.redose.admin.scheduler.servlet.RedoseSchedulerServlet;
import es.caib.redose.persistence.delegate.DelegateUtil;
import es.caib.redose.persistence.delegate.RdsProcesosDelegate;
import es.caib.sistra.plugins.PluginFactory;

/**
 * 
 * Job que realiza la consolidacion en el gestor documental
 *
 */
public class ConsolidacionGestorDocumentalJob implements Job {

    private Log log = LogFactory.getLog(ConsolidacionGestorDocumentalJob.class);

    /**
	 * Job que realiza consolidacion en el gestor documental
	 */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        RdsProcesosDelegate delegate = DelegateUtil.getRdsProcesosDelegate();
        log.debug("Job consolidacion gestor documental");
        try {
            boolean existePluginGestionDocumental = true;
            try {
                PluginFactory.getInstance().getPluginGestionDocumental();
            } catch (Exception e) {
                if (context.getScheduler() != null) {
                    context.getScheduler().deleteJob(RedoseSchedulerServlet.NAMEBORRADOGESTIONDOCUMENTAL, RedoseSchedulerServlet.GROUP);
                }
                existePluginGestionDocumental = false;
            }
            if (existePluginGestionDocumental) {
                delegate.consolidacionGestorDocumental();
            }
        } catch (Exception exc) {
            throw new JobExecutionException(exc);
        }
    }
}
