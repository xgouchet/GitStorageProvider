package fr.xgouchet.gitsp.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthAccountInfoObservable;
import fr.xgouchet.gitsp.oauth.OAuthAccountStore;
import fr.xgouchet.gitsp.oauth.config.OAuthConfig;
import fr.xgouchet.gitsp.oauth.config.OAuthConfigFactory;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulDialogFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Xavier Gouchet
 */
public class OAuthFragment extends StatefulDialogFragment {


    public static OAuthFragment withService(final @OAuthConfigFactory.ServiceId int serviceId) {
        OAuthFragment fragment = new OAuthFragment();

        Bundle args = new Bundle(1);
        args.putInt(ARG_SERVICE_ID, serviceId);
        fragment.setArguments(args);

        return fragment;
    }

    private static final String ARG_SERVICE_ID = "service_id";

    @Nullable
    private OAuthConfig oAuthConfig;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        oAuthConfig = getOAuthConfig();
        if (oAuthConfig == null) {
            stateDelegate.setFailure(new IllegalStateException("You're trying to add an account, but we don't seem to know from what service."));
            setCurrentState(StateHolder.ERROR);
        } else {
            setCurrentState(StateHolder.IDEAL);
        }

    }

    @Nullable
    private OAuthConfig getOAuthConfig() {
        Bundle args = getArguments();
        if (args == null) {
            return null;
        }

        int serviceId = args.getInt(ARG_SERVICE_ID, OAuthConfigFactory.SERVICE_NONE);
        return OAuthConfigFactory.getOAuthConfig(serviceId);
    }

    /*
     * CUSTOMIZATION
     */

    @Nullable
    @Override
    protected FabDelegate getFabDelegate() {
        return null;
    }

    @NonNull
    @Override
    public SimpleStateDelegate getStateDelegate() {
        return stateDelegate;
    }

    private final SimpleStateDelegate stateDelegate = new SimpleStateDelegate() {
        @SuppressLint("SetJavaScriptEnabled")
        @NonNull
        @Override
        public View createIdealView(@NonNull ViewGroup parent) {
            assert oAuthConfig != null;

            // setup webview
            WebView webview = new WebView(getActivity());
            webview.setWebViewClient(new OAuthWebViewClient(oAuthConfig, oAuthWebViewListener));

            // settings
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);

            return webview;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {
            assert oAuthConfig != null;
            ((WebView) idealView).loadUrl(oAuthConfig.getRequestTokenUri());
        }
    };

    private final OAuthWebViewClient.OAuthWebViewListener oAuthWebViewListener = new OAuthWebViewClient.OAuthWebViewListener() {
        @Override
        public void onCodeObtained(@NonNull String code) {
            Toast.makeText(getActivity(), "Code " + code, Toast.LENGTH_SHORT).show();
            assert oAuthConfig != null;
            stateDelegate.setLoading("Checking Access code");
            setCurrentState(StateHolder.LOADING);
            Observable.create(oAuthConfig.getAccessTokenObservable(code))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(accessTokenObserver);
        }

        @Override
        public void onError(String error, String errorDescription) {
            Toast.makeText(getActivity(), "Error : " + error + " / " + errorDescription, Toast.LENGTH_SHORT).show();
            stateDelegate.setFailure(new RuntimeException(errorDescription));
            setCurrentState(StateHolder.ERROR);
            // invalid_request, unauthorized_client, access_denied, unsupported_response_type, invalid_scope, server_error, temporarily_unavailable
        }
    };

    private final Observer<String> accessTokenObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            stateDelegate.setFailure(e);
            setCurrentState(StateHolder.ERROR);
        }

        @Override
        public void onNext(String accessToken) {
            stateDelegate.setLoading("Fetching account informations");
            setCurrentState(StateHolder.LOADING);
            if (oAuthConfig == null) {
                return;
            }
            Observable.create(new OAuthAccountInfoObservable(oAuthConfig, accessToken))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(oAuthAccountObserver);
        }
    };

    private final Observer<OAuthAccount> oAuthAccountObserver = new Observer<OAuthAccount>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            stateDelegate.setFailure(e);
            setCurrentState(StateHolder.ERROR);
        }

        @Override
        public void onNext(OAuthAccount oAuthAccount) {
            OAuthAccountStore.persistAccount(getActivity(), oAuthAccount);
            stateDelegate.setLoading("Creating credentials");

        }
    };


    private static class OAuthWebViewClient extends WebViewClient {

        public interface OAuthWebViewListener {
            void onCodeObtained(String code);

            void onError(String error, String errorDescription);
        }

        public static final String TAG = OAuthWebViewClient.class.getSimpleName();

        @NonNull
        private final OAuthConfig oAuthConfig;

        @NonNull
        private final OAuthWebViewListener oAuthWebViewListener;


        private OAuthWebViewClient(@NonNull OAuthConfig oAuthConfig,
                                   @NonNull OAuthWebViewListener oAuthWebViewListener) {
            this.oAuthConfig = oAuthConfig;
            this.oAuthWebViewListener = oAuthWebViewListener;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.d(TAG, "onPageStarted " + url);

            if (url.startsWith(oAuthConfig.getRedirectUri())) {
                // TODO handle state to prevent XSS

                String code = Uri.parse(url).getQueryParameter("code");
                String error = Uri.parse(url).getQueryParameter("error");
                String errorDescription = Uri.parse(url).getQueryParameter("error_description");
                if (code != null) {
                    oAuthWebViewListener.onCodeObtained(code);
                } else if (error != null) {
                    oAuthWebViewListener.onError(error, errorDescription);
                }

                view.stopLoading();
            }
        }
    }

}

