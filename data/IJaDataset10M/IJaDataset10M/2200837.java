package uk.ac.warwick.dcs.boss.frontend.sites.staffpages;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import uk.ac.warwick.dcs.boss.frontend.Page;
import uk.ac.warwick.dcs.boss.frontend.PageContext;
import uk.ac.warwick.dcs.boss.frontend.PageLoadException;
import uk.ac.warwick.dcs.boss.model.FactoryException;
import uk.ac.warwick.dcs.boss.model.FactoryRegistrar;
import uk.ac.warwick.dcs.boss.model.dao.DAOException;
import uk.ac.warwick.dcs.boss.model.dao.DAOFactory;
import uk.ac.warwick.dcs.boss.model.dao.IAssignmentDAO;
import uk.ac.warwick.dcs.boss.model.dao.IDAOSession;
import uk.ac.warwick.dcs.boss.model.dao.IMarkingCategoryDAO;
import uk.ac.warwick.dcs.boss.model.dao.IModuleDAO;
import uk.ac.warwick.dcs.boss.model.dao.IStaffInterfaceQueriesDAO;
import uk.ac.warwick.dcs.boss.model.dao.beans.Assignment;
import uk.ac.warwick.dcs.boss.model.dao.beans.MarkingCategory;
import uk.ac.warwick.dcs.boss.model.dao.beans.Module;

public class MarkingCategoriesPage extends Page {

    public MarkingCategoriesPage() throws PageLoadException {
        super("staff_markingcategories", AccessLevel.USER);
    }

    @Override
    protected void handleGet(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            f = df.getInstance();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        String assignmentString = pageContext.getParameter("assignment");
        if (assignmentString == null) {
            throw new ServletException("No assignment parameter given");
        }
        Long assignmentId = Long.valueOf(pageContext.getParameter("assignment"));
        IMarkingCategoryDAO.SortingType sortingType = null;
        if (pageContext.getParameter("sorting") != null) {
            if (pageContext.getParameter("sorting").equals("weighting_asc")) {
                sortingType = IMarkingCategoryDAO.SortingType.WEIGHTING_ASCENDING;
                templateContext.put("sorting", "weighting_asc");
            } else if (pageContext.getParameter("sorting").equals("weighting_desc")) {
                sortingType = IMarkingCategoryDAO.SortingType.WEIGHTING_DESCENDING;
                templateContext.put("sorting", "weighting_desc");
            }
        } else {
            sortingType = IMarkingCategoryDAO.SortingType.NONE;
            templateContext.put("sorting", "");
        }
        try {
            f.beginTransaction();
            IStaffInterfaceQueriesDAO staffInterfaceQueriesDAO = f.getStaffInterfaceQueriesDAOInstance();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            Assignment assignment = assignmentDao.retrievePersistentEntity(assignmentId);
            if (!staffInterfaceQueriesDAO.isStaffModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new ServletException("permission denied");
            }
            IModuleDAO moduleDao = f.getModuleDAOInstance();
            Module module = moduleDao.retrievePersistentEntity(assignment.getModuleId());
            IMarkingCategoryDAO markingCategoryDao = f.getMarkingCategoryDAOInstance();
            markingCategoryDao.setSortingType(sortingType);
            MarkingCategory exampleMarkingCategory = new MarkingCategory();
            exampleMarkingCategory.setAssignmentId(assignmentId);
            Collection<MarkingCategory> result = markingCategoryDao.findPersistentEntitiesByExample(exampleMarkingCategory);
            f.endTransaction();
            templateContext.put("greet", pageContext.getSession().getPersonBinding().getChosenName());
            templateContext.put("module", module);
            templateContext.put("assignment", assignment);
            templateContext.put("markingCategories", result);
            pageContext.renderTemplate(template, templateContext);
        } catch (DAOException e) {
            f.abortTransaction();
            throw new ServletException("dao exception", e);
        }
    }

    @Override
    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        throw new ServletException("Unexpected POST");
    }
}
