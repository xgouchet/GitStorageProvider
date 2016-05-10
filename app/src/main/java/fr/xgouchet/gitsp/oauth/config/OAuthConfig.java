package fr.xgouchet.gitsp.oauth.config;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * @author Xavier Gouchet
 */
public abstract class OAuthConfig {

    @NonNull
    public String getRequestTokenUri() {

        Uri.Builder builder = Uri.parse(getAuthorizationEndpointUri()).buildUpon();

        // REQUIRED PARAMS
        builder.appendQueryParameter("response_type", "code");
        builder.appendQueryParameter("client_id", getClientId());

        // OPTIONAL PARAMS
        if (getScope() != null) {
            builder.appendQueryParameter("scope", getScope());
        }
        builder.appendQueryParameter("redirect_uri", getRedirectUri());

        // TODO include state to prevent XSS

        return builder.build().toString();
    }


    /**
     * @return the key in the shared preference to store the access token for this config
     */
    public final String getAccessTokenKey() {
        return String.format("%s_access_token", getConfigId());
    }

    /**
     * @return a unique id representing this configuration (usually the name of the OAuth provider, eg : github, google, ...)
     */
    public abstract String getConfigId();

    /**
     * @return the authorization endpoint uri (eg: http://provider.sample/oauth/authorize)
     */
    @NonNull
    protected abstract String getAuthorizationEndpointUri();

    /**
     * @return the Access token request uri (eg: http://provider.sample/oauth/access_token)
     */
    public abstract String getAccessTokenRequestUri();

    /**
     * @return the Redirect URI used in case of success, as configured in the 3rd party API
     */
    @NonNull
    public abstract String getRedirectUri();

    /**
     * @return the client id for your app
     */
    @NonNull
    public abstract String getClientId();

    /**
     * @return the secret key for your app
     */
    @Nullable
    public abstract String getClientSecret();

    /**
     * @return The scope of the access request (eg : read, write)
     */
    @Nullable
    protected abstract String getScope();

    /**
     * @param accessToken the current (valid) access token
     * @return the request URI to get the user information (user name, ...)
     */
    public abstract String getUserInfoRequest(@NonNull String accessToken);

    /**
     * Parse the user name from the response of the user info request
     *
     * @param string the body of the response
     * @return the user name
     * @throws IllegalArgumentException if the response does not contain a user name
     */
    @NonNull
    public abstract String parseUserName(@NonNull String string) throws IllegalArgumentException;

    /**
     * @return when querying the user info request, the path to the user name information.
     * for example, to get the user name in this JSON :
     * <pre><code>
     *     { "token" : "F78CE9", "info" : { "repos" : 6, "identity" : { "name" : "foo"}}}
     * </code></pre>
     * You should return {"info", "identity", "name"}
     */
    @NonNull
    public abstract String[] getUserNamePath();

    /**
     * @return the service id this config represents
     */
    @OAuthConfigFactory.ServiceId
    public abstract int getServiceId();
}
