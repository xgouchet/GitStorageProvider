package fr.xgouchet.gitsp.credentials;

import android.content.Context;

import fr.xgouchet.gitsp.credentials.Credential;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class CredentialsSupplier implements Observable.OnSubscribe<Credential> {

    public CredentialsSupplier(Context context) {

    }

    @Override
    public void call(Subscriber<? super Credential> subscriber) {

        subscriber.onCompleted();
    }
}
