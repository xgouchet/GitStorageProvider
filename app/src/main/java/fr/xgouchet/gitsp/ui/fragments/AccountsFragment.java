package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.agera.Mergers;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.List;

import fr.xgouchet.gitsp.GitSPApplication;
import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthAccountsSupplier;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleFabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulFragment;

/**
 * @author Xavier Gouchet
 */
public class AccountsFragment extends StatefulFragment implements Updatable {


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
                setCurrentState(StateHolder.LOADING);
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
        stateDelegate.setEmptyContent(getString(R.string.empty_accounts),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_accounts));
        setCurrentState(StateHolder.EMPTY);
    }


    private void setupRepositories() {
        refreshObservable = new RefreshObservable();

        accountsRepository = Repositories
                .repositoryWithInitialValue(Result.<List<OAuthAccount>>absent())
                .observe(refreshObservable)
                .onUpdatesPer(500)
                .goTo(((GitSPApplication) getActivity().getApplication()).getBackgroundExecutor())
                .thenGetFrom(new OAuthAccountsSupplier(getActivity().getBaseContext()))
                .notifyIf(Mergers.staticMerger(true))
                .compile();
    }

    /*
     * CUSTOMIZATION
     */

    private final FabDelegate fabDelegate = new SimpleFabDelegate() {
        @Override
        public void onFabClicked(@StateHolder.State int state) {
            DialogFragment fragment = OAuthFragment.withService(OAuthConfigFactory.SERVICE_GITHUB);
            fragment.show(getFragmentManager(), "OAuth");
        }

        @Nullable
        @Override
        public Drawable getFabDrawable(@StateHolder.State int state) {
            return ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_add_acount);
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

    @NonNull
    @Override
    protected StateDelegate getStateDelegate() {
        return stateDelegate;
    }

    @Nullable
    @Override
    protected FabDelegate getFabDelegate() {
        return fabDelegate;
    }
}
