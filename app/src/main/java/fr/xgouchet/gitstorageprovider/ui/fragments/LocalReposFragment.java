package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.actions.ActionQueueExecutor;
import fr.xgouchet.gitstorageprovider.core.actions.AsyncActionListener;
import fr.xgouchet.gitstorageprovider.core.actions.CloneRepositoryAction;
import fr.xgouchet.gitstorageprovider.ui.adapters.LocalReposAdapter;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

import com.melnykov.fab.FloatingActionButton;

import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * This fragment displays the local repositories, and allow local actions :
 * Clone, delete, pull, commit, push
 *
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends Fragment {

    private DoubleDeckerBus mBus;

    @InjectView(android.R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.fab)
    FloatingActionButton mFAB;

    private final ActionQueueExecutor mActionQueueExecutor = new ActionQueueExecutor();

    private RecyclerView.Adapter mLocalReposAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common event bus
        mBus = ((GitApplication) getActivity().getApplication()).getBus();
        mLocalReposAdapter = new LocalReposAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_fab, container, false);
        ButterKnife.inject(this, root);

        // set recycler view layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        llm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mLocalReposAdapter);
        // TODO setup empty view

        // attach FAB to the recycler view
        mFAB.attachToRecyclerView(mRecyclerView);
        mFAB.setOnClickListener(mFABOnClickListener);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // register our event handler
        // TODO mBus.register(mEventHandler);
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister our event handler
        // TODO mBus.unregister(mEventHandler);
    }

    private final View.OnClickListener mFABOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Cloning ViewFieldMatcher", Toast.LENGTH_SHORT).show();

            CloneRepositoryAction.Input input = new CloneRepositoryAction.Input();
            input.uri = "https://github.com/xgouchet/ViewFieldMatcher.git";
            input.localPath = new File("/sdcard/GitA/ViewFieldMatcher");

            AsyncActionListener<Git> listener = new AsyncActionListener<Git>() {
                @Override
                public void onActionPerformed(Git output) {
                    Toast.makeText(getActivity(), "onActionPerformed", Toast.LENGTH_LONG).show();
                    output.close();
                }

                @Override
                public void onActionFailed(Exception e) {
                    Toast.makeText(getActivity(), "onActionFailed " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            };

            mActionQueueExecutor.queueAction(new CloneRepositoryAction(), input, listener);
        }
    };
}
