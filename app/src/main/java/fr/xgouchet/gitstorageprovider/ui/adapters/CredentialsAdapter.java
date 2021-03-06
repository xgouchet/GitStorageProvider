package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import fr.xgouchet.gitsp.R;
import fr.xgouchet.gitsp.credentials.Credential;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class CredentialsAdapter extends RecyclerView.Adapter<CredentialsAdapter.ViewHolder> {


    /**
     * Internal View holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public Credential mCredential;

        private final View mRootView;

        @BindView(android.R.id.title)
        TextView mTitleView;


        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            bind(this, itemView);
        }

    }


    private List<Credential> mCredentials;

    public void setCredentials(List<Credential> credentials) {
        mCredentials = credentials;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_icon, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (mCredentials == null) {
            return 0;
        } else {
            return mCredentials.size();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Credential credential = mCredentials.get(position);

        holder.mTitleView.setText(credential.getDisplayName());
        holder.mCredential = credential;
    }
}
