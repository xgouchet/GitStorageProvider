package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;
import java.util.Collection;

import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * TODO return a LocalRepo
 *
 * @author Xavier Gouchet
 */
public class CloneRepositoryAction implements AsyncAction<CloneRepositoryAction.Input, LocalRepo> {

    /**
     * The expected input for a clone action
     */
    public static class Input {
        final String mUri;
        final File mLocalPath;
        final CredentialsProvider mCredentialsProvider;

        public Input(final @NonNull String uri,
                     final @NonNull File localPath,
                     final @NonNull CredentialsProvider credentialsProvider) {
            mUri = uri;
            mLocalPath = localPath;
            mCredentialsProvider = credentialsProvider;
        }

        public File getLocalPath() {
            return mLocalPath;
        }
    }

    @Nullable
    @Override
    public LocalRepo performAction(final @NonNull Input input) throws Exception {

        // prepare the clone command
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(input.mUri)
                .setDirectory(input.mLocalPath)
                .setCredentialsProvider(input.mCredentialsProvider);

        // actually clone the repo
        Git git = cloneCommand.call();

        // get the status
        Status status = git.status().call();

        // get the remote url
        Collection<Ref> remotes = git.lsRemote().setRemote(Constants.DEFAULT_REMOTE_NAME).call();


        return new LocalRepo(input.mLocalPath, remotes, status);
    }

    @Override
    public String toString(){
        return "CloneRepositoryAction";
    }
}
