package fr.xgouchet.gitstorageprovider;

import android.app.Application;

import fr.xgouchet.gitstorageprovider.core.git.LocalRepositoriesManager;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class GitApplication extends Application {

    private DoubleDeckerBus mBus;
    private LocalRepositoriesManager mLocalRepositoriesManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mBus = new DoubleDeckerBus();
        mLocalRepositoriesManager = new LocalRepositoriesManager(getApplicationContext(), mBus);
    }

    public DoubleDeckerBus getBus() {
        return mBus;
    }

    public LocalRepositoriesManager getLocalRepositoriesManager() {
        return mLocalRepositoriesManager;
    }
}
