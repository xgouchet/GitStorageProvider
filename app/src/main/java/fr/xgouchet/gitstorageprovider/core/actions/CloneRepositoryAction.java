package fr.xgouchet.gitstorageprovider.core.actions;

import android.support.annotation.Nullable;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;

import fr.xgouchet.gitstorageprovider.core.git.LocalRepository;

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

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(input.uri)
                .setCredentialsProvider(input.credentialsProvider)
                .setDirectory(input.localPath);

        Git git = cloneCommand.call();
        Status status = git.status().call();

        return new LocalRepository(input.localPath, input.localPath.getName(), status);
    }
}
