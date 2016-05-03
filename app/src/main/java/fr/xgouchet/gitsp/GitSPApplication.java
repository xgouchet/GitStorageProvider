package fr.xgouchet.gitsp;

import android.app.Application;

import com.google.android.agera.Repository;
import com.google.android.agera.Result;

import java.util.List;

import fr.xgouchet.gitsp.oauth.OAuthAccount;

/**
 * @author Xavier Gouchet
 */
public class GitSPApplication extends Application {

    Repository<Result<List<OAuthAccount>>> accountsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
