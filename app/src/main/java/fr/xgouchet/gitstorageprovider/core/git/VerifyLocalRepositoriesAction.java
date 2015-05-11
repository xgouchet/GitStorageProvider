package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.xgouchet.gitstorageprovider.utils.actions.AsyncAction;

/**
 * An action that will check each subfolders in the given folder, and return a list of
 * LocalRepository objects properly filled
 *
 * @author Xavier Gouchet
 */
public class VerifyLocalRepositoriesAction implements AsyncAction<File, List<LocalRepository>> {

    private static final String TAG = VerifyLocalRepositoriesAction.class.getSimpleName();

    @Nullable
    @Override
    public List<LocalRepository> performAction(@Nullable File input) throws Exception {
        List<LocalRepository> results = new ArrayList<>();

        if ((input == null) || (input.isFile())) {
            Log.w(TAG, "input is null, no result");
            return results;
        }

        File[] candidates = input.listFiles();
        if (candidates == null) {
            Log.i(TAG, "No candidates");
            return results;
        }

        for (File candidate : candidates) {
            Log.i(TAG, "Candidate : " + candidate.getAbsolutePath());
            LocalRepository repo;

            try {
                repo = getLocalRepository(candidate);
            } catch (IOException e) {
                continue;
            } catch (GitAPIException e) {
                continue;
            }

            if (repo != null) {
                results.add(repo);
            }
        }

        return results;
    }

    @Nullable
    private LocalRepository getLocalRepository(File candidate) throws IOException, GitAPIException {

        // create a git instance in the given repository
        Git git = Git.open(candidate);

        // get the status
        Status status = git.status().call();

        // get the remote url
        Collection<Ref> remotes = git.lsRemote().setRemote(Constants.DEFAULT_REMOTE_NAME).call();

        return new LocalRepository(candidate, remotes, status);
    }
}
