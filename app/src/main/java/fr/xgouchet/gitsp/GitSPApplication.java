package fr.xgouchet.gitsp;

import android.app.Application;

import java.util.concurrent.ExecutorService;

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
