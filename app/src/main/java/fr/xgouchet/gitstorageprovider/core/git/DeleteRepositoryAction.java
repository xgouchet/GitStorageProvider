package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import fr.xgouchet.gitstorageprovider.utils.actions.AsyncVoidAction;

/**
 * @author Xavier Gouchet
 */
public class DeleteRepositoryAction extends AsyncVoidAction<LocalRepository> {

    @Override
    public void performVoidAction(final @NonNull LocalRepository input) throws Exception {
        File repo = input.getFolder();
        if (repo.isDirectory()) {
            deleteRecursive(repo);
        }
    }

    private void deleteRecursive(final @NonNull File directory) {
        File[] children = directory.listFiles();

        if (children != null) {
            for (File child : children) {
                if (child.isDirectory()) {
                    deleteRecursive(child);
                } else {
                    child.delete();
                }
            }
        }

        directory.delete();
    }
}
