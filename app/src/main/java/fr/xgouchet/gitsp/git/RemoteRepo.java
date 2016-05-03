package fr.xgouchet.gitsp.git;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class RemoteRepo {

    private final int mServiceId;
    private final String mName;
    private final String mUrl;

    public RemoteRepo(final int serviceId,
                      final @NonNull String name,
                      final @NonNull String url) {
        mServiceId = serviceId;
        mName = name;
        mUrl = url;
    }
}
