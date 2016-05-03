package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import fr.xgouchet.gitsp.oauth.OAuthAccount;
import fr.xgouchet.gitsp.oauth.OAuthConfigFactory;
import fr.xgouchet.gitstorageprovider.R;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {


    /**
     * Internal View holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public OAuthAccount mAccount;

        private final View mRootView;

        @BindView(android.R.id.title)
        TextView mTitleView;

        @BindView(android.R.id.icon)
        ImageView mIconView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            bind(this, itemView);
        }

    }


    private List<OAuthAccount> mAccounts;

    public void setAccounts(List<OAuthAccount> accounts) {
        mAccounts = accounts;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (mAccounts == null) {
            return 0;
        } else {
            return mAccounts.size();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OAuthAccount account = mAccounts.get(position);

        int icon;
        switch (account.getServiceId()) {
            case OAuthConfigFactory.SERVICE_GITHUB:
                icon = R.mipmap.ic_account_github;
                break;
            default:
                icon = R.mipmap.ic_launcher;
                break;
        }

        holder.mIconView.setImageResource(icon);
        holder.mTitleView.setText(account.getUserName());
        holder.mAccount = account;
    }
}
