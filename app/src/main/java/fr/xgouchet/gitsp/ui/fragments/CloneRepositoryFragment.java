package fr.xgouchet.gitsp.ui.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.git.RemoteRepo;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthAccountStore;
import fr.xgouchet.gitsp.oauth.config.OAuthConfig;
import fr.xgouchet.gitsp.oauth.config.OAuthConfigFactory;
import fr.xgouchet.gitsp.ui.adapters.ListRV;
import fr.xgouchet.gitsp.ui.adapters.TextIconViewHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulDialogFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Xavier Gouchet
 */
public class CloneRepositoryFragment extends StatefulDialogFragment {

    @Override
    public void onResume() {
        super.onResume();
        setCurrentState(StateHolder.LOADING);

        Observable.create(new OAuthAccountStore.Observable(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oAuthAccountObserver);
    }

    @NonNull
    @Override
    protected StateDelegate getStateDelegate() {
        return stateDelegate;
    }

    @Nullable
    @Override
    protected FabDelegate getFabDelegate() {
        return null;
    }

    private void setEmpty() {
        stateDelegate.setEmpty(getString(R.string.empty_accounts),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_local_repository));
        setCurrentState(StateHolder.EMPTY);
    }

    private final Observer<OAuthAccount> oAuthAccountObserver = new Observer<OAuthAccount>() {

        private final String TAG = CloneRepositoryFragment.class.getSimpleName() + "$oAuthAccountObserver";

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Log.w(TAG, "onError", e);
            stateDelegate.setFailure(e);
            setCurrentState(StateHolder.ERROR);
        }

        @Override
        public void onNext(OAuthAccount oAuthAccount) {
            Log.i(TAG, "onNext → " + oAuthAccount);
            if (oAuthAccount != null) {
                OAuthConfig oAuthConfig = OAuthConfigFactory.getOAuthConfig(oAuthAccount.getServiceId());
                if (oAuthConfig == null) {
                    return;
                }

                Observable.create(oAuthConfig.getRemoteRepoObservable())
                        .subscribeOn(Schedulers.io())
                        .onBackpressureBuffer()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(remoteRepoObserver);
            }
        }
    };

    private final Observer<RemoteRepo> remoteRepoObserver = new Observer<RemoteRepo>() {
        private final String TAG = CloneRepositoryFragment.class.getSimpleName() + "$remoteRepoObserver";

        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted");
            if (remoteRepoAdapter.getItemCount() == 0) {
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
        public void onNext(RemoteRepo remoteRepo) {
            Log.i(TAG, "onNext → " + remoteRepo);
            if (remoteRepo != null) {
                remoteRepoAdapter.addItem(remoteRepo);
                setCurrentState(StateHolder.IDEAL);
            }
        }
    };

    private SimpleStateDelegate stateDelegate = new SimpleStateDelegate() {
        @NonNull
        @Override
        public View createIdealView(@NonNull ViewGroup parent) {
            View ideal = LayoutInflater.from(getActivity())
                    .inflate(R.layout.ideal_list, parent, false);

            RecyclerView recyclerView = (RecyclerView) ideal.findViewById(android.R.id.list);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(remoteRepoAdapter);

            return ideal;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {
        }
    };

    private ListRV.Adapter<RemoteRepo> remoteRepoAdapter = new ListRV.Adapter<RemoteRepo>() {

        @Override
        public ListRV.ViewHolder<RemoteRepo> onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_icon_desc, parent, false);
            return new RemoteRepoViewHolder(itemView);
        }
    };

    private class RemoteRepoViewHolder extends TextIconViewHolder<RemoteRepo> {

        private final TextView description;

        public RemoteRepoViewHolder(View itemView) {
            super(itemView, remoteRepoItemClickListener);
            description = (TextView) itemView.findViewById(android.R.id.summary);
        }

        @Override
        protected void onItemBound(RemoteRepo item, int position) {
            super.onItemBound(item, position);
            description.setText(item.getDescription());
        }

        @Nullable
        @Override
        protected String getText(RemoteRepo item, int position) {
            return item.getName();
        }

        @Override
        protected int getIcon(RemoteRepo item, int position) {
            switch (item.getServiceId()) {
                case OAuthConfigFactory.SERVICE_GITHUB:
                    return R.mipmap.ic_account_github;
                case OAuthConfigFactory.SERVICE_NONE:
                default:
                    return R.drawable.ic_empty;
            }
        }
    }

    private final ListRV.ItemClickListener<RemoteRepo> remoteRepoItemClickListener = new ListRV.ItemClickListener<RemoteRepo>() {
        @Override
        public void onItemClicked(@Nullable RemoteRepo item, int position) {
            // TODO clone !
            if (item != null) {
                Toast.makeText(getActivity(), "Cloning " + item.getName(), Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Cloning null ‽", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
