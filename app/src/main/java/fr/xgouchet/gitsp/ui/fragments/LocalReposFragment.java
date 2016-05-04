package fr.xgouchet.gitsp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.List;
import java.util.concurrent.ExecutorService;

import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitsp.git.LocalReposSupplier;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitsp.git.Credential;

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

    @Override
    protected void onFabClicked(@State int state) {
        Toast.makeText(getActivity(), "Cloning Editors (need Credentials)", Toast.LENGTH_SHORT).show();
    }

    /*
     * CUSTOMIZATION
     */

    @NonNull
    @Override
    protected View createEmptyView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.empty_default, parent, false);
    }

    @Override
    protected void updateEmptyView(@NonNull View emptyView) {
        TextView emptyText = (TextView) emptyView.findViewById(android.R.id.message);
        if (emptyText == null) {
            return;
        }

        emptyText.setText(R.string.empty_local_repos);
        emptyText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository), null, null);
    }

    @NonNull
    @Override
    protected View createLoadingView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.loading_default, parent, false);
    }

    @NonNull
    @Override
    protected View createErrorView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(getActivity())
                .inflate(R.layout.error_default, parent, false);
    }

    @Override
    protected void updateErrorView(@NonNull View errorView) {
        TextView errorText = (TextView) errorView.findViewById(android.R.id.message);
        if (errorText == null) {
            return;
        }

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable failure = localReposRepository.get().failureOrNull();

        if (failure == null) {
            errorText.setText(R.string.error_empty_failure);
        } else {
            errorText.setText(failure.getMessage());
        }
        errorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository), null, null);
    }

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
