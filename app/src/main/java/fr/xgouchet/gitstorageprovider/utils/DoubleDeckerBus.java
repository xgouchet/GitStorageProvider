package fr.xgouchet.gitstorageprovider.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * An extension to the Otto event bus class to allow event to be posted on the main thread.
 * @author Xavier Gouchet
 */
public class DoubleDeckerBus extends Bus {

    private final Handler mainThread = new Handler(Looper.getMainLooper());

    /**
     * Creates a new Bus named "default" with no thread enforcer for actions.
     */
    public DoubleDeckerBus() {
        super(ThreadEnforcer.ANY);
    }

    /**
     * Creates a new Bus with no thread enforcer for actions and the given identifier.
     *
     * @param identifier A brief name for this bus, for debugging purposes. Should be a valid Java identifier.
     */
    public DoubleDeckerBus(String identifier) {
        super(ThreadEnforcer.ANY, identifier);
    }

    /**
     * Posts an event to all registered handlers, making sure the event is sent on the main thread.
     * This method will return successfully.
     * <p/>
     * If no handlers have been subscribed for event's class, and event is not already a DeadEvent, it will be wrapped in a DeadEvent and reposted.
     *
     * @param event event to post.
     * @throws NullPointerException if the event is null.
     */
    public void postOnUiThread(final Object event) {

        if (event == null) {
            throw new NullPointerException("Event to post must not be null.");
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }
    }
}
