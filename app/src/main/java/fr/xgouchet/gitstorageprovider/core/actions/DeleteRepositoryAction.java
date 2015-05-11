package fr.xgouchet.gitstorageprovider.core.actions;

import android.support.annotation.Nullable;

import java.io.File;

import fr.xgouchet.gitstorageprovider.core.git.LocalRepository;

/**
 * @author Xavier Gouchet
 */
public class DeleteRepositoryAction extends AsyncVoidAction<LocalRepository> {

    @Override
    public void performVoidAction(@Nullable LocalRepository input) throws Exception {
        if (input == null) {
            return;
        }

        File repo = input.getFolder();
        if (repo.isDirectory()) {
            deleteRecursive(repo);
        }
    }

    private void deleteRecursive(File directory) {
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
