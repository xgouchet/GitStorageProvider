package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.ui.adapters.CredentialsAdapter;
import fr.xgouchet.gitstorageprovider.ui.adapters.LocalRepositoriesAdapter;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * This fragment displays the local credentials
 *
 * @author Xavier Gouchet
 */
public class CredentialsFragment extends Fragment {

    private DoubleDeckerBus mBus;

    @InjectView(android.R.id.list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.fab)
    FloatingActionButton mFAB;

    private RecyclerView.Adapter mCredentialsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common event bus
        mBus = ((GitApplication) getActivity().getApplication()).getBus();
        mCredentialsAdapter = new CredentialsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_fab, container, false);
        ButterKnife.inject(this, root);

        // set recycler view layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mCredentialsAdapter);
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
            Toast.makeText(getActivity(), "Hello Fab", Toast.LENGTH_LONG).show();
        }
    };
}
