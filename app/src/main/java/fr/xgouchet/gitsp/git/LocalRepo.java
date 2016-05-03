package fr.xgouchet.gitsp.git;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

/**
 * Model object holding a reference and state of a local repository
 *
 * @author Xavier Gouchet
 */
public class LocalRepo {

    private final File mFolder;
    private final String mName;
    private final Collection<Ref> mRemote;
    private Status mStatus;
    private int mAhead, mBehind;

    public LocalRepo(File folder) {
        this(folder, Collections.<Ref>emptyList(), null);
    }

    public LocalRepo(final @NonNull File folder,
                     final @NonNull Collection<Ref> remote,
                     final @Nullable Status status) {
        mFolder = folder;
        mName = folder.getName();
        mRemote = remote;
    }

    public void setStatus(@NonNull Status status) {
        mStatus = status;
    }

    public String getName() {
        return mName;
    }

    public Status getStatus() {
        return mStatus;
    }

    public File getFolder() {
        return mFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalRepo that = (LocalRepo) o;

        return (mFolder.equals(that.mFolder));
    }

    @Override
    public int hashCode() {
        return mFolder.hashCode();
    }

}
