package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author Xavier Gouchet
 */
public class NoFabDelegate implements FabDelegate {

    @Override
    public void onFabClicked(@StateHolder.State int state) {
    }

    @Override
    public int getFabVisibility(@StateHolder.State int state) {
        return View.GONE;
    }

    @Nullable
    @Override
    public Drawable getFabDrawable(@StateHolder.State int state) {
        return null;
    }
}
