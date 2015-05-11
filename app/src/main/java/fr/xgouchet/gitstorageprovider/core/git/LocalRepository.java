package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.NonNull;

import org.eclipse.jgit.api.Status;

import java.io.File;

/**
 * Model object holding a reference and state of a local repository
 *
 * @author Xavier Gouchet
 */
public class LocalRepository {

    private final File mFolder;
    private final String mName;
    private Status mStatus;
    private int mAhead, mBehind;

    public LocalRepository(final @NonNull File folder,
                           final @NonNull String name,
                           final @NonNull Status status) {
        mFolder = folder;
        mName = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalRepository that = (LocalRepository) o;

        return (mFolder.equals(that.mFolder));
    }

    @Override
    public int hashCode() {
        return mFolder.hashCode();
    }
}
