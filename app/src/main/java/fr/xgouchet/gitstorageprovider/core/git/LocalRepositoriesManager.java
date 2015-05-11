package fr.xgouchet.gitstorageprovider.core.git;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.xgouchet.gitstorageprovider.utils.actions.ActionQueueExecutor;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncActionListener;
import fr.xgouchet.gitstorageprovider.core.credentials.CredentialsManager;
import fr.xgouchet.gitstorageprovider.core.events.LocalRepositoriesChangedEvent;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * This manager provides interface to get information on local mLocalRepositories and perform actions over them
 *
 * @author Xavier Gouchet
 */
public class LocalRepositoriesManager {

    private static final String WORKSPACE_NAME = "workspace";
    private static final String TAG = LocalRepositoriesManager.class.getSimpleName();

    private final Context mContext;
    private final DoubleDeckerBus mBus;

    private final CredentialsManager mCredentialsManager;

    private final File mLocalWorkspace;

    private final ActionQueueExecutor mActionQueueExecutor = new ActionQueueExecutor();
    private final List<LocalRepository> mLocalRepositories = new ArrayList<>();


    public LocalRepositoriesManager(final @NonNull Context context,
                                    final @NonNull DoubleDeckerBus bus) {
        mContext = context;
        mBus = bus;

        mCredentialsManager = new CredentialsManager(context);

        // get the local workspaces
        mLocalWorkspace = new File(context.getFilesDir(), WORKSPACE_NAME);
        if (!mLocalWorkspace.exists()) {
            mLocalWorkspace.mkdirs();
        }
    }


    /**
     * Clones the remote project from the given uri to a local folder with the given name
     *
     * @param name the name of the local repository
     * @param uri  the remote (origin) url
     */
    public void cloneRepositoryAsync(String name, String uri) {
        CloneRepositoryAction.Input input = new CloneRepositoryAction.Input();
        input.uri = uri;
        input.localPath = new File(mLocalWorkspace, name);
        input.credentialsProvider = mCredentialsManager;

        mActionQueueExecutor.queueAction(
                new CloneRepositoryAction(),
                input,
                mCloneActionListener);
    }

    /**
     * List all local mLocalRepositories, and start actions to query their states
     */
    public void listLocalRepositories() {
        Log.d(TAG, "listLocalRepositories");
        mActionQueueExecutor.queueAction(
                new VerifyLocalRepositoriesAction(),
                mLocalWorkspace,
                mVerifyActionListener);
    }

    /**
     * Sends an event to anyone interested that the local repos have changed
     */
    private void fireLocalRepositoriesChanged() {
        mBus.postOnUiThread(new LocalRepositoriesChangedEvent(mLocalRepositories));
    }

    /**
     * Listener for clone actions
     */
    private final AsyncActionListener<File, List<LocalRepository>>
            mVerifyActionListener = new AsyncActionListener<File, List<LocalRepository>>() {

        @Override
        public void onActionPerformed(List<LocalRepository> output) {
            for (LocalRepository candidates : output) {
                if (mLocalRepositories.contains(candidates)) {
                    mLocalRepositories.remove(candidates);
                }
                mLocalRepositories.add(candidates);
            }
            fireLocalRepositoriesChanged();
        }

        @Override
        public void onActionFailed(File input, Exception e) {
            Log.e(TAG, "Verify failed", e);
        }
    };

    /**
     * Listener for clone actions
     */
    private final AsyncActionListener<CloneRepositoryAction.Input, LocalRepository>
            mCloneActionListener = new AsyncActionListener<CloneRepositoryAction.Input, LocalRepository>() {
        @Override
        public void onActionPerformed(LocalRepository output) {
            if (mLocalRepositories.contains(output)) {
                mLocalRepositories.remove(output);
            }
            mLocalRepositories.add(output);
            fireLocalRepositoriesChanged();
        }

        @Override
        public void onActionFailed(CloneRepositoryAction.Input input, Exception e) {
            Log.e(TAG, "Clone failed", e);
            LocalRepository fakeRepo = new LocalRepository(input.localPath);
            mActionQueueExecutor.queueAction(new DeleteRepositoryAction(), fakeRepo, mDeleteActionListener);
        }
    };

    /**
     * Listener for clone actions
     */
    private final AsyncActionListener<LocalRepository, Void>
            mDeleteActionListener = new AsyncActionListener<LocalRepository, Void>() {
        @Override
        public void onActionPerformed(Void output) {
            Log.d(TAG, "Delete successful");
        }

        @Override
        public void onActionFailed(LocalRepository input, Exception e) {
            Log.e(TAG, "Delete failed", e);
        }
    };
}
