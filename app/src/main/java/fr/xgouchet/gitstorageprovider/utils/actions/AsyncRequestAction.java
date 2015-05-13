package fr.xgouchet.gitstorageprovider.utils.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A basic URL request action. By default performs a GET request, but you can change that in the
 * prepareUrlConnection() method.
 *
 * @author Xavier Gouchet
 */
public abstract class AsyncRequestAction<I, O> implements AsyncAction<I, O> {


    private static final String TAG = AsyncRequestAction.class.getSimpleName();

    @Nullable
    @Override
    public final O performAction(final @NonNull I input) throws Exception {

        URL url = getRequestUrl(input);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        Log.d(TAG, "prepareUrlConnection(" + url + ")");
        prepareUrlConnection(urlConnection, input);

        // Connect !
        Log.d(TAG, "connect(" + url + ")");
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        Log.d(TAG, "responseCode " + url + " -> " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.d(TAG, "readResponse(" + url + ")");
            String response = readResponse(urlConnection);

            Log.d(TAG, "handleResponse(\"" + response + "\")");
            return handleResponse(response, input);
        } else {
            throw new IOException("HTTP response " + responseCode);
        }
    }

    /**
     * @param input the action input
     * @return the url this request acts on, or null if the input is invalid
     */
    @NonNull
    protected abstract URL getRequestUrl(@NonNull I input) throws Exception;

    /**
     * Prepares the url connection (set header values, post data, ..)
     *
     * @param urlConnection the url connection
     * @param input         the input parameter
     */
    protected abstract void prepareUrlConnection(@NonNull HttpURLConnection urlConnection,
                                                 @NonNull I input) throws Exception;

    /**
     * Handles the response given by the server and create the action output
     *
     * @param response the server's response
     * @return the output for this action
     */
    protected abstract O handleResponse(@NonNull String response,
                                        @NonNull I input) throws Exception;

    /**
     * Reads the response of the server
     *
     * @param urlConnection the url connection
     * @return the response
     */
    @NonNull
    private String readResponse(final @NonNull HttpURLConnection urlConnection) throws IOException {

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

        return content.toString();
    }
}
