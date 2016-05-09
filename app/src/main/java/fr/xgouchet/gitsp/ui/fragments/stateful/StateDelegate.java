package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author Xavier Gouchet
 */
public interface StateDelegate {

    /**
     * Called once, the first time the empty view is required
     *
     * @param parent the parent view
     * @return the view to display when the current fragment's state is empty
     */
    @NonNull
    View createEmptyView(@NonNull ViewGroup parent);

    /**
     * Called every time the empty view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param emptyView the emptyView about to be displayed
     */
    void updateEmptyView(@NonNull View emptyView);

    /**
     * Called once, the first time the loading view is required
     *
     * @param parent the parent view
     * @return the view to display when the current fragment's state is loading
     */
    @NonNull
    View createLoadingView(@NonNull ViewGroup parent);

    /**
     * Called every time the loading view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param loadingView the view about to be displayed
     */
    void updateLoadingView(@NonNull View loadingView);

    /**
     * Called once, the first time the error view is required
     *
     * @param parent the parent view
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    View createErrorView(@NonNull ViewGroup parent);

    /**
     * Called every time the error view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param errorView the view about to be displayed
     */
    void updateErrorView(@NonNull View errorView);

    /**
     * Called once, the first time the error view is required
     *
     * @param parent the parent view
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    View createIdealView(@NonNull ViewGroup parent);

    /**
     * Called every time the ideal view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param idealView the view about to be displayed
     */
    void updateIdealView(@NonNull View idealView);
}
