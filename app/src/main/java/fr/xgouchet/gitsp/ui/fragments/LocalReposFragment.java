package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.agera.Mergers;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.List;

import fr.xgouchet.gitsp.GitSPApplication;
import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitsp.git.LocalReposSupplier;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleFabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulFragment;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends StatefulFragment implements Updatable {


    private RefreshObservable refreshObservable;
    private Repository<Result<List<LocalRepo>>> localReposRepository;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRepositories();
    }

    @Override
    public void onResume() {
        super.onResume();

        localReposRepository.addUpdatable(this);
        refreshObservable.addUpdatable(new Updatable() {
            @Override
            public void update() {
                setCurrentState(StateHolder.LOADING);
            }
        });
        refreshObservable.onRefresh();
    }


    @Override
    public void onPause() {
        super.onPause();
        localReposRepository.removeUpdatable(this);
    }

    @Override
    public void update() {
        Result<List<LocalRepo>> result = localReposRepository.get();

        if (result.isAbsent()) {
            setEmpty();
        } else if (result.failed()) {
            stateDelegate.setFailure(result.failureOrNull());
            setCurrentState(StateHolder.ERROR);
        } else {
            if (result.get().isEmpty()) {
                setEmpty();
            } else {
                setCurrentState(StateHolder.IDEAL);
            }
        }
    }

    private void setEmpty() {
        stateDelegate.setEmptyContent(getString(R.string.empty_local_repos),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository));
        setCurrentState(StateHolder.EMPTY);
    }

    private void setupRepositories() {
        refreshObservable = new RefreshObservable();

        localReposRepository = Repositories
                .repositoryWithInitialValue(Result.<List<LocalRepo>>absent())
                .observe(refreshObservable)
                .onUpdatesPer(500)
                .goTo(((GitSPApplication) getActivity().getApplication()).getBackgroundExecutor())
                .thenGetFrom(new LocalReposSupplier(getActivity().getBaseContext()))
                .notifyIf(Mergers.staticMerger(true))
                .compile();
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
            Toast.makeText(getActivity(), "Cloning Editors (need Credentials)", Toast.LENGTH_SHORT).show();
        }

        @Nullable
        @Override
        public Drawable getFabDrawable(@StateHolder.State int state) {
            return ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_add);
        }
    };

    private final SimpleStateDelegate stateDelegate = new SimpleStateDelegate() {
        @NonNull
        @Override
        public View createIdealView(@NonNull ViewGroup parent) {
            View ideal = LayoutInflater.from(getActivity())
                    .inflate(R.layout.ideal_local_repos, parent, false);

            bind(this, ideal);

            return ideal;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {

        }
    };

}
