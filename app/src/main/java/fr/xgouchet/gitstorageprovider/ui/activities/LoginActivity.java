package fr.xgouchet.gitstorageprovider.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.apis.GithubOAuthConfig;
import fr.xgouchet.gitstorageprovider.ui.fragments.OAuthFragment;


/**
 * @author Xavier Gouchet
 */
public class LoginActivity extends FragmentActivity {

    // TODO add intent param to choose the 3rd party oauth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onStart() {
        super.onStart();
        OAuthFragment fragment = new OAuthFragment();
        fragment.setOAuthConfig(new GithubOAuthConfig());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_container, fragment)
                .commit();
    }


}
