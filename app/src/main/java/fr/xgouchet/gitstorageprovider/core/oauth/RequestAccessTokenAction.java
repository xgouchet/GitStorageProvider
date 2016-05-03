package fr.xgouchet.gitstorageprovider.core.oauth;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import fr.xgouchet.gitsp.oauth.OAuthConfig;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncRequestAction;

/**
 * @author Xavier Gouchet
 */
public class RequestAccessTokenAction extends AsyncRequestAction<RequestAccessTokenAction.Input, RequestAccessTokenAction.Output> {


    private static final String TAG = RequestAccessTokenAction.class.getSimpleName();

    public static class Input {
        final OAuthConfig mConfig;
        final String mCode;

        /**
         * @param config the OAuth service configuration
         * @param code   the OAuth code
         */
        public Input(final @NonNull OAuthConfig config, final @NonNull String code) {
            mConfig = config;
            mCode = code;
        }
    }

    public static class Output {
        final OAuthConfig mConfig;
        final String mAccessToken;

        /**
         * @param config      the OAuth service configuration
         * @param accessToken the accessToken
         */
        public Output(final @NonNull OAuthConfig config, final @NonNull String accessToken) {
            mConfig = config;
            mAccessToken = accessToken;
        }

        @NonNull
        public OAuthConfig getOAuthConfig() {
            return mConfig;
        }

        @NonNull
        public String getAccessToken() {
            return mAccessToken;
        }
    }


    @NonNull
    @Override
    protected URL getRequestUrl(final @NonNull Input input) throws Exception {
//        return new URL(input.mConfig.getAccessTokenRequestUri());
        return null;
    }

    @Override
    protected void prepareUrlConnection(final @NonNull HttpURLConnection urlConnection,
                                        final @NonNull Input input) throws Exception {
        // Set POST params
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");

        // Construct post parameter
        StringBuilder postParams = new StringBuilder();
        postParams.append("grant_type=authorization_code");
        postParams.append("&code=");
        postParams.append(URLEncoder.encode(input.mCode, "UTF-8"));

        if (input.mConfig.getRedirectUri() != null) {
            postParams.append("&redirect_uri=");
            postParams.append(URLEncoder.encode(input.mConfig.getRedirectUri(), "UTF-8"));
        }

        postParams.append("&client_id=");
//        postParams.append(URLEncoder.encode(input.mConfig.getClientId(), "UTF-8"));

//        if (input.mConfig.getClientSecret() != null) {
//            postParams.append("&client_secret=");
//            postParams.append(URLEncoder.encode(input.mConfig.getClientSecret(), "UTF-8"));
//        }
        Log.i(TAG, postParams.toString());

        //  write params to the connection output stream
        urlConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
        writer.write(postParams.toString());
        writer.flush();
        writer.close();
    }

    @Override
    protected Output handleResponse(final @NonNull String response,
                                    final @NonNull Input input) throws Exception {
        JSONObject object = new JSONObject(response);
        String accessToken = object.optString("access_token");

        if (accessToken != null) {
            return new Output(input.mConfig, accessToken);
        } else {
            throw new IOException("No access_token in response");
        }
    }
}
