package fr.xgouchet.gitsp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.agera.Observable;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;

import java.util.concurrent.ExecutorService;

import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitstorageprovider.R;

/**
 * @author Xavier Gouchet
 */
public class MainActivity extends AppCompatActivity {


    Repository<Result<OAuthAccount>> accountsRepository;
    private ExecutorService networkExecutor;
    private Observable needAccountObservable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
