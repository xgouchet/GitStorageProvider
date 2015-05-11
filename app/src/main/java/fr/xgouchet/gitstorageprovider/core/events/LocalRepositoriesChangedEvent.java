package fr.xgouchet.gitstorageprovider.core.events;

import java.util.List;
import java.util.Set;

import fr.xgouchet.gitstorageprovider.core.git.LocalRepository;

/**
 * @author Xavier Gouchet
 */
public class LocalRepositoriesChangedEvent {

    private final List<LocalRepository> mLocalRepositories;

    public LocalRepositoriesChangedEvent(List<LocalRepository> localRepositories) {
        mLocalRepositories = localRepositories;
    }

    public List<LocalRepository> getLocalRepositories() {
        return mLocalRepositories;
    }
}
