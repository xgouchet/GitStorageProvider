package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfig;
import fr.xgouchet.gitstorageprovider.core.oauth.RequestAccessTokenAction;
import fr.xgouchet.gitstorageprovider.utils.actions.ActionQueueExecutor;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncActionListener;

/**
 * @author Xavier Gouchet
 */
public class OAuthFragment extends Fragment {

    private static final String TAG = OAuthFragment.class.getSimpleName();
    private final ActionQueueExecutor mActionQueueExecutor = new ActionQueueExecutor();

    private OAuthConfig mOAuthConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setup webview
        WebView webview = new WebView(getActivity());
        webview.setWebViewClient(new OauthWebViewClient());


        // settings
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        // Load 1st OAuth page
        webview.loadUrl(mOAuthConfig.getRequestTokenUri().toString());

        return webview;
    }


    public void setOAuthConfig(final @NonNull OAuthConfig OAuthConfig) {
        mOAuthConfig = OAuthConfig;
    }


    /**
     *
     */
    private class OauthWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.d(TAG, "onPageStarted " + url);

            if (url.startsWith(mOAuthConfig.getRedirectUri())) {
                String code = Uri.parse(url).getQueryParameter("code");

                RequestAccessTokenAction.Input input = new RequestAccessTokenAction.Input(mOAuthConfig, code);
                mActionQueueExecutor.queueAction(new RequestAccessTokenAction(), input, mRequestAccessTokenListener);

                view.stopLoading();
            }


        }
    }


    private AsyncActionListener<RequestAccessTokenAction.Input, String>
            mRequestAccessTokenListener = new AsyncActionListener<RequestAccessTokenAction.Input, String>() {

        @Override
        public void onActionPerformed(String output) {
            // TODO send message : we're oauthenticated
            Log.i(TAG, "Authenticated !");
            // TODO save access token somewhere
        }

        @Override
        public void onActionFailed(RequestAccessTokenAction.Input input, Exception e) {
            Log.e(TAG, "Error in OAuth request access token", e);
        }
    };
}
