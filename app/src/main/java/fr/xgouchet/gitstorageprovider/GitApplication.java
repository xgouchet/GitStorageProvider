package fr.xgouchet.gitstorageprovider;

import android.app.Application;

import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class GitApplication extends Application {

    private DoubleDeckerBus mBus;

    @Override
    public void onCreate() {
        super.onCreate();

        mBus = new DoubleDeckerBus();
    }

    public DoubleDeckerBus getBus() {
        return mBus;
    }
}
