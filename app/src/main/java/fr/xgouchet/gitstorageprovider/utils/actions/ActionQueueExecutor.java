package fr.xgouchet.gitstorageprovider.utils.actions;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * The ActionQueueExecutor keeps a queue of actions and performs them one by one on a background thread.
 * Once the action is performed, it notifies the listener
 *
 * @author Xavier Gouchet
 */
public class ActionQueueExecutor {

    private static final int WHAT_ACTION_PERFORMED = 0xACED;
    private static final String TAG = ActionQueueExecutor.class.getSimpleName();

    // TODO add constructor param for thread names
    private final Executor mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(final @NonNull Runnable runnable) {
            return new Thread(runnable, "ActionQueueWorker");
        }
    });

    private final Handler mHandler;

    /**
     * Creates an instance, with all listener callback performed on the main thread
     */
    public ActionQueueExecutor() {
        this(Looper.getMainLooper());
    }

    /**
     * Creates an instance, with all listener callback performed on the given looper
     */
    public ActionQueueExecutor(final @NonNull Looper looper) {
        mHandler = new AsyncActionHandler(looper);
    }

    /**
     * Adds an action to the queue to be performed in the background
     *
     * @param action the action to perform
     */
    public <I, O> void queueAction(final @NonNull AsyncAction<I, O> action,
                                   final @Nullable I input,
                                   final @NonNull AsyncActionListener<I, O> listener) {
        Log.d(TAG, "Queue action " + action);
        mExecutor.execute(new AsyncActionRunnable<>(action, input, listener, mHandler));
    }

    /**
     * A Runnable which performs the Action.
     *
     * @param <I> the input type
     * @param <O> the output type
     */
    private static final class AsyncActionRunnable<I, O> implements Runnable {

        private final AsyncAction<I, O> mAction;
        private final Handler mHandler;
        private final I mInput;
        private final AsyncActionListener<I, O> mListener;
        private O mOutput;
        private Exception mException;

        private AsyncActionRunnable(final @NonNull AsyncAction<I, O> action,
                                    final @Nullable I input,
                                    final @NonNull AsyncActionListener<I, O> listener,
                                    final @NonNull Handler handler) {
            mAction = action;
            mInput = input;
            mListener = listener;
            mHandler = handler;

        }

        @Override
        public void run() {
            Log.d(TAG, "Perform action " + mAction);
            try {
                mOutput = mAction.performAction(mInput);
                Log.d(TAG, "Action successfull : " + mInput + " -> " + mOutput);
            } catch (Exception e) {
                mException = e;
                Log.w(TAG, "Action failed : " + mInput + " -> " + mException);
            }

            // notify target
            Log.d(TAG, "Notify handler");
            Message msg = mHandler.obtainMessage(WHAT_ACTION_PERFORMED);
            msg.obj = this;
            msg.sendToTarget();
        }

        /**
         * Notifies the listener of the success or failure of the operation
         */
        void notifyListener() {
            Log.d(TAG, "notifyListener()");
            if (mException == null) {
                mListener.onActionPerformed(mOutput);
            } else {
                mListener.onActionFailed(mInput, mException);
            }
        }
    }

    /**
     * The handler class to notify the listeners for each AsyncAction we treated
     */
    private static class AsyncActionHandler extends Handler {

        /**
         * Creates an Handler living in the given Looper
         *
         * @param looper the looper to use (NonNull)
         */
        public AsyncActionHandler(final @NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final @NonNull Message msg) {
            Log.d(TAG, "handleMessage(" + msg.what + ")");
            switch (msg.what) {
                case WHAT_ACTION_PERFORMED:
                    AsyncActionRunnable action = (AsyncActionRunnable) msg.obj;
                    Log.d(TAG, "Notify listener for action " + action);
                    action.notifyListener();
                    break;
            }
        }
    }
}