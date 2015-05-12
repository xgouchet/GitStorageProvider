package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.core.account.Account;
import fr.xgouchet.gitstorageprovider.core.account.AccountsManager;
import fr.xgouchet.gitstorageprovider.core.events.UserLoggedEvent;
import fr.xgouchet.gitstorageprovider.core.oauth.GetUserInfoAction;
import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfig;
import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.core.oauth.RequestAccessTokenAction;
import fr.xgouchet.gitstorageprovider.ui.activities.LoginActivity;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;
import fr.xgouchet.gitstorageprovider.utils.actions.ActionQueueExecutor;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncActionListener;

/**
 * @author Xavier Gouchet
 */
public class OAuthFragment extends Fragment {


    private static final String TAG = OAuthFragment.class.getSimpleName();

    public static final String ARG_SERVICE_ID = "service_id";


    private OAuthConfig mOAuthConfig;
    private AccountsManager mAccountsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common managers
        mAccountsManager = ((GitApplication) getActivity().getApplication()).getAccountsManager();

        Bundle args = getArguments();
        if (args == null) {
            finish("No argument supplied");
            return;
        }

        int serviceId = args.getInt(ARG_SERVICE_ID, OAuthConfigFactory.SERVICE_NONE);
        if (serviceId == OAuthConfigFactory.SERVICE_NONE) {
            finish("No service id supplied");
            return;
        }

        mOAuthConfig = OAuthConfigFactory.getOAuthConfig(serviceId);
        if (mOAuthConfig == null) {
            finish("No config found");
            return;
        }

        Log.d(TAG, "Staring OAuth sequence");
    }

    @SuppressWarnings("SetJavascriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setup webview
        WebView webview = new WebView(getActivity());
        webview.setWebViewClient(new OauthWebViewClient());


        // settings
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        // Load 1st OAuth page
        webview.loadUrl(mOAuthConfig.getRequestTokenUri());

        return webview;
    }


    private void finish(final @NonNull String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        Log.w(TAG, message);

        getActivity().finish();
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


                // code=xxx&state=yyy -> success
                // TODO handle state to prevent XSS
                String code = Uri.parse(url).getQueryParameter("code");
                if (code != null) {
                    mAccountsManager.requestAccessToken(mOAuthConfig, code);
                    finish("Fetching user information...");
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
