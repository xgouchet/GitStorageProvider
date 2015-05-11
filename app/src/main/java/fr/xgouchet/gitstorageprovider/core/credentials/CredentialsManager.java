package fr.xgouchet.gitstorageprovider.core.credentials;

import android.content.Context;
import android.util.Log;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

import java.util.Arrays;

/**
 * @author Xavier Gouchet
 */
public class CredentialsManager extends CredentialsProvider {

    private static final String TAG = CredentialsManager.class.getSimpleName();
    private final Context mContext;

    public CredentialsManager(Context context) {
        mContext = context;
    }

    @Override
    public boolean isInteractive() {
        Log.i(TAG, "isInteractive()");
        return false;
    }

    @Override
    public boolean supports(CredentialItem... items) {
        Log.i(TAG, "supports(" + Arrays.toString(items) + ")");
        return false;
    }

    @Override
    public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
        Log.i(TAG, "get(" + uri + "," + Arrays.toString(items) + ")");
        // get(ssh://git@github.com:22,[org.eclipse.jgit.transport.CredentialItem$YesNoType@1c75e107])
        return false;
    }
}
