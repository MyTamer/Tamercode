package xtrememp.update;

import xtrememp.util.LanguageBundle;

/**
 *
 * @author Besmir Beqiri
 */
public class Version implements Comparable<Version> {

    public enum VersionType {

        BETA, BETA1, BETA2, RC, RC1, RC2, DEV, FINAL
    }

    /** 
     * Major number.
     */
    private int majorNumber;

    /** 
     * Minor number.
     */
    private int minorNumber;

    /** 
     * Micro number.
     */
    private int microNumber;

    /**
     * Version type.
     */
    private VersionType versionType = VersionType.FINAL;

    /** 
     * Release date.
     */
    private String releaseDate;

    /**
     * Download url string.
     */
    private String downloadURL;

    /**
     * Empty constructor
     */
    public Version() {
        super();
    }

    /**
     * Default constructor
     * 
     * @param majorNumber
     * @param minorNumber
     * @param microNumber
     * @param versionType
     * @param date
     * @param downloadURL
     */
    public Version(int majorNumber, int minorNumber, int microNumber, VersionType versionType, String date, String downloadURL) {
        super();
        this.majorNumber = majorNumber;
        this.minorNumber = minorNumber;
        this.microNumber = microNumber;
        this.versionType = versionType;
        this.releaseDate = date;
        this.downloadURL = downloadURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            return compareTo((Version) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new String(majorNumber + "." + minorNumber + "." + microNumber + "." + (versionType != VersionType.FINAL ? versionType : "")).hashCode();
    }

    @Override
    public int compareTo(Version version) {
        if (version == null) {
            throw new IllegalArgumentException();
        }
        if (majorNumber > version.getMajorNumber()) {
            return 1;
        } else if (majorNumber < version.getMajorNumber()) {
            return -1;
        } else if (minorNumber > version.getMinorNumber()) {
            return 1;
        } else if (minorNumber < version.getMinorNumber()) {
            return -1;
        } else if (microNumber > version.getMicroNumber()) {
            return 1;
        } else if (microNumber < version.getMicroNumber()) {
            return -1;
        } else {
            return this.versionType.compareTo(version.getVersionType());
        }
    }

    /**
     * Gets major number.
     * 
     * @return the major number
     */
    public int getMajorNumber() {
        return majorNumber;
    }

    /**
     * Gets minor number.
     * 
     * @return the minor number
     */
    public int getMinorNumber() {
        return minorNumber;
    }

    /**
     * Gets micro number.
     * 
     * @return the micro number
     */
    public int getMicroNumber() {
        return microNumber;
    }

    /**
     * Returns version type
     * 
     * @return VersionType
     */
    public VersionType getVersionType() {
        return versionType;
    }

    /**
     * Gets release date.
     * 
     * @return the release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets download url.
     * 
     * @return the download url
     */
    public String getDownloadURL() {
        return downloadURL;
    }

    /**
     * Returns version in string format.
     * 
     * @return the version in string format
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(majorNumber);
        sb.append(".");
        sb.append(minorNumber);
        sb.append(".");
        sb.append(microNumber);
        if (versionType != VersionType.FINAL) {
            sb.append(" ");
            sb.append(versionType);
        }
        return sb.toString();
    }

    /**
     * Sets major number.
     * 
     * @param majorNumber
     *            the major number
     */
    protected void setMajorNumber(int majorNumber) {
        this.majorNumber = majorNumber;
    }

    /**
     * Sets minor number.
     * 
     * @param minorNumber
     *            the minor number
     */
    protected void setMinorNumber(int minorNumber) {
        this.minorNumber = minorNumber;
    }

    /**
     * Sets micro number.
     * 
     * @param microNumber the micro number
     */
    protected void setMicroNumber(int microNumber) {
        this.microNumber = microNumber;
    }

    /**
     * Sets the version type value
     * 
     * @param version the version type
     */
    protected void setVersionType(VersionType versionType) {
        this.versionType = versionType;
    }

    /**
     * Sets release date.
     * 
     * @param date the release date
     */
    protected void setReleaseDate(String date) {
        this.releaseDate = date;
    }

    /**
     * Sets download url.
     * 
     * @param downloadURL
     *            the download url
     */
    protected void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public static final Version getCurrentVersion() {
        int majorNumber = Integer.parseInt(LanguageBundle.getString("Application.version.majorNumber"));
        int minorNumber = Integer.parseInt(LanguageBundle.getString("Application.version.minorNumber"));
        int microNumber = Integer.parseInt(LanguageBundle.getString("Application.version.microNumber"));
        String releaseDate = LanguageBundle.getString("Application.version.releaseDate");
        return new Version(majorNumber, minorNumber, microNumber, VersionType.FINAL, releaseDate, "");
    }
}
