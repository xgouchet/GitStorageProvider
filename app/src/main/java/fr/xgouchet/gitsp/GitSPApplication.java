package fr.xgouchet.gitsp;

import android.app.Application;

import com.google.android.agera.Repository;
import com.google.android.agera.Result;

import java.util.List;
import java.util.concurrent.ExecutorService;

import fr.xgouchet.gitsp.oauth.OAuthAccount;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * @author Xavier Gouchet
 */
public class GitSPApplication extends Application {


    private final ExecutorService backgroundExecutor = newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ExecutorService getBackgroundExecutor() {
        return backgroundExecutor;
    }
}
