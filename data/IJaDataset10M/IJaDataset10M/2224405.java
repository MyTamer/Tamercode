package org.yawlfoundation.yawl.engine;

import org.yawlfoundation.yawl.elements.YSpecVersion;
import java.io.Serializable;

/** * @author Mike Fowler *         Date: 14-Sep-2006 */
public class P_YSpecFileID implements Serializable {

    private String id;

    private YSpecVersion version;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version.toString();
    }

    public void setVersion(YSpecVersion version) {
        this.version = version;
    }

    public void setVersion(String version) {
        this.version = new YSpecVersion(version);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof P_YSpecFileID) {
            P_YSpecFileID instance = (P_YSpecFileID) obj;
            return instance.getId().equals(this.getId()) && instance.getVersion() == this.getVersion();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "P_YSpecFileID: ID = '" + id + "' version = '" + version.toString() + "'";
    }
}
