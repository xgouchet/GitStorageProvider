package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthAccountStore;
import fr.xgouchet.gitsp.oauth.config.OAuthConfigFactory;
import fr.xgouchet.gitsp.ui.adapters.ListRV;
import fr.xgouchet.gitsp.ui.adapters.TextIconViewHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.FabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleFabDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.SimpleStateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateDelegate;
import fr.xgouchet.gitsp.ui.fragments.stateful.StateHolder;
import fr.xgouchet.gitsp.ui.fragments.stateful.StatefulFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Xavier Gouchet
 */
public class AccountsFragment extends StatefulFragment {

    public static final String TAG = AccountsFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        stateDelegate.setLoading("");
        setCurrentState(StateHolder.LOADING);
        oAuthAccountAdapter.clear();
        Observable.create(new OAuthAccountStore.Observable(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oAuthAccountObserver);
    }

    private void setEmpty() {
        stateDelegate.setEmpty(getString(R.string.empty_accounts),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_accounts));
        setCurrentState(StateHolder.EMPTY);
    }

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

    private final Observer<OAuthAccount> oAuthAccountObserver = new Observer<OAuthAccount>() {

        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted");
            if (oAuthAccountAdapter.getItemCount() == 0) {
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
        public void onNext(OAuthAccount oAuthAccount) {
            Log.i(TAG, "onNext → " + oAuthAccount);
            if (oAuthAccount != null) {
                oAuthAccountAdapter.addItem(oAuthAccount);
                setCurrentState(StateHolder.IDEAL);
            }
        }
    };

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
            View ideal = LayoutInflater.from(getActivity())
                    .inflate(R.layout.ideal_list, parent, false);

            RecyclerView recyclerView = (RecyclerView) ideal.findViewById(android.R.id.list);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(oAuthAccountAdapter);

            return ideal;
        }

        @Override
        public void updateIdealView(@NonNull View idealView) {
        }
    };

    private final ListRV.Adapter<OAuthAccount> oAuthAccountAdapter = new ListRV.Adapter<OAuthAccount>() {
        @Override
        public ListRV.ViewHolder<OAuthAccount> onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_icon, parent, false);
            return new OAuthAccountViewHolder(itemView);
        }
    };

    private static class OAuthAccountViewHolder extends TextIconViewHolder<OAuthAccount> {

        public OAuthAccountViewHolder(View itemView) {
            super(itemView);
        }

        @Nullable
        @Override
        protected String getText(OAuthAccount item, int position) {
            return item.getUserName();
        }

        @Override
        protected int getIcon(OAuthAccount item, int position) {
            switch (item.getServiceId()) {
                case OAuthConfigFactory.SERVICE_GITHUB:
                    return R.mipmap.ic_account_github;
                case OAuthConfigFactory.SERVICE_NONE:
                default:
                    return R.drawable.ic_empty;
            }
        }
    }
}
