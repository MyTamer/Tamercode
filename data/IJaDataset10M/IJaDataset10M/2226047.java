package updaters;

import java.util.StringTokenizer;

public class FileInfo {

    /**
     * Format of string:
     * local directory offset*server directory offset*crc32 of remote file
     *
     * Examples:
     * xith.bat*xith.bat*123437
     * jre/bin/java.exe*jre/bin/java.exe*912344845
     * cosm.jar!com/navtools/util/IOUtil.class*cosm.jar/com/navtools/util/IOUtil.class*44359845
     *
     * @param manifestLine the string to be parsed
     */
    public FileInfo(String manifestLine) {
        StringTokenizer toker = new StringTokenizer(manifestLine, VersionManifest.separator);
        localOffset_ = toker.nextToken();
        remoteOffset_ = toker.nextToken();
        crc32_ = Long.parseLong(toker.nextToken());
    }

    public String getRemoteOffset() {
        return remoteOffset_;
    }

    public String getLocalOffset() {
        return localOffset_;
    }

    public long getCRC32() {
        return crc32_;
    }

    public boolean getTempFileUpToDate() {
        return tempFileUpToDate_;
    }

    public void setTempFileUpToDate(boolean isLatest) {
        tempFileUpToDate_ = isLatest;
    }

    protected long crc32_ = -1;

    protected String remoteOffset_;

    protected String localOffset_;

    protected boolean tempFileUpToDate_ = false;
}
