package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.Nullable;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;
import java.util.Collection;

import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * TODO return a LocalRepository
 *
 * @author Xavier Gouchet
 */
public class CloneRepositoryAction implements AsyncAction<CloneRepositoryAction.Input, LocalRepository> {

    /**
     * The expected input for a clone action
     */
    public static class Input {
        public String uri;
        public File localPath;
        public CredentialsProvider credentialsProvider;
    }

    @Nullable
    @Override
    public LocalRepository performAction(@Nullable Input input) throws Exception {
        if (input == null) {
            return null;
        }

        // prepare the clone command
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(input.uri)
                .setCredentialsProvider(input.credentialsProvider)
                .setDirectory(input.localPath);

        // actually clone the repo
        Git git = cloneCommand.call();

        // get the status
        Status status = git.status().call();

        // get the remote url
        Collection<Ref> remotes = git.lsRemote().setRemote(Constants.DEFAULT_REMOTE_NAME).call();


        return new LocalRepository(input.localPath, remotes, status);
    }
}
