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
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthAccountsSupplier;

/**
 * @author Xavier Gouchet
 */
public class AccountsFragment extends AStatefulFragment implements Updatable {


    private RefreshObservable refreshObservable;
    private Repository<Result<List<OAuthAccount>>> accountsRepository;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRepositories();
    }

    @Override
    public void onResume() {
        super.onResume();

        accountsRepository.addUpdatable(this);
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
        accountsRepository.removeUpdatable(this);
    }


    @Override
    public void update() {
        Result<List<OAuthAccount>> result = accountsRepository.get();

        if (result.isAbsent()) {
            setEmptyState(getString(R.string.empty_accounts),
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_accounts));
        } else if (result.failed()) {
            setErrorState(result.failureOrNull());
        } else {
            if (result.get().isEmpty()) {
                setEmptyState(getString(R.string.empty_accounts),
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_accounts));
            } else {
                setIdealState();
            }
        }
    }

    private void setupRepositories() {
        refreshObservable = new RefreshObservable();

        accountsRepository = Repositories
                .repositoryWithInitialValue(Result.<List<OAuthAccount>>absent())
                .observe(refreshObservable)
                .onUpdatesPer(500)
                .goTo(getBackgroundExecutor())
                .thenGetFrom(new OAuthAccountsSupplier(getActivity().getBaseContext()))
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
