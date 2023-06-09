package android.accounts;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * A {@link Parcelable} value type that contains information about an account authenticator.
 */
public class AuthenticatorDescription implements Parcelable {

    /** The string that uniquely identifies an authenticator */
    public final String type;

    /** A resource id of a label for the authenticator that is suitable for displaying */
    public final int labelId;

    /** A resource id of a icon for the authenticator */
    public final int iconId;

    /** A resource id of a smaller icon for the authenticator */
    public final int smallIconId;

    /**
     * A resource id for a hierarchy of PreferenceScreen to be added to the settings page for the
     * account. See {@link AbstractAccountAuthenticator} for an example.
     */
    public final int accountPreferencesId;

    /** The package name that can be used to lookup the resources from above. */
    public final String packageName;

    /** A constructor for a full AuthenticatorDescription */
    public AuthenticatorDescription(String type, String packageName, int labelId, int iconId, int smallIconId, int prefId) {
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (packageName == null) throw new IllegalArgumentException("packageName cannot be null");
        this.type = type;
        this.packageName = packageName;
        this.labelId = labelId;
        this.iconId = iconId;
        this.smallIconId = smallIconId;
        this.accountPreferencesId = prefId;
    }

    /**
     * A factory method for creating an AuthenticatorDescription that can be used as a key
     * to identify the authenticator by its type.
     */
    public static AuthenticatorDescription newKey(String type) {
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        return new AuthenticatorDescription(type);
    }

    private AuthenticatorDescription(String type) {
        this.type = type;
        this.packageName = null;
        this.labelId = 0;
        this.iconId = 0;
        this.smallIconId = 0;
        this.accountPreferencesId = 0;
    }

    private AuthenticatorDescription(Parcel source) {
        this.type = source.readString();
        this.packageName = source.readString();
        this.labelId = source.readInt();
        this.iconId = source.readInt();
        this.smallIconId = source.readInt();
        this.accountPreferencesId = source.readInt();
    }

    /** @inheritDoc */
    public int describeContents() {
        return 0;
    }

    /** Returns the hashcode of the type only. */
    public int hashCode() {
        return type.hashCode();
    }

    /** Compares the type only, suitable for key comparisons. */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AuthenticatorDescription)) return false;
        final AuthenticatorDescription other = (AuthenticatorDescription) o;
        return type.equals(other.type);
    }

    public String toString() {
        return "AuthenticatorDescription {type=" + type + "}";
    }

    /** @inhericDoc */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(packageName);
        dest.writeInt(labelId);
        dest.writeInt(iconId);
        dest.writeInt(smallIconId);
        dest.writeInt(accountPreferencesId);
    }

    /** Used to create the object from a parcel. */
    public static final Creator<AuthenticatorDescription> CREATOR = new Creator<AuthenticatorDescription>() {

        /** @inheritDoc */
        public AuthenticatorDescription createFromParcel(Parcel source) {
            return new AuthenticatorDescription(source);
        }

        /** @inheritDoc */
        public AuthenticatorDescription[] newArray(int size) {
            return new AuthenticatorDescription[size];
        }
    };
}
