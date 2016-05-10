package fr.xgouchet.gitstorageprovider.core.account;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.config.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * @author Xavier Gouchet
 */
public class LoadPersistedAccountsAction implements AsyncAction<Context, List<OAuthAccount>> {

    private static final int[] SERVICE_IDS = new int[]{
            OAuthConfigFactory.SERVICE_GITHUB
    };

    @Nullable
    @Override
    public List<OAuthAccount> performAction(final @NonNull Context context) throws Exception {

        List<OAuthAccount> output = new ArrayList<>();

        for (int serviceId : SERVICE_IDS) {
            OAuthAccount account = AccountsManager.loadPersistedAccount(context, serviceId);
            if (account != null) {
                output.add(account);
            }
        }

        return output;
    }
}
