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

import fr.xgouchet.gitsp.oauth.OAuthConfig;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulDialogFragment;

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
            webview.setWebViewClient(new OAuthWebViewClient(oAuthConfig));

            // settings
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);


            return webview;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {
            // Load 1st OAuth page
            ((WebView) idealView).loadUrl(oAuthConfig.getRequestTokenUri());
        }
    };


    private static class OAuthWebViewClient extends WebViewClient {

        public static final String TAG = OAuthWebViewClient.class.getSimpleName();

        @NonNull
        private final OAuthConfig oAuthConfig;

        private OAuthWebViewClient(@NonNull OAuthConfig oAuthConfig) {
            this.oAuthConfig = oAuthConfig;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.d(TAG, "onPageStarted " + url);

            if (url.startsWith(oAuthConfig.getRedirectUri())) {


                // code=xxx&state=yyy -> success
                // TODO handle state to prevent XSS
                String code = Uri.parse(url).getQueryParameter("code");
                if (code != null) {
                    // TODO mAccountsManager.requestAccessToken(mOAuthConfig, code);
//                    finish("Fetching user information...");
                }

                // TODO handle error=error_code&error_description=description&error_uri=&state=yyy
                /**
                 * invalid_request, unauthorized_client, access_denied, unsupported_response_type, invalid_scope, server_error, temporarily_unavailable
                 */

                view.stopLoading();
            }
        }
    }

}

