package fr.xgouchet.gitstorageprovider.core.oauth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * @author Xavier Gouchet
 */
public class RequestAccessTokenAction implements AsyncAction<RequestAccessTokenAction.Input, RequestAccessTokenAction.Output> {


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

    @Nullable
    @Override
    public Output performAction(@Nullable Input input) throws Exception {
        if (input == null) {
            throw new NullPointerException();
        }

        URL url = new URL(input.mConfig.getAccessTokenRequestUri());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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
        postParams.append(URLEncoder.encode(input.mConfig.getClientId(), "UTF-8"));

        if (input.mConfig.getClientSecret() != null) {
            postParams.append("&client_secret=");
            postParams.append(URLEncoder.encode(input.mConfig.getClientSecret(), "UTF-8"));
        }


        //  write params to the connection output stream
        urlConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
        writer.write(postParams.toString());
        writer.flush();
        writer.close();

        // Connect !
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            // read the response
            String line;
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            do {
                line = reader.readLine();

                if (line != null) {
                    content.append(line);
                }
            } while (line != null);
            reader.close();
            Log.i(TAG, content.toString());

            // parse the response
            // {
            //   "access_token":"c075e70101a018446e89dc9d0311f10b1a106ed9",
            //   "token_type":"bearer",
            //   "scope":"repo"
            // }
            JSONObject object = new JSONObject(content.toString());
            String accessToken = object.optString("access_token");

            if (accessToken != null) {
                return new Output(input.mConfig, accessToken);
            } else {
                throw new IOException("No access_token in response");
            }

        } else {
            throw new IOException("HTTP response : " + responseCode);
        }
    }
}
