package fr.xgouchet.gitsp.ui.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.OnClick;
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

    @BindView(R.id.state_fab)
    FloatingActionButton fab;

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

        prepareFab();

        setCurrentState(EMPTY);
        return root;
    }

    private void prepareFab() {
        //noinspection ResourceType
        fab.setVisibility(getFabVisibility(state));
        fab.setImageDrawable(getFabDrawable(state));
    }

    @OnClick(R.id.state_fab)
    public final void fabClicked() {
        onFabClicked(state);
    }

    /**
     * Called when the user pressed the FAB
     *
     * @param state the current state
     */
    protected void onFabClicked(@State int state) {
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
     * @param state
     * @return whether the FAB should be visible
     */
    protected int getFabVisibility(@State int state) {
        return View.GONE;
    }

    @NonNull
    protected Drawable getFabDrawable(@State int state) {
        return ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_add);
    }

    /**
     * Called once, the first time the empty view is required
     *
     * @param parent the parent view
     * @return the view to display when the current fragment's state is empty
     */
    @NonNull
    protected abstract View createEmptyView(@NonNull ViewGroup parent);

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
     * @param parent the parent view
     * @return the view to display when the current fragment's state is loading
     */
    @NonNull
    protected abstract View createLoadingView(@NonNull ViewGroup parent);

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
     * @param parent the parent view
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    protected abstract View createErrorView(@NonNull ViewGroup parent);

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
     * @param parent the parent view
     * @return the view to display when the current fragment's state is error
     */
    @NonNull
    protected abstract View createIdealView(@NonNull ViewGroup parent);

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
            emptyView = createEmptyView(container);
        }
        updateEmptyView(emptyView);
        return emptyView;
    }

    @NonNull
    private View getLoadingView() {
        if (loadingView == null) {
            loadingView = createLoadingView(container);
        }
        updateLoadingView(loadingView);
        return loadingView;
    }


    @NonNull
    private View getErrorView() {
        if (errorView == null) {
            errorView = createErrorView(container);
        }
        updateErrorView(errorView);
        return errorView;
    }

    @NonNull
    private View getIdealView() {
        if (idealView == null) {
            idealView = createIdealView(container);
        }
        updateIdealView(idealView);
        return idealView;
    }


}
