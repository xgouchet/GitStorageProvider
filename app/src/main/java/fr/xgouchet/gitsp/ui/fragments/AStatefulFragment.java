package fr.xgouchet.gitsp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import fr.xgouchet.gitstorageprovider.R;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public abstract class AStatefulFragment extends Fragment {

    public static final int EMPTY = 0;
    public static final int LOADING = 1;
    public static final int IDEAL = 2;
    public static final int ERROR = 3;


    @IntDef({EMPTY, LOADING, IDEAL, ERROR})
    public @interface State {
    }

    @State
    private int state;

    @BindView(R.id.state_container)
    FrameLayout container;

    private View emptyView, loadingView, errorView, idealView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stateful, container, false);
        bind(this, root);
        setCurrentState(EMPTY);
        return root;
    }

    /*
     * STATES
     */

    public void setCurrentState(@State int state) {
        if (this.state == state) {
            // already in the correct state, ignore
            return;
        }

        this.state = state;
        switch (state) {
            case EMPTY:
                showView(getEmptyView());
                break;
            case ERROR:
                showView(getErrorView());
                break;
            case IDEAL:
                showView(getIdealView());
                break;
            case LOADING:
                showView(getLoadingView());
                break;
        }
    }

    private void showView(@NonNull View view) {
        container.removeAllViews();
        container.addView(view);
    }

    /*
     * CUSTOMIZATION
     */

    /**
     * Called once, the first time the empty view is required
     *
     * @return the view to display when the current fragment's state is empty
     */
    @NonNull
    protected View createEmptyView() {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.default_empty_view, container, false);
    }

    /**
     * Called every time the empty view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param emptyView the emptyView about to be displayed
     */
    protected void updateEmptyView(@NonNull View emptyView) {
    }

    /**
     * Called once, the first time the loading view is required
     *
     * @return the view to display when the current fragment's state is loading
     */
    @NonNull
    protected View createLoadingView() {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.default_loading_view, container, false);
    }

    /**
     * Called every time the loading view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param loadingView the view about to be displayed
     */
    protected void updateLoadingView(@NonNull View loadingView) {
    }

    /**
     * Called once, the first time the error view is required
     *
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    protected View createErrorView() {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.default_empty_view, container, false);
    }

    /**
     * Called every time the error view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param errorView the view about to be displayed
     */
    protected void updateErrorView(@NonNull View errorView) {
    }

    /**
     * Called once, the first time the error view is required
     *
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    protected abstract View createIdealView();

    /**
     * Called every time the ideal view needs to be displayed.
     * This lets you change some things, like the displayed message
     *
     * @param idealView the view about to be displayed
     */
    protected void updateIdealView(@NonNull View idealView) {
    }

    /*
     * INTERNAL
     */

    @NonNull
    private View getEmptyView() {
        if (emptyView == null) {
            emptyView = createEmptyView();
        }
        updateEmptyView(emptyView);
        return emptyView;
    }

    @NonNull
    private View getLoadingView() {
        if (loadingView == null) {
            loadingView = createLoadingView();
        }
        updateLoadingView(loadingView);
        return loadingView;
    }


    @NonNull
    private View getErrorView() {
        if (errorView == null) {
            errorView = createErrorView();
        }
        updateErrorView(errorView);
        return errorView;
    }

    @NonNull
    private View getIdealView() {
        if (idealView == null) {
            idealView = createErrorView();
        }
        updateIdealView(idealView);
        return idealView;
    }
}
