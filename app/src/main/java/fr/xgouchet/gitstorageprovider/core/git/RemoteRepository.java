package fr.xgouchet.gitstorageprovider.core.git;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class RemoteRepository {

    private final int mServiceId;
    private final String mName;
    private final String mUrl;

    public RemoteRepository(final int serviceId,
                            final @NonNull String name,
                            final @NonNull String url) {
        mServiceId = serviceId;
        mName = name;
        mUrl = url;
    }
}
