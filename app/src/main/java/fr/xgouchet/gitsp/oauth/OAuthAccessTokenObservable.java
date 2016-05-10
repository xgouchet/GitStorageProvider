package fr.xgouchet.gitsp.oauth;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.xgouchet.gitsp.oauth.config.OAuthConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class OAuthAccessTokenObservable implements Observable.OnSubscribe<String> {


    private static final String TAG = OAuthAccessTokenObservable.class.getSimpleName();

    @NonNull
    private final OAuthConfig oAuthConfig;
    @NonNull
    private final String code;

    public OAuthAccessTokenObservable(@NonNull OAuthConfig oAuthConfig, @NonNull String code) {
        this.oAuthConfig = oAuthConfig;
        this.code = code;
    }

    @Override
    public void call(Subscriber<? super String> subscriber) {
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = buildRequest();

            Response response = client.newCall(request).execute();

            String accessToken = handleResponse(response);

            if (accessToken != null) {
                subscriber.onNext(accessToken);
            }
            subscriber.onCompleted();

        } catch (IOException | JSONException e) {
            subscriber.onError(e);
        }
    }

    private String handleResponse(Response response) throws JSONException, IOException {
        JSONObject object = new JSONObject(response.body().string());
        return object.optString("access_token");
    }

    private Request buildRequest() throws UnsupportedEncodingException {
        String postParams = buildRequestParams();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, postParams);

        return new Request.Builder()
                .header("Accept", "application/json")
                .url(oAuthConfig.getAccessTokenRequestUri())
                .post(body)
                .build();
    }

    @NonNull
    private String buildRequestParams() throws UnsupportedEncodingException {

        StringBuilder postParamsBuilder = new StringBuilder();
        postParamsBuilder.append("grant_type=authorization_code");

        postParamsBuilder.append("&code=");
        postParamsBuilder.append(URLEncoder.encode(code, "UTF-8"));

        postParamsBuilder.append("&redirect_uri=");
        postParamsBuilder.append(URLEncoder.encode(oAuthConfig.getRedirectUri(), "UTF-8"));

        postParamsBuilder.append("&client_id=");
        postParamsBuilder.append(URLEncoder.encode(oAuthConfig.getClientId(), "UTF-8"));

        String clientSecret = oAuthConfig.getClientSecret();
        if (clientSecret != null) {
            postParamsBuilder.append("&client_secret=");
            postParamsBuilder.append(URLEncoder.encode(clientSecret, "UTF-8"));
        }

        return postParamsBuilder.toString();
    }
}
