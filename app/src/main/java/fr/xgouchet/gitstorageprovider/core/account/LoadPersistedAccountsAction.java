package fr.xgouchet.gitstorageprovider.core.account;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * @author Xavier Gouchet
 */
public class LoadPersistedAccountsAction implements AsyncAction<Context, List<Account>> {

    private static final int[] SERVICE_IDS = new int[]{
            OAuthConfigFactory.SERVICE_GITHUB
    };

    @Nullable
    @Override
    public List<Account> performAction(@Nullable Context context) throws Exception {

        List<Account> output = new ArrayList<>();
        if (context == null) {
            return output;
        }


        for (int serviceId : SERVICE_IDS) {
            Account account = AccountsManager.loadPersistedAccount(context, serviceId);
            if (account != null) {
                output.add(account);
            }
        }

        return output;
    }
}
