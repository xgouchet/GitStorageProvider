package fr.xgouchet.gitsp.git;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class LocalReposObservable implements Observable.OnSubscribe<LocalRepo> {

    private static final String WORKSPACE_NAME = "workspace";

    private static final String TAG = LocalReposObservable.class.getSimpleName();

    @NonNull
    private final File localWorkspaceRoot;

    public LocalReposObservable(@NonNull Context context) {

        localWorkspaceRoot = new File(context.getFilesDir(), WORKSPACE_NAME);
        if (!localWorkspaceRoot.exists()) {
            localWorkspaceRoot.mkdirs();
        }
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

    @Override
    public void call(Subscriber<? super LocalRepo> subscriber) {
        File[] candidates = localWorkspaceRoot.listFiles();
        if (candidates == null) {
            Log.i(TAG, "No candidates");
            subscriber.onCompleted();
            return;
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
                subscriber.onNext(repo);
            }
        }

        subscriber.onCompleted();
    }
}
