package org.apache.roller.presentation.bookmarks.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.roller.model.BookmarkManager;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.pojos.BookmarkData;
import org.apache.roller.pojos.FolderData;
import org.apache.roller.pojos.PermissionsData;
import org.apache.roller.presentation.RollerRequest;
import org.apache.roller.presentation.RollerSession;
import org.apache.roller.presentation.bookmarks.formbeans.BookmarkFormEx;
import org.apache.roller.util.cache.CacheManager;

/**
 * @struts.action path="/editor/bookmarkSave" name="bookmarkFormEx"
 *    validate="true" input="/editor/bookmarkEdit.do"
 * @struts.action-forward name="Bookmarks" path="/editor/bookmarks.do?method=selectFolder"
 * 
 * @author Dave Johnson
 */
public class BookmarkSaveAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward("Bookmarks");
        BookmarkFormEx form = (BookmarkFormEx) actionForm;
        RollerRequest rreq = RollerRequest.getRollerRequest(request);
        BookmarkManager bmgr = RollerFactory.getRoller().getBookmarkManager();
        BookmarkData bd = null;
        if (null != form.getId() && !form.getId().trim().equals("")) {
            bd = bmgr.getBookmark(form.getId());
        } else {
            bd = new BookmarkData();
            FolderData fd = bmgr.getFolder(request.getParameter(RollerRequest.FOLDERID_KEY));
            bd.setFolder(fd);
        }
        RollerSession rses = RollerSession.getRollerSession(request);
        if (bd.getFolder().getWebsite().hasUserPermissions(rses.getAuthenticatedUser(), PermissionsData.AUTHOR)) {
            form.copyTo(bd, request.getLocale());
            bmgr.saveBookmark(bd);
            RollerFactory.getRoller().flush();
            CacheManager.invalidate(bd);
            request.setAttribute(RollerRequest.FOLDERID_KEY, bd.getFolder().getId());
        } else {
            ActionErrors errors = new ActionErrors();
            errors.add(null, new ActionError("error.permissions.deniedSave"));
            saveErrors(request, errors);
            forward = mapping.findForward("access-denied");
        }
        return forward;
    }
}
