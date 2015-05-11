package fr.xgouchet.gitstorageprovider.core.credentials;

/**
 * Parent class for all known credentials (SSH, HTTP user-password, ...)
 *
 * @author Xavier Gouchet
 */
public abstract class Credential {

    private final String mHost;
    private final String mDisplayName;

    public Credential(String host, String displayName) {
        mHost = host;
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getHost() {
        return mHost;
    }
}
