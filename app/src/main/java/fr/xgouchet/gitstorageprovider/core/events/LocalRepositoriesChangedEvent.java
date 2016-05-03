package fr.xgouchet.gitstorageprovider.core.events;

import java.util.List;

import fr.xgouchet.gitsp.git.LocalRepo;

/**
 * @author Xavier Gouchet
 */
public class LocalRepositoriesChangedEvent {

    private final List<LocalRepo> mLocalRepositories;

    public LocalRepositoriesChangedEvent(List<LocalRepo> localRepositories) {
        mLocalRepositories = localRepositories;
    }

    public List<LocalRepo> getLocalRepositories() {
        return mLocalRepositories;
    }
}
