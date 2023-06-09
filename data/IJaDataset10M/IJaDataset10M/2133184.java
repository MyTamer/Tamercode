package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.jforum.JForumExecutionContext;
import net.jforum.entities.Bookmark;
import net.jforum.entities.BookmarkType;
import net.jforum.exceptions.DatabaseException;
import net.jforum.exceptions.InvalidBookmarkTypeException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: GenericBookmarkDAO.java,v 1.8 2006/08/23 02:13:42 rafaelsteil Exp $
 */
public class GenericBookmarkDAO implements net.jforum.dao.BookmarkDAO {

    /**
	 * @see net.jforum.dao.BookmarkDAO#add(net.jforum.entities.Bookmark)
	 */
    public void add(Bookmark b) {
        PreparedStatement p = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.add"));
            p.setInt(1, b.getUserId());
            p.setInt(2, b.getRelationId());
            p.setInt(3, b.getRelationType());
            p.setInt(4, b.isPublicVisible() ? 1 : 0);
            p.setString(5, b.getTitle());
            p.setString(6, b.getDescription());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(p);
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#update(net.jforum.entities.Bookmark)
	 */
    public void update(Bookmark b) {
        PreparedStatement p = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.update"));
            p.setInt(1, b.isPublicVisible() ? 1 : 0);
            p.setString(2, b.getTitle());
            p.setString(3, b.getDescription());
            p.setInt(4, b.getId());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(p);
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#remove(int)
	 */
    public void remove(int bookmarkId) {
        PreparedStatement p = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.remove"));
            p.setInt(1, bookmarkId);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(p);
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#selectByUser(int, int)
	 */
    public List selectByUser(int userId, int relationType) {
        if (relationType == BookmarkType.FORUM) {
            return this.getForums(userId);
        } else if (relationType == BookmarkType.TOPIC) {
            return this.getTopics(userId);
        } else if (relationType == BookmarkType.USER) {
            return this.getUsers(userId);
        } else {
            throw new InvalidBookmarkTypeException("The type " + relationType + " is not a valid bookmark type");
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#selectByUser(int)
	 */
    public List selectByUser(int userId) {
        List l = new ArrayList();
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectAllFromUser"));
            p.setInt(1, userId);
            rs = p.executeQuery();
            while (rs.next()) {
                l.add(this.getBookmark(rs));
            }
            return l;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#selectById(int)
	 */
    public Bookmark selectById(int bookmarkId) {
        Bookmark b = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectById"));
            p.setInt(1, bookmarkId);
            rs = p.executeQuery();
            if (rs.next()) {
                b = this.getBookmark(rs);
            }
            return b;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    /**
	 * @see net.jforum.dao.BookmarkDAO#selectForUpdate(int, int, int)
	 */
    public Bookmark selectForUpdate(int relationId, int relationType, int userId) {
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectForUpdate"));
            p.setInt(1, relationId);
            p.setInt(2, relationType);
            p.setInt(3, userId);
            Bookmark b = null;
            rs = p.executeQuery();
            if (rs.next()) {
                b = this.getBookmark(rs);
            }
            return b;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    protected List getUsers(int userId) {
        List l = new ArrayList();
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectUserBookmarks"));
            p.setInt(1, userId);
            rs = p.executeQuery();
            while (rs.next()) {
                Bookmark b = this.getBookmark(rs);
                if (b.getTitle() == null || "".equals(b.getTitle())) {
                    b.setTitle(rs.getString("username"));
                }
                l.add(b);
            }
            return l;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    protected List getTopics(int userId) {
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            List l = new ArrayList();
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectTopicBookmarks"));
            p.setInt(1, userId);
            rs = p.executeQuery();
            while (rs.next()) {
                Bookmark b = this.getBookmark(rs);
                if (b.getTitle() == null || "".equals(b.getTitle())) {
                    b.setTitle(rs.getString("topic_title"));
                }
                l.add(b);
            }
            return l;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    protected List getForums(int userId) {
        PreparedStatement p = null;
        ResultSet rs = null;
        try {
            List l = new ArrayList();
            p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("BookmarkModel.selectForumBookmarks"));
            p.setInt(1, userId);
            rs = p.executeQuery();
            while (rs.next()) {
                Bookmark b = this.getBookmark(rs);
                if (b.getTitle() == null || "".equals(b.getTitle())) {
                    b.setTitle(rs.getString("forum_name"));
                }
                if (b.getDescription() == null || "".equals(b.getDescription())) {
                    b.setDescription(rs.getString("forum_desc"));
                }
                l.add(b);
            }
            return l;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DbUtils.close(rs, p);
        }
    }

    protected Bookmark getBookmark(ResultSet rs) throws SQLException {
        Bookmark b = new Bookmark();
        b.setId(rs.getInt("bookmark_id"));
        b.setDescription(rs.getString("description"));
        b.setPublicVisible(rs.getInt("public_visible") == 1);
        b.setRelationId(rs.getInt("relation_id"));
        b.setTitle(rs.getString("title"));
        b.setDescription(rs.getString("description"));
        b.setUserId(rs.getInt("user_id"));
        b.setRelationType(rs.getInt("relation_type"));
        return b;
    }
}
