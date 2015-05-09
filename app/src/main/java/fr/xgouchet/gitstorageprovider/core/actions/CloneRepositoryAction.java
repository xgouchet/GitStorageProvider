package fr.xgouchet.gitstorageprovider.core.actions;

import android.support.annotation.Nullable;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;

/**
 * @author Xavier Gouchet
 */
public class CloneRepositoryAction implements AsyncAction<CloneRepositoryAction.Input, Git> {

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
    public Git performAction(@Nullable Input input) throws Exception {
        if (input == null) {
            return null;
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(input.uri)
                .setCredentialsProvider(input.credentialsProvider)
                .setDirectory(input.localPath);

        return cloneCommand.call();
    }
}
