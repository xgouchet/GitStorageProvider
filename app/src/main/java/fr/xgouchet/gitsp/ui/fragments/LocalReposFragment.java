package fr.xgouchet.gitsp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitsp.git.LocalReposSupplier;
import fr.xgouchet.gitstorageprovider.R;

import static butterknife.ButterKnife.bind;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends AStatefulFragment implements Updatable {

    private static final ExecutorService NETWORK_EXECUTOR = newSingleThreadExecutor();


    private RefreshObservable refreshObservable;
    private LocalReposSupplier localReposSupplier;
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
                setCurrentState(LOADING);
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
            setCurrentState(EMPTY);
        } else if (result.failed()) {
            setCurrentState(ERROR);
        } else {
            if (result.get().isEmpty()) {
                setCurrentState(EMPTY);
            } else {
                setCurrentState(IDEAL);
            }
        }
    }

    @NonNull
    @Override
    protected View createIdealView() {
        View ideal = LayoutInflater.from(getActivity())
                .inflate(R.layout.ideal_local_repos, container, false);

        bind(this, ideal);

        return ideal;
    }

    private void setupRepositories() {
        refreshObservable = new RefreshObservable();
        localReposSupplier = new LocalReposSupplier(getActivity().getBaseContext());

        localReposRepository = Repositories
                .repositoryWithInitialValue(Result.<List<LocalRepo>>absent())
                .observe(refreshObservable)
                .onUpdatesPerLoop()
                .goTo(NETWORK_EXECUTOR)
                .thenGetFrom(localReposSupplier)
                .compile();

    }

}
