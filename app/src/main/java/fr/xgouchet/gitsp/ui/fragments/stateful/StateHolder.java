package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.OnClick;
import fr.xgouchet.gitsp.R;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class StateHolder {

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

    @Nullable
    private final FabDelegate fabDelegate;
    @NonNull
    private final StateDelegate stateDelegate;

    public StateHolder(@NonNull StateDelegate stateDelegate, @Nullable FabDelegate fabDelegate) {
        this.stateDelegate = stateDelegate;
        this.fabDelegate = fabDelegate;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container) {

        View root = inflater.inflate(R.layout.fragment_stateful, container, false);
        bind(this, root);

        prepareFab();

        setCurrentState(EMPTY);
        return root;
    }

    public void onDetach() {
        container.removeAllViews();
    }

    @OnClick(R.id.state_fab)
    public final void fabClicked() {
        if (fabDelegate != null) {
            fabDelegate.onFabClicked(state);
        }
    }

    /*
     * STATES
     */

    @UiThread
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

    @UiThread
    private void showView(@NonNull View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) parent.removeView(view);

        container.removeAllViews();
        container.addView(view);
    }

    /*
     * FAB
     */


    private void prepareFab() {
        if (fabDelegate == null) {
            fab.setVisibility(View.GONE);
        } else {
            //noinspection ResourceType
            fab.setVisibility(fabDelegate.getFabVisibility(state));
            fab.setImageDrawable(fabDelegate.getFabDrawable(state));
        }
    }

    /*
     * INTERNAL
     */

    @NonNull
    private View getEmptyView() {
        if (emptyView == null) {
            emptyView = stateDelegate.createEmptyView(container);
        }
        stateDelegate.updateEmptyView(emptyView);
        return emptyView;
    }

    @NonNull
    private View getLoadingView() {
        if (loadingView == null) {
            loadingView = stateDelegate.createLoadingView(container);
        }
        stateDelegate.updateLoadingView(loadingView);
        return loadingView;
    }


    @NonNull
    private View getErrorView() {
        if (errorView == null) {
            errorView = stateDelegate.createErrorView(container);
        }
        stateDelegate.updateErrorView(errorView);
        return errorView;
    }

    @NonNull
    private View getIdealView() {
        if (idealView == null) {
            idealView = stateDelegate.createIdealView(container);
        }
        stateDelegate.updateIdealView(idealView);
        return idealView;
    }


}
