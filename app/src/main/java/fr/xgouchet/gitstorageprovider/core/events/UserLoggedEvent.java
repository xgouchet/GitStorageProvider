package fr.xgouchet.gitstorageprovider.core.events;

import fr.xgouchet.gitsp.oauth.OAuthConfig;

/**
 * @author Xavier Gouchet
 */
public class UserLoggedEvent {


    private final OAuthConfig mOAuthConfig;
    private final String mAccessToken;

    public UserLoggedEvent(OAuthConfig oAuthConfig, String accessToken) {
        mOAuthConfig = oAuthConfig;
        mAccessToken = accessToken;
    }

    public OAuthConfig getOAuthConfig() {
        return mOAuthConfig;
    }

    public String getAccessToken() {
        return mAccessToken;
    }
}
