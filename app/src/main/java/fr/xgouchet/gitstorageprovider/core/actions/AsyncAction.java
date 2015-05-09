package fr.xgouchet.gitstorageprovider.core.actions;

import android.support.annotation.Nullable;

/**
 * An Action that will be performed in the background
 *
 * @param <I> the input type
 * @param <O> the output type
 * @author Xavier Gouchet
 */
public interface AsyncAction<I, O> {

    /**
     * Preforms an action based on the given input
     *
     * @param input the input
     * @return the output
     */
    @Nullable
    O performAction(final @Nullable I input) throws Exception;


}
