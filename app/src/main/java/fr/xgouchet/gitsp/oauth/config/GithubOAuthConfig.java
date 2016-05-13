package fr.xgouchet.gitsp.oauth.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.xgouchet.gitsp.git.RemoteRepo;
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
public class GithubOAuthConfig extends OAuthConfig {


    private static final String OAUTH_LOGIN = "https://github.com/login/oauth/authorize";
    private static final String OAUTH_ACCESS = "https://github.com/login/oauth/access_token";


    private static final String CLIENT_ID = "ae6fa7487a90f106797f";
    private static final String SECRET_KEY = "4d581b0548e85fccc48fde56387efcca893b2d66";


    private static final String REDIRECT = "http://www.xgouchet.fr/gitstorageprovider/oauth/login_success";

    public static final String ENDPOINT = "https://api.github.com";
    public static final int SERVICE_ID = OAuthConfigFactory.SERVICE_GITHUB;

    @NonNull
    private final OkHttpClient okHttpClient;
    @Nullable
    private String accessTokenCode;

    public GithubOAuthConfig() {
        okHttpClient = new OkHttpClient.Builder().build();
    }

    @NonNull
    @Override
    protected String getAuthorizationEndpointUri() {
        return OAUTH_LOGIN;
    }


    @NonNull
    @Override
    public String getClientId() {
        return CLIENT_ID;
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
    public String getUserInfoRequestUri(@NonNull String accessToken) {
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

    @NonNull
    @Override
    public Observable.OnSubscribe<String> getAccessTokenObservable(String code) {
        this.accessTokenCode = code;
        return accessTokenObservable;
    }

    @NonNull
    public Observable.OnSubscribe<RemoteRepo> getRemoteRepoObservable() {
        return remoteRepoObservable;
    }

    private final Observable.OnSubscribe<String> accessTokenObservable = new Observable.OnSubscribe<String>() {

        @Override
        public void call(Subscriber<? super String> subscriber) {
            try {
                Request request = buildRequest();

                Response response = okHttpClient.newCall(request).execute();

                String accessToken = handleResponse(response);

                if (accessToken != null) {
                    subscriber.onNext(accessToken);
                }
                subscriber.onCompleted();

            } catch (IOException | JSONException e) {
                subscriber.onError(e);
            }
        }

        private Request buildRequest() throws UnsupportedEncodingException {
            String postParams = buildRequestParams();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, postParams);

            return new Request.Builder()
                    .header("Accept", "application/json")
                    .url(OAUTH_ACCESS)
                    .post(body)
                    .build();
        }

        @NonNull
        private String buildRequestParams() throws UnsupportedEncodingException {

            StringBuilder postParamsBuilder = new StringBuilder();
            postParamsBuilder.append("grant_type=authorization_code");

            postParamsBuilder.append("&code=");
            postParamsBuilder.append(URLEncoder.encode(accessTokenCode, "UTF-8"));

            postParamsBuilder.append("&redirect_uri=");
            postParamsBuilder.append(URLEncoder.encode(REDIRECT, "UTF-8"));

            postParamsBuilder.append("&client_id=");
            postParamsBuilder.append(URLEncoder.encode(CLIENT_ID, "UTF-8"));

            postParamsBuilder.append("&client_secret=");
            postParamsBuilder.append(URLEncoder.encode(SECRET_KEY, "UTF-8"));

            return postParamsBuilder.toString();
        }

        @Nullable
        private String handleResponse(Response response) throws JSONException, IOException {
            JSONObject object = new JSONObject(response.body().string());
            return object.optString("access_token");
        }
    };

    private final Observable.OnSubscribe<RemoteRepo> remoteRepoObservable = new Observable.OnSubscribe<RemoteRepo>() {

        @Override
        public void call(Subscriber<? super RemoteRepo> subscriber) {
            // TODO add settings for this !
            // ?visibility=*all*|public|private
            // ?affiliation=owner,collaborator,organization_member
            // ?sort=created|updated|pushed|*full_name*
            Request request = new Request.Builder()
                    .url(ENDPOINT + "/user/repos?access_token=" + getAccessToken())
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                parseRepositories(response.body().string(), subscriber);
            } catch (IOException | JSONException e) {
                subscriber.onError(e);
            }
        }

        public void parseRepositories(String response, Subscriber<? super RemoteRepo> subscriber) throws JSONException {
            JSONArray array = new JSONArray(response);
            int count = array.length();

            for (int i = 0; i < count; ++i) {
                JSONObject jsonRepo = array.getJSONObject(i);
                String name = jsonRepo.getString("name");
                String description = jsonRepo.getString("description");
                String url = jsonRepo.getString("git_url");
                String ssh_url = jsonRepo.getString("ssh_url");

                subscriber.onNext(new RemoteRepo(OAuthConfigFactory.SERVICE_GITHUB, name, url, description));
            }

            subscriber.onCompleted();
        }
    };

}
