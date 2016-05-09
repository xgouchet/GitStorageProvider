package fr.xgouchet.gitsp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.agera.Mergers;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.List;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.git.Credential;
import fr.xgouchet.gitsp.git.CredentialsSupplier;

/**
 * @author Xavier Gouchet
 */
public class CredentialsFragment extends AStatefulFragment implements Updatable {


    private RefreshObservable refreshObservable;
    private Repository<Result<List<Credential>>> credentialsRepository;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRepositories();
    }

    @Override
    public void onResume() {
        super.onResume();

        credentialsRepository.addUpdatable(this);
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
        credentialsRepository.removeUpdatable(this);
    }


    @Override
    public void update() {
        Result<List<Credential>> result = credentialsRepository.get();

        if (result.isAbsent()) {
            setEmptyState(getString(R.string.empty_credentials),
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_credentials));
        } else if (result.failed()) {
            setErrorState(result.failureOrNull());
        } else {
            if (result.get().isEmpty()) {
                setEmptyState(getString(R.string.empty_credentials),
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_credentials));
            } else {
                setIdealState();
            }
        }
    }

    private void setupRepositories() {
        refreshObservable = new RefreshObservable();

        credentialsRepository = Repositories
                .repositoryWithInitialValue(Result.<List<Credential>>absent())
                .observe(refreshObservable)
                .onUpdatesPer(500)
                .goTo(getBackgroundExecutor())
                .thenGetFrom(new CredentialsSupplier(getActivity().getBaseContext()))
                .notifyIf(Mergers.staticMerger(true))
                .compile();
    }

    /*
     * CUSTOMIZATION
     */

    @NonNull
    @Override
    protected View createIdealView(@NonNull ViewGroup parent) {
        return new View(getActivity());
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
