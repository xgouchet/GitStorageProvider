package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public interface FabDelegate {
    /**
     * Called when the user pressed the FAB
     *
     * @param state the current state
     */
    void onFabClicked(@StateHolder.State int state);

    /**
     * @param state the current state of the fragment
     * @return whether the FAB should be visible
     */
    int getFabVisibility(@StateHolder.State int state);

    /**
     * @param state the current state of the fragment
     * @return the drawable to display (default is a '+')
     */
    @Nullable
    Drawable getFabDrawable(@StateHolder.State int state);
}
