package fr.xgouchet.gitstorageprovider.core.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitstorageprovider.core.events.AccountsChangedEvent;
import fr.xgouchet.gitsp.oauth.OAuthConfig;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.core.oauth.RequestAccessTokenAction;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;
import fr.xgouchet.gitstorageprovider.utils.actions.ActionQueueExecutor;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncActionListener;

/**
 * @author Xavier Gouchet
 */
public class AccountsManager {


    private static final String TAG = AccountsManager.class.getSimpleName();

    private final DoubleDeckerBus mBus;
    private final Context mContext;
    private final List<OAuthAccount> mAccounts = new ArrayList<>();

    private final ActionQueueExecutor mActionQueueExecutor = new ActionQueueExecutor();

    public AccountsManager(final @NonNull Context context,
                           final @NonNull DoubleDeckerBus bus) {
        mContext = context;
        mBus = bus;
    }

    public List<OAuthAccount> getAccounts() {
        return mAccounts;
    }

    /**
     * Sends an event to anyone interested that the local repos have changed
     */
    private void fireAccountsChanged() {
        mBus.postOnUiThread(new AccountsChangedEvent(mAccounts));
    }


    /**
     * Request the access token based on a Grant code fetched after the user logged himself in a webview
     *
     * @param oAuthConfig the config
     * @param code        the grant code
     */
    public void requestAccessToken(OAuthConfig oAuthConfig, String code) {
        RequestAccessTokenAction.Input input = new RequestAccessTokenAction.Input(oAuthConfig, code);
        mActionQueueExecutor.queueAction(new RequestAccessTokenAction(), input, mRequestAccessTokenListener);
    }

    /**
     * Listener for Access Token Request
     */
    private final AsyncActionListener<RequestAccessTokenAction.Input, RequestAccessTokenAction.Output>
            mRequestAccessTokenListener = new AsyncActionListener<RequestAccessTokenAction.Input, RequestAccessTokenAction.Output>() {

        @Override
        public void onActionPerformed(final @Nullable RequestAccessTokenAction.Output output) {

            if (output == null) {
                Log.w(TAG, "Got null AccessToken output");
                return;
            }

            // TODO send message : we're oauthenticated
            Log.i(TAG, "Authenticated : " + output);

            AsyncAction<String, OAuthAccount> action;
            switch (output.getOAuthConfig().getServiceId()) {
                case OAuthConfigFactory.SERVICE_GITHUB:
                    action = new fr.xgouchet.gitstorageprovider.core.api.github.GetAccountAction();
                    break;
                default:
                    action = null;
            }

            if (action != null) {
                mActionQueueExecutor.queueAction(action, output.getAccessToken(), mGetAccountListener);
            }
        }

        @Override
        public void onActionFailed(final @NonNull RequestAccessTokenAction.Input input,
                                   final @NonNull Exception e) {
            Log.e(TAG, "Error in OAuth request access token", e);
        }
    };

    /**
     * Listener for UserInfo request
     */
    private final AsyncActionListener<String, OAuthAccount>
            mGetAccountListener = new AsyncActionListener<String, OAuthAccount>() {

        @Override
        public void onActionPerformed(final @Nullable OAuthAccount account) {
            Log.i(TAG, "Got account : " + account);

            if (account == null) {
                Log.w(TAG, "Got null account !");
                return;
            }

            // set result
            if (mAccounts.contains(account)) {
                mAccounts.remove(account);
            }

            persistAccount(mContext, account);
            mAccounts.add(account);

            fireAccountsChanged();
        }

        @Override
        public void onActionFailed(final @NonNull String input,
                                   final @NonNull Exception e) {
            Log.e(TAG, "Error in get user info request", e);
        }
    };

    /**
     * Listener for persisted account loading
     */
    private final AsyncActionListener<Context, List<OAuthAccount>>
            mLoadPersistedAccountsListener = new AsyncActionListener<Context, List<OAuthAccount>>() {
        @Override
        public void onActionPerformed(@Nullable List<OAuthAccount> output) {
            mAccounts.clear();
            mAccounts.addAll(output);
            fireAccountsChanged();
        }

        @Override
        public void onActionFailed(@NonNull Context input, @NonNull Exception e) {
            Log.e(TAG, "Error in load persisted accounts", e);
        }
    };

    private static final String KEY_ACCESS_TOKEN = "account_%d_access_token";
    private static final String KEY_USER_NAME = "account_%d_user_name";

    /**
     * @param account persists the account
     */
    private static void persistAccount(final @NonNull Context context, final @NonNull OAuthAccount account) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        int serviceId = account.getServiceId();

        editor.putString(String.format(KEY_ACCESS_TOKEN, serviceId), account.getAccessToken());
        editor.putString(String.format(KEY_USER_NAME, serviceId), account.getUserName());

        editor.apply();
    }

    /**
     * @param serviceId the service id of the account to load
     * @return the account or null if none was found
     */
    @Nullable
    static OAuthAccount loadPersistedAccount(final @NonNull Context context, final int serviceId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String accessToken = preferences.getString(String.format(KEY_ACCESS_TOKEN, serviceId), null);
        String userName = preferences.getString(String.format(KEY_USER_NAME, serviceId), null);

        if ((accessToken == null) || (userName == null)) {
            return null;
        } else {
            return new OAuthAccount(serviceId, userName, accessToken);
        }
    }


    public void refreshAvailableAccounts() {
        mActionQueueExecutor.queueAction(new LoadPersistedAccountsAction(), mContext, mLoadPersistedAccountsListener);
    }
}
