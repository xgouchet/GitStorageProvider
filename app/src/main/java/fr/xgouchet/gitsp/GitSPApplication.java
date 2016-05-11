package fr.xgouchet.gitsp;

import android.app.Application;
import android.util.Log;

import java.util.concurrent.ExecutorService;

import fr.xgouchet.gitsp.credentials.CredentialHelper;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * @author Xavier Gouchet
 */
public class GitSPApplication extends Application {

    private static final String TAG = GitSPApplication.class.getSimpleName();

    private final ExecutorService backgroundExecutor = newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        Observable.create(new CredentialHelper.Observable(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(credentialsObserver);
    }

    private Observer<Void> credentialsObserver = new Observer<Void>() {
        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.w(TAG, "onError", e);
        }

        @Override
        public void onNext(Void v) {
            Log.i(TAG, "onNext → " + v);
        }
    };


    public ExecutorService getBackgroundExecutor() {
        return backgroundExecutor;
    }
}
