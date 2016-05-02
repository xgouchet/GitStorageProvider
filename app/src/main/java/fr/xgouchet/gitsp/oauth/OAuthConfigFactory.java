package fr.xgouchet.gitsp.oauth;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.xgouchet.gitstorageprovider.core.api.github.GithubOAuthConfig;

/**
 * @author Xavier Gouchet
 */
public final class OAuthConfigFactory {


    private static final String TAG = OAuthConfigFactory.class.getSimpleName();


    public static final int SERVICE_NONE = -1;
    public static final int SERVICE_GITHUB = 1;

    @IntDef({SERVICE_NONE, SERVICE_GITHUB})
    public @interface ServiceId {
    }


    @Nullable
    public static OAuthConfig getOAuthConfig(final int serviceId) {
        switch (serviceId) {
            case SERVICE_GITHUB:
                return new GithubOAuthConfig();
            case SERVICE_NONE:
                return null;
            default:
                Log.w(TAG, "unknown service id : " + serviceId);
                return null;
        }
    }
}
