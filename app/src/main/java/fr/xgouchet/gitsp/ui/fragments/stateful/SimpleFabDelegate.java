package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.view.View;

/**
 * @author Xavier Gouchet
 */
public abstract class SimpleFabDelegate implements FabDelegate {

    @Override
    public int getFabVisibility(@StateHolder.State int state) {
        switch (state) {
            case StateHolder.EMPTY:
            case StateHolder.ERROR:
            case StateHolder.IDEAL:
                return View.VISIBLE;
            case StateHolder.LOADING:
            default:
                return View.GONE;
        }
    }


}
