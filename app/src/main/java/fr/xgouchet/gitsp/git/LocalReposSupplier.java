package fr.xgouchet.gitsp.git;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

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

/**
 * @author Xavier Gouchet
 */
public class LocalReposSupplier implements Supplier<Result<List<LocalRepo>>> {

    private static final String WORKSPACE_NAME = "workspace";

    private static final String TAG = LocalReposSupplier.class.getSimpleName();

    @NonNull
    private final File localWorkspaceRoot;

    public LocalReposSupplier(@NonNull Context context) {

        localWorkspaceRoot = new File(context.getFilesDir(), WORKSPACE_NAME);
        if (!localWorkspaceRoot.exists()) {
            localWorkspaceRoot.mkdirs();
        }
    }

    @NonNull
    @Override
    public Result<List<LocalRepo>> get() {
        List<LocalRepo> results = new ArrayList<>();

        File[] candidates = localWorkspaceRoot.listFiles();
        if (candidates == null) {
            Log.i(TAG, "No candidates");
            return Result.present(results);
        }

        for (File candidate : candidates) {
            Log.i(TAG, "Candidate : " + candidate.getAbsolutePath());
            LocalRepo repo;

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

        return Result.present(results);
    }

    @Nullable
    private LocalRepo getLocalRepository(final @NonNull File candidate)
            throws IOException, GitAPIException {

        // create a git instance in the given repository
        Git git = Git.open(candidate);

        // get the status
        Status status = git.status().call();

        // get the remote url
        Collection<Ref> remotes = git.lsRemote().setRemote(Constants.DEFAULT_REMOTE_NAME).call();

        return new LocalRepo(candidate, remotes, status);
    }
}
