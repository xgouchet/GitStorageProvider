package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitsp.git.LocalReposObservable;
import fr.xgouchet.gitsp.ui.adapters.ListRV;
import fr.xgouchet.gitsp.ui.adapters.TextIconViewHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleFabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends StatefulFragment {

    public static final String TAG = LocalReposFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();

        stateDelegate.setLoading("");
        setCurrentState(StateHolder.LOADING);

        localRepoAdapter.clear();
        Observable.create(new LocalReposObservable(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(localRepoObserver);
    }


    private void setEmpty() {
        stateDelegate.setEmpty(getString(R.string.empty_local_repos),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository));
        setCurrentState(StateHolder.EMPTY);
    }

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

    private final Observer<LocalRepo> localRepoObserver = new Observer<LocalRepo>() {

        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted");
            if (localRepoAdapter.getItemCount() == 0) {
                setEmpty();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.w(TAG, "onError", e);
            stateDelegate.setFailure(e);
            setCurrentState(StateHolder.ERROR);
        }

        @Override
        public void onNext(LocalRepo localRepo) {
            Log.i(TAG, "onNext → " + localRepo);
            if (localRepo != null) {
                localRepoAdapter.addItem(localRepo);
                setCurrentState(StateHolder.IDEAL);
            }
        }
    };

    private final FabDelegate fabDelegate = new SimpleFabDelegate() {
        @Override
        public void onFabClicked(@StateHolder.State int state) {
            Toast.makeText(getActivity(), "Cloning Editors (need Credentials)", Toast.LENGTH_SHORT).show();

//            Toast.makeText(getActivity(), "Cloning Editors (need Credentials", Toast.LENGTH_SHORT).show();
//            String repoHttps = "https://github.com/xgouchet/Editors.git";
//            String repoGit = "git://github.com/xgouchet/Editors.git";
//            String repoSsh = "git@github.com:xgouchet/Editors.git";
//            mLocalRepositoriesManager.cloneRepositoryAsync("Editors", repoGit);

//            List<OAuthAccount> accounts = mAccountsManager.getAccounts();
//            if (accounts.size() == 0) {
//                Toast.makeText(getActivity(), "You need to set up an account and add credentials", Toast.LENGTH_SHORT).show();
//                mBus.postOnUiThread(new NavigationEvent(NavigationEvent.NAV_ACCOUNT));
//            } else {
//
//                // TODO check credentials
//                // TODO display all remote repositories
//            }
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
                    .inflate(R.layout.ideal_list, parent, false);

            bind(this, ideal);

            return ideal;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {

        }
    };

    private final ListRV.Adapter<LocalRepo> localRepoAdapter = new ListRV.Adapter<LocalRepo>() {
        @Override
        public ListRV.ViewHolder<LocalRepo> onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_icon, parent, false);
            return new LocalRepoViewHolder(itemView);
        }
    };

    static class LocalRepoViewHolder extends TextIconViewHolder<LocalRepo> {

        public LocalRepoViewHolder(View itemView) {
            super(itemView);
        }

        @Nullable
        @Override
        protected String getText(LocalRepo item, int position) {
            return item.getName();
        }

        @Override
        protected int getIcon(LocalRepo item, int position) {
            return R.drawable.ic_local_repository;
        }
    }
}
