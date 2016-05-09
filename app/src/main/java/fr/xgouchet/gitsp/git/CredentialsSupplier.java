package fr.xgouchet.gitsp.git;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import java.util.List;

/**
 * @author Xavier Gouchet
 */
public class CredentialsSupplier implements Supplier<Result<List<Credential>>> {

    public CredentialsSupplier(Context context) {

    }

    @NonNull
    @Override
    public Result<List<Credential>> get() {
        return Result.absent();
    }
}
