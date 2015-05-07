package fr.xgouchet.gitstorageprovider.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import fr.xgouchet.gitstorageprovider.GitApplication;
import fr.xgouchet.gitstorageprovider.utils.DoubleDeckerBus;

/**
 * @author Xavier Gouchet
 */
public class LocalReposFragment extends Fragment {

    private DoubleDeckerBus mEventBus;
    private DoubleDeckerBus mBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the common event bus
        mBus = ((GitApplication )getActivity().getApplication()).getBus();
    }

}
