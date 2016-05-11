package fr.xgouchet.gitsp.credentials;

import org.eclipse.jgit.transport.CredentialsProvider;

/**
 * Parent class for all known credentials (SSH, HTTP user-password, ...)
 *
 * @author Xavier Gouchet
 */
public abstract class Credential extends CredentialsProvider {

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
