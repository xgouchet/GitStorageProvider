package fr.xgouchet.gitstorageprovider.utils.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A shortcut for AsyncAction returning Void
 *
 * @author Xavier Gouchet
 */
public abstract class AsyncVoidAction<I> implements AsyncAction<I, Void> {

    /**
     * Preforms an action based on the given input
     *
     * @param input the input
     * @throws Exception
     */
    public abstract void performVoidAction(@NonNull I input) throws Exception;

    @Nullable
    @Override
    public final Void performAction(final @NonNull I input) throws Exception {
        performVoidAction(input);
        return null;
    }
}
