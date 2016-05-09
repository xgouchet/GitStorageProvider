package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Xavier Gouchet
 */
public abstract class StatefulDialogFragment extends DialogFragment {

    @NonNull
    private StateHolder stateHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateHolder = new StateHolder(getStateDelegate(), getFabDelegate());
    }

    @NonNull
    protected abstract StateDelegate getStateDelegate();

    @Nullable
    protected abstract FabDelegate getFabDelegate();

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return stateHolder.onCreateView(inflater, container);
    }

    @Override
    public void onDetach() {
        stateHolder.onDetach();
        super.onDetach();
    }

    public void setCurrentState(@StateHolder.State int state) {
        stateHolder.setCurrentState(state);
    }
}
