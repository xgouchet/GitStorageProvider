package fr.xgouchet.gitstorageprovider.core.api.github;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfig;
import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfigFactory;

/**
 * @author Xavier Gouchet
 */
public class GithubOAuthConfig extends OAuthConfig {


    private static final String OAUTH_LOGIN = "https://github.com/login/oauth/authorize";
    private static final String OAUTH_ACCESS = "https://github.com/login/oauth/access_token";


    private static final String CLIENT_ID = "ae6fa7487a90f106797f";
    private static final String SECRET_KEY = "4d581b0548e85fccc48fde56387efcca893b2d66";


    private static final String REDIRECT = "http://www.xgouchet.fr/gitstorageprovider/oauth/login_success";

    public static final String ENDPOINT = "https://api.github.com";
    public static final int SERVICE_ID = OAuthConfigFactory.SERVICE_GITHUB;


    @NonNull
    @Override
    protected String getAuthorizationEndpointUri() {
        return OAUTH_LOGIN;
    }

    @Override
    protected String getAccessTokenRequestUri() {
        return OAUTH_ACCESS;
    }


    @NonNull
    @Override
    protected String getClientId() {
        return CLIENT_ID;
    }

    @Nullable
    @Override
    protected String getClientSecret() {
        return SECRET_KEY;
    }

    @NonNull
    @Override
    protected String getScope() {
        return "public_repo,repo";
    }

    @Nullable
    @Override
    public String getRedirectUri() {
        return REDIRECT;
    }

    @Override
    public String getConfigId() {
        return "github";
    }

    @Override
    public String getUserInfoRequest(@NonNull String accessToken) {
        return ENDPOINT + "/user?access_token=" + accessToken;
    }

    @NonNull
    @Override
    public String[] getUserNamePath() {
        return new String[]{"login"};
    }

    @Override
    public int getServiceId() {
        return OAuthConfigFactory.SERVICE_GITHUB;
    }
}
