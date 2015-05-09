package fr.xgouchet.gitstorageprovider.core.git;

import android.content.Context;

import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * This manager provides interface to get information on local repositories and perform actions over them
 *
 * @author Xavier Gouchet
 */
public class LocalRepositoriesManager {

    private static final String WORKSPACE_NAME = "workspace";



    private final Context mContext;
    private final File mLocalWorkspace;
    private final CredentialsManager mCredentialsManager;

    public LocalRepositoriesManager(Context context) {
        mContext = context;

        mCredentialsManager = new CredentialsManager(context);

        // get the local workspaces
        mLocalWorkspace = new File(context.getFilesDir(), WORKSPACE_NAME);
        mLocalWorkspace.mkdirs();
    }

    /**
     * Clones the remote project from the given uri to a local folder with the given name
     *
     * @param name
     * @param uri
     */
    public void cloneRepositoryAsync(String name, String uri) {

    }


}
