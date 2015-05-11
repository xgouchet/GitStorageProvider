package fr.xgouchet.gitstorageprovider.core.actions;

/**
 * @param <I> the action input type
 * @param <O> the expected output type
 * @author Xavier Gouchet
 */
public interface AsyncActionListener<I, O> {

    /**
     * Called when the action is done. This will always be called on the main thread
     *
     * @param output the result of the action
     */
    void onActionPerformed(O output);

    /**
     * Called when the action failed and threw and excetion.
     * This will always be called on the main thread
     *
     * @param e the exception thrown
     */
    void onActionFailed(I input, Exception e);
}
