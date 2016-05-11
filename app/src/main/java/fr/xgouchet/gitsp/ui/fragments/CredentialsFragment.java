package fr.xgouchet.gitsp.ui.fragments;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.credentials.Credential;
import fr.xgouchet.gitsp.credentials.CredentialsSupplier;
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

/**
 * @author Xavier Gouchet
 */
public class CredentialsFragment extends StatefulFragment {

    private static final String TAG = CredentialsFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        stateDelegate.setLoading("");
        setCurrentState(StateHolder.LOADING);
        credentialAdapter.clear();
        Observable.create(new CredentialsSupplier(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(credentialObserver);
    }

    private void setEmpty() {
        stateDelegate.setEmpty(getString(R.string.empty_credentials),
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_credentials));
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

    private final Observer<Credential> credentialObserver = new Observer<Credential>() {

        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted");
            if (credentialAdapter.getItemCount() == 0) {
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
        public void onNext(Credential credential) {
            Log.i(TAG, "onNext → " + credential);
            if (credential != null) {
                credentialAdapter.addItem(credential);
                setCurrentState(StateHolder.IDEAL);
            }
        }
    };

    private final FabDelegate fabDelegate = new SimpleFabDelegate() {
        @Override
        public void onFabClicked(@StateHolder.State int state) {
//            Toast.makeText(getActivity(), "Add credentials (TODO)", Toast.LENGTH_SHORT).show();
//
//            KeyPair keyPair = null;
//            try {
//                keyPair = CredentialHelper.generateNewKeyPair();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//                return;
//            }
//
//            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//            try {
//                Log.i(TAG, "Public key " + Base64.encodeToString(CredentialHelper.encodePublicKey(publicKey), 0));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Nullable
        @Override
        public Drawable getFabDrawable(@StateHolder.State int state) {
            return ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_add_credential);
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

    private final ListRV.Adapter<Credential> credentialAdapter = new ListRV.Adapter<Credential>() {
        @Override
        public ListRV.ViewHolder<Credential> onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_icon, parent, false);
            return new CredentialViewHolder(itemView);
        }
    };


    private static class CredentialViewHolder extends TextIconViewHolder<Credential> {

        public CredentialViewHolder(View itemView) {
            super(itemView);
        }

        @Nullable
        @Override
        protected String getText(Credential item, int position) {
            return item.getDisplayName();
        }

        @Override
        protected int getIcon(Credential item, int position) {
            return R.drawable.ic_credentials;
        }


    }
}
