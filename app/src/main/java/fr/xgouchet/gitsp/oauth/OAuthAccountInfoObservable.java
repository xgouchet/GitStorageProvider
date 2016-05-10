package fr.xgouchet.gitsp.oauth;

import android.support.annotation.NonNull;

import java.io.IOException;

import fr.xgouchet.gitsp.oauth.config.OAuthConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class OAuthAccountInfoObservable implements Observable.OnSubscribe<OAuthAccount> {

    @NonNull
    private final String accessToken;
    @NonNull
    private final OAuthConfig oAuthConfig;

    public OAuthAccountInfoObservable(@NonNull OAuthConfig oAuthConfig, @NonNull String accessToken) {
        this.oAuthConfig = oAuthConfig;
        this.accessToken = accessToken;
    }

    @Override
    public void call(Subscriber<? super OAuthAccount> subscriber) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(oAuthConfig.getUserInfoRequest(accessToken))
                .build();
        try {
            Response response = client.newCall(request).execute();

            String userName = oAuthConfig.parseUserName(response.body().string());

            subscriber.onNext(new OAuthAccount(oAuthConfig.getServiceId(), userName, accessToken));
            subscriber.onCompleted();
        } catch (IOException | IllegalArgumentException e) {
            subscriber.onError(e);
        }
    }
}
