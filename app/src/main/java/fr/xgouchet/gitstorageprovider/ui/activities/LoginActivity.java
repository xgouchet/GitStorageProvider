package fr.xgouchet.gitstorageprovider.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.ui.fragments.OAuthFragment;


/**
 * @author Xavier Gouchet
 */
public class LoginActivity extends FragmentActivity {

    // TODO android.app.ServiceConnectionLeaked:
    // Activity fr.xgouchet.gitstorageprovider.ui.activities.LoginActivity has leaked ServiceConnection
    // android.speech.tts.TextToSpeech$Connection@35000e85 that was originally bound here


    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final String EXTRA_SERVICE_ID = "service_id";

    /**
     * Starts the login activity using the given service as OAuth endpoint. The Account will be
     * set in the activity result
     *
     * @param context   the current application context
     * @param serviceId the service id
     */
    public static void loginWithService(final @NonNull Context context,
                                        final @OAuthConfigFactory.ServiceId int serviceId) {
        Intent login = new Intent(context, LoginActivity.class);
        login.putExtra(EXTRA_SERVICE_ID, serviceId);
        context.startActivity(login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onStart() {
        super.onStart();

        int serviceId = getIntent().getIntExtra(EXTRA_SERVICE_ID, OAuthConfigFactory.SERVICE_NONE);


        Bundle args = new Bundle(1);
        args.putInt(OAuthFragment.ARG_SERVICE_ID, serviceId);

        OAuthFragment fragment = new OAuthFragment();
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_container, fragment)
                .commit();
    }


}
