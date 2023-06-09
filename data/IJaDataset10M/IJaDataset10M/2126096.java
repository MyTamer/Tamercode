package info.jtrac.domain;

import java.io.Serializable;

/**
 * Class that exists purely to hold a single user associated with an item
 * along with a integer "type" indicating the nature of the relationship
 * between Item --> User (one directional relationship)
 *
 * This is used in the following cases
 * - users "watching" an Item and need to be notified on Status changes
 *
 * and can be used for other kinds of relationships in the future
 */
public class ItemUser implements Serializable {

    private long id;

    private User user;

    private int type;

    public ItemUser() {
    }

    public ItemUser(User user) {
        this.user = user;
    }

    public ItemUser(User user, int type) {
        this.user = user;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id [").append(id);
        sb.append("]; user [").append(user);
        sb.append("]; type [").append(type);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemUser)) {
            return false;
        }
        final ItemUser iu = (ItemUser) o;
        return (user.equals(iu.getUser()) && type == iu.getType());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + user.hashCode();
        hash = hash * 31 + type;
        return hash;
    }
}
