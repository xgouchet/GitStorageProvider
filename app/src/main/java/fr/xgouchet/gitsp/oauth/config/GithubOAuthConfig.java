package fr.xgouchet.gitsp.oauth.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

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
    public String getAccessTokenRequestUri() {
        return OAUTH_ACCESS;
    }


    @NonNull
    @Override
    public String getClientId() {
        return CLIENT_ID;
    }

    @Nullable
    @Override
    public String getClientSecret() {
        return SECRET_KEY;
    }

    @NonNull
    @Override
    protected String getScope() {
        return "public_repo,repo,write:public_key";
    }

    @NonNull
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
    public String parseUserName(@NonNull String response) throws IllegalArgumentException {
        try {
            JSONObject object = new JSONObject(response);
            String userName = object.getString("login");
            if (userName == null) {
                // Coding by exception, don't like it either... then again
                throw new NullPointerException();
            }
            return userName;
        } catch (JSONException | NullPointerException e) {
            throw new IllegalArgumentException(e);
        }

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
