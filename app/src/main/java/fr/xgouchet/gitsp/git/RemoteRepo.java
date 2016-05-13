package fr.xgouchet.gitsp.git;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public class RemoteRepo {

    private final int serviceId;
    private final String name;
    private final String url;
    private String description;

    public RemoteRepo(final int serviceId,
                      final @NonNull String name,
                      final @NonNull String url) {
        this(serviceId, name, url, null);
    }

    public RemoteRepo(final int serviceId,
                      final @NonNull String name,
                      final @NonNull String url,
                      final @Nullable String description) {
        this.serviceId = serviceId;
        this.name = name;
        this.url = url;
        this.description = description == null ? "" : description;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }


    public String getDescription() {
        return description;
    }
}
