package fr.xgouchet.gitstorageprovider.core.oauth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.xgouchet.gitstorageprovider.core.account.Account;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * @author Xavier Gouchet
 */
public class GetUserInfoAction implements AsyncAction<GetUserInfoAction.Input, Account> {

    private static final String TAG = GetUserInfoAction.class.getSimpleName();

    public static class Input {

        final OAuthConfig mOAuthConfig;
        final String mAccessToken;

        public Input(OAuthConfig oAuthConfig, String accessToken) {
            mOAuthConfig = oAuthConfig;
            mAccessToken = accessToken;
        }

    }

    @Nullable
    @Override
    public Account performAction(@Nullable Input input) throws Exception {
        if (input == null) {
            return null;
        }

        URL requestUrl = new URL(input.mOAuthConfig.getUserInfoRequest(input.mAccessToken));
        Log.d(TAG, requestUrl.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();

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

            Object json = new JSONTokener(content.toString()).nextValue();
            String[] path = input.mOAuthConfig.getUserNamePath();

            String name = extractString(json, path);

            return new Account(input.mOAuthConfig.getServiceId(), name, input.mAccessToken);

        } else {
            throw new IOException("HTTP response : " + responseCode);
        }

    }

    private String extractString(Object json, String[] path) throws JSONException {

        if (path.length == 0) {
            throw new IllegalArgumentException("Ooopsy daisy");
        }

        return extractString(json, path, 0);
    }

    private String extractString(final @NonNull Object json,
                                 final @NonNull String[] path,
                                 final int index) throws JSONException {


        if (json instanceof JSONObject) {
            if (index == path.length - 1) {
                return ((JSONObject) json).getString(path[index]);
            } else {
                return extractString(((JSONObject) json).get(path[index]), path, index + 1);
            }
        }

        if (json instanceof JSONArray) {
            if (index == path.length - 1) {
                return ((JSONArray) json).getString(Integer.parseInt(path[index]));
            } else {
                return extractString(((JSONArray) json).get(Integer.parseInt(path[index])), path, index + 1);
            }
        }

        // TODO
        return null;
    }

}
