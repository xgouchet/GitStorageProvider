package fr.xgouchet.gitsp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitsp.git.LocalReposSupplier;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends AStatefulFragment implements Updatable {


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
                setLoadingState();
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
            setEmptyState(getString(R.string.empty_local_repos),
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository));
        } else if (result.failed()) {
            setErrorState(result.failureOrNull());
        } else {
            if (result.get().isEmpty()) {
                setEmptyState(getString(R.string.empty_local_repos),
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository));
            } else {
                setIdealState();
            }
        }
    }

    private void setupRepositories() {
        refreshObservable = new RefreshObservable();

        localReposRepository = Repositories
                .repositoryWithInitialValue(Result.<List<LocalRepo>>absent())
                .observe(refreshObservable)
                .onUpdatesPer(500)
                .goTo(getBackgroundExecutor())
                .thenGetFrom(new LocalReposSupplier(getActivity().getBaseContext()))
                .notifyIf(Mergers.staticMerger(true))
                .compile();
    }

    @Override
    protected void onFabClicked(@State int state) {
        Toast.makeText(getActivity(), "Cloning Editors (need Credentials)", Toast.LENGTH_SHORT).show();
    }

    /*
     * CUSTOMIZATION
     */

    @NonNull
    @Override
    protected View createIdealView(@NonNull ViewGroup parent) {
        View ideal = LayoutInflater.from(getActivity())
                .inflate(R.layout.ideal_local_repos, parent, false);

        bind(this, ideal);

        return ideal;
    }

    @Override
    protected int getFabVisibility(@State int state) {
        switch (state) {
            case AStatefulFragment.EMPTY:
            case AStatefulFragment.ERROR:
            case AStatefulFragment.IDEAL:
                return View.VISIBLE;
            case AStatefulFragment.LOADING:
            default:
                return View.GONE;
        }
    }
}
