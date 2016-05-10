package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleFabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulFragment;

/**
 * @author Xavier Gouchet
 */
public class CredentialsFragment extends StatefulFragment  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRepositories();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    public void update() {
//        Result<List<Credential>> result = credentialsRepository.get();
//
//        if (result.isAbsent()) {
//            setEmpty();
//        } else if (result.failed()) {
//            stateDelegate.setFailure(result.failureOrNull());
//            setCurrentState(StateHolder.ERROR);
//        } else {
//            if (result.get().isEmpty()) {
//                setEmpty();
//            } else {
//                setCurrentState(StateHolder.IDEAL);
//            }
//        }
    }

    private void setEmpty() {
        stateDelegate.setEmptyContent(getString(R.string.empty_credentials),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_credentials));
        setCurrentState(StateHolder.EMPTY);
    }

    private void setupRepositories() {
//        refreshObservable = new RefreshObservable();
//
//        credentialsRepository = Repositories
//                .repositoryWithInitialValue(Result.<List<Credential>>absent())
//                .observe(refreshObservable)
//                .onUpdatesPer(500)
//                .goTo(((GitSPApplication) getActivity().getApplication()).getBackgroundExecutor())
//                .thenGetFrom(new CredentialsSupplier(getActivity().getBaseContext()))
//                .notifyIf(Mergers.staticMerger(true))
//                .compile();
    }

    /*
     * CUSTOMIZATION
     */

    @Nullable
    @Override
    public FabDelegate getFabDelegate() {
        return fabDelegate;
    }

    @NonNull
    @Override
    public SimpleStateDelegate getStateDelegate() {
        return stateDelegate;
    }

    private final FabDelegate fabDelegate = new SimpleFabDelegate() {
        @Override
        public void onFabClicked(@StateHolder.State int state) {
            Toast.makeText(getActivity(), "Add credentials (TODO)", Toast.LENGTH_SHORT).show();
        }

        @Nullable
        @Override
        public Drawable getFabDrawable(@StateHolder.State int state) {
            return ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_add_credential);
        }
    };

    private final SimpleStateDelegate stateDelegate = new SimpleStateDelegate() {
        @NonNull
        @Override
        public View createIdealView(@NonNull ViewGroup parent) {
            return new View(getActivity());
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {

        }
    };


}
