package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitstorageprovider.core.account.AccountsManager;
import fr.xgouchet.gitstorageprovider.core.events.LocalRepositoriesChangedEvent;
import fr.xgouchet.gitstorageprovider.core.events.NavigationEvent;
import fr.xgouchet.gitstorageprovider.core.git.LocalRepositoriesManager;
import fr.xgouchet.gitstorageprovider.ui.adapters.LocalRepositoriesAdapter;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

import static butterknife.ButterKnife.bind;

/**
 * This fragment displays the local mLocalRepositories, and allow local actions :
 * Clone, delete, pull, commit, push
 *
 * @author Xavier Gouchet
 */
public class LocalRepositoriesFragment extends Fragment {

    private static final String TAG = LocalRepositoriesFragment.class.getSimpleName();
    private DoubleDeckerBus mBus;

    @BindView(android.R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    private LocalRepositoriesManager mLocalRepositoriesManager;
    private AccountsManager mAccountsManager;

    private LocalRepositoriesAdapter mLocalRepositoriesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common managers
        GitApplication app = (GitApplication) getActivity().getApplication();
        mBus = app.getBus();
        mLocalRepositoriesManager = app.getLocalRepositoriesManager();
        mAccountsManager = app.getAccountsManager();

        //
        mLocalRepositoriesAdapter = new LocalRepositoriesAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ideal_local_repos, container, false);
        bind(this, root);

        // set recycler view layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mLocalRepositoriesAdapter);
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

        // fetch all repos
        mLocalRepositoriesManager.listLocalRepositories();
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

//            Toast.makeText(getActivity(), "Cloning Editors (need Credentials", Toast.LENGTH_SHORT).show();
//            String repoHttps = "https://github.com/xgouchet/Editors.git";
//            String repoGit = "git://github.com/xgouchet/Editors.git";
//            String repoSsh = "git@github.com:xgouchet/Editors.git";
//            mLocalRepositoriesManager.cloneRepositoryAsync("Editors", repoGit);

            List<OAuthAccount> accounts = mAccountsManager.getAccounts();
            if (accounts.size() == 0) {
                Toast.makeText(getActivity(), "You need to set up an account and add credentials", Toast.LENGTH_SHORT).show();
                mBus.postOnUiThread(new NavigationEvent(NavigationEvent.NAV_ACCOUNT));
            } else {

                // TODO check credentials
                // TODO display all remote repositories
            }
        }
    };

    private Object mEventHandler = new Object() {
        @Subscribe
        public void onLocalRepositoriesChanged(LocalRepositoriesChangedEvent event) {
            Log.i(TAG, "onLocalRepositoriesChanged");
            mLocalRepositoriesAdapter.setLocalRepositories(event.getLocalRepositories());
        }
    };
}
