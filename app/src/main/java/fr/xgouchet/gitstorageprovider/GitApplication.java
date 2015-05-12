package fr.xgouchet.gitstorageprovider;

import android.app.Application;

import fr.xgouchet.gitstorageprovider.core.account.AccountsManager;
import fr.xgouchet.gitstorageprovider.core.git.LocalRepositoriesManager;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class GitApplication extends Application {

    private DoubleDeckerBus mBus;

    private LocalRepositoriesManager mLocalRepositoriesManager;
    private AccountsManager mAccountsManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mBus = new DoubleDeckerBus();
        mLocalRepositoriesManager = new LocalRepositoriesManager(getApplicationContext(), mBus);
        mAccountsManager = new AccountsManager(getApplicationContext(), mBus);
    }

    public DoubleDeckerBus getBus() {
        return mBus;
    }

    public LocalRepositoriesManager getLocalRepositoriesManager() {
        return mLocalRepositoriesManager;
    }

    public AccountsManager getAccountsManager() {
        return mAccountsManager;
    }
}
