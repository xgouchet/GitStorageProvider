package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.account.AccountsManager;
import fr.xgouchet.gitstorageprovider.core.events.AccountsChangedEvent;
import fr.xgouchet.gitstorageprovider.ui.activities.LoginActivity;
import fr.xgouchet.gitstorageprovider.ui.adapters.AccountsAdapter;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

import static butterknife.ButterKnife.bind;

/**
 * This fragment displays the accounts the user is logged into
 *
 * @author Xavier Gouchet
 */
public class AccountsFragment extends Fragment {

    private DoubleDeckerBus mBus;
    private AccountsManager mAccountsManager;

    @BindView(android.R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    private AccountsAdapter mAccountsAdapter;
    private final List<OAuthAccount> mAccounts = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common managers
        GitApplication app = (GitApplication) getActivity().getApplication();
        mBus = app.getBus();
        mAccountsManager = app.getAccountsManager();

        // create adapter
        mAccountsAdapter = new AccountsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ideal_local_repos, container, false);
        bind(this, root);

        // set recycler view layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAccountsAdapter);
        // TODO setup empty view

        // attach FAB to the recycler view
        mFAB.setOnClickListener(mFABOnClickListener);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // register our event handler
        mBus.register(mEventHandler);

        mAccountsManager.refreshAvailableAccounts();
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister our event handler
        mBus.unregister(mEventHandler);
    }

    private final View.OnClickListener mFABOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Add more services (bitbucket, sourceforge, googlecode, ...)
            // TODO Add service selector
            Toast.makeText(getActivity(), "Github OAuth", Toast.LENGTH_SHORT).show();
            LoginActivity.loginWithService(getActivity(),
                    OAuthConfigFactory.SERVICE_GITHUB);
        }
    };


    private final Object mEventHandler = new Object() {
        @Subscribe
        public void onAccountsChanged(final @NonNull AccountsChangedEvent event) {
            mAccountsAdapter.setAccounts(event.getAccounts());
        }
    };

}
