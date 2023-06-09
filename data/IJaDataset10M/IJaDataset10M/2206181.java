package org.opennms.web.notification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.opennms.web.notification.filter.NotificationCriteria;
import org.opennms.web.notification.filter.NotificationIdFilter;
import org.opennms.web.notification.filter.NotificationCriteria.BaseNotificationCriteriaVisitor;
import org.opennms.web.notification.filter.NotificationCriteria.NotificationCriteriaVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcWebNotificationRepository implements WebNotificationRepository {

    @Autowired
    SimpleJdbcTemplate m_simpleJdbcTemplate;

    private String getSql(final String selectClause, final NotificationCriteria criteria) {
        final StringBuilder buf = new StringBuilder(selectClause);
        criteria.visit(new NotificationCriteriaVisitor<RuntimeException>() {

            boolean first = true;

            public void and(StringBuilder buf) {
                if (first) {
                    buf.append(" WHERE ");
                    first = false;
                } else {
                    buf.append(" AND ");
                }
            }

            public void visitAckType(AcknowledgeType ackType) throws RuntimeException {
                and(buf);
                buf.append(ackType.getAcknowledgeTypeClause());
            }

            public void visitFilter(org.opennms.web.filter.Filter filter) throws RuntimeException {
                and(buf);
                buf.append(filter.getParamSql());
            }

            public void visitLimit(int limit, int offset) throws RuntimeException {
                buf.append(" LIMIT ").append(limit).append(" OFFSET ").append(offset);
            }

            public void visitSortStyle(SortStyle sortStyle) throws RuntimeException {
                buf.append(" ");
                buf.append(sortStyle.getOrderByClause());
            }
        });
        return buf.toString();
    }

    private PreparedStatementSetter paramSetter(final NotificationCriteria criteria, final Object... args) {
        return new PreparedStatementSetter() {

            int paramIndex = 1;

            public void setValues(final PreparedStatement ps) throws SQLException {
                for (Object arg : args) {
                    ps.setObject(paramIndex, arg);
                    paramIndex++;
                }
                criteria.visit(new BaseNotificationCriteriaVisitor<SQLException>() {

                    @Override
                    public void visitFilter(org.opennms.web.filter.Filter filter) throws SQLException {
                        System.out.println("filter sql: " + filter.getSql());
                        paramIndex += filter.bindParam(ps, paramIndex);
                    }
                });
            }
        };
    }

    private static class NotificationMapper implements ParameterizedRowMapper<Notification> {

        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notice = new Notification();
            notice.m_notifyID = new Integer(rs.getInt("notifyid"));
            notice.m_timeSent = getTimestamp("pagetime", rs);
            notice.m_timeReply = getTimestamp("respondtime", rs);
            notice.m_txtMsg = rs.getString("textmsg");
            notice.m_numMsg = rs.getString("numericmsg");
            notice.m_responder = rs.getString("answeredby");
            notice.m_nodeID = new Integer(rs.getInt("nodeid"));
            notice.m_interfaceID = rs.getString("interfaceid");
            notice.m_eventId = new Integer(rs.getInt("eventid"));
            notice.m_serviceName = rs.getString("servicename");
            return notice;
        }

        private long getTimestamp(String field, ResultSet rs) throws SQLException {
            if (rs.getTimestamp(field) != null) {
                return rs.getTimestamp(field).getTime();
            } else {
                return 0;
            }
        }
    }

    public void acknowledgeMatchingNotification(String user, Date timestamp, NotificationCriteria criteria) {
        String sql = getSql("UPDATE NOTIFICATIONS SET RESPONDTIME=?, ANSWEREDBY=?", criteria);
        jdbc().update(sql, paramSetter(criteria, new Timestamp(timestamp.getTime()), user));
    }

    public Notification[] getMatchingNotifications(NotificationCriteria criteria) {
        String sql = getSql("SELECT NOTIFICATIONS.*, SERVICE.SERVICENAME FROM NOTIFICATIONS LEFT OUTER JOIN SERVICE USING (SERVICEID)", criteria);
        return getNotifications(sql, paramSetter(criteria));
    }

    private Notification[] getNotifications(String sql, PreparedStatementSetter paramSetter) {
        List<Notification> notifications = queryForList(sql, paramSetter, new NotificationMapper());
        return notifications.toArray(new Notification[0]);
    }

    public Notification getNotification(int noticeId) {
        Notification[] notifications = getMatchingNotifications(new NotificationCriteria(new NotificationIdFilter(noticeId)));
        if (notifications.length < 1) {
            return null;
        } else {
            return notifications[0];
        }
    }

    public int countMatchingNotifications(NotificationCriteria criteria) {
        String sql = getSql("SELECT COUNT(*) AS NOTICECOUNT FROM NOTIFICATIONS", criteria);
        return queryForInt(sql, paramSetter(criteria));
    }

    private int queryForInt(String sql, PreparedStatementSetter setter) throws DataAccessException {
        Number number = (Number) queryForObject(sql, setter, new SingleColumnRowMapper(Integer.class));
        return (number != null ? number.intValue() : 0);
    }

    @SuppressWarnings("unchecked")
    private Object queryForObject(String sql, PreparedStatementSetter setter, RowMapper rowMapper) throws DataAccessException {
        return DataAccessUtils.requiredSingleResult((List) jdbc().query(sql, setter, new RowMapperResultSetExtractor(rowMapper, 1)));
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> queryForList(String sql, PreparedStatementSetter setter, ParameterizedRowMapper<T> rm) {
        return (List<T>) jdbc().query(sql, setter, new RowMapperResultSetExtractor(rm));
    }

    private JdbcOperations jdbc() {
        return m_simpleJdbcTemplate.getJdbcOperations();
    }
}
