package fr.xgouchet.gitstorageprovider.core.api.github;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncRequestAction;

/**
 * @author Xavier Gouchet
 */
public class GetAccountAction extends AsyncRequestAction<String, OAuthAccount> {
    @NonNull
    @Override
    protected URL getRequestUrl(@NonNull String accessToken) throws Exception {
        return new URL(GithubOAuthConfig.ENDPOINT + "/user?access_token=" + accessToken);
    }

    @Override
    protected void prepareUrlConnection(@NonNull HttpURLConnection urlConnection, @NonNull String input) throws Exception {
    }

    @Override
    protected OAuthAccount handleResponse(@NonNull String response, @NonNull String accessToken) throws Exception {

        JSONObject object = new JSONObject(response);
        String userName = object.getString("login");

        return new OAuthAccount(GithubOAuthConfig.SERVICE_ID, userName, accessToken);
    }
}
