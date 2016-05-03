package fr.xgouchet.gitsp.ui.fragments;

import android.support.v4.widget.SwipeRefreshLayout;

import com.google.android.agera.BaseObservable;

/**
 * @author Xavier Gouchet
 */
class RefreshObservable extends BaseObservable implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    public void onRefresh() {
        dispatchUpdate();
    }
}
