package fr.xgouchet.gitsp.oauth;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import java.util.List;

/**
 * @author Xavier Gouchet
 */
public class OAuthAccountsSupplier implements Supplier<Result<List<OAuthAccount>>> {


    private static final String TAG = OAuthAccountsSupplier.class.getSimpleName();

    public OAuthAccountsSupplier(@NonNull Context context) {

    }

    @NonNull
    @Override
    public Result<List<OAuthAccount>> get() {
        return Result.absent();
    }

}
