package fr.xgouchet.gitstorageprovider.core.oauth;

import android.net.Uri;

import org.apache.http.client.methods.HttpPost;

import java.net.URL;

/**
 * @author Xavier Gouchet
 */
public abstract class OAuthConfig {

    public abstract Uri getRequestTokenUri();



    /**
     * @return the Redirect URI used in case of success, as configured in the 3rd party API
     */
    public abstract String getRedirectUri();

    @Deprecated
    public abstract HttpPost getAccessTokenPost(String code);

    public abstract URL getRequestAccessTokenUrl(String code);
}
