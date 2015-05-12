package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.account.Account;
import fr.xgouchet.gitstorageprovider.core.credentials.Credential;
import fr.xgouchet.gitstorageprovider.core.oauth.OAuthConfigFactory;

/**
 * @author Xavier Gouchet
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {


    /**
     * Internal View holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public Account mAccount;

        private final View mRootView;

        @InjectView(android.R.id.title)
        TextView mTitleView;

        @InjectView(android.R.id.icon)
        ImageView mIconView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.inject(this, itemView);
        }

    }


    private List<Account> mAccounts;

    public void setAccounts(List<Account> accounts) {
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
        Account account = mAccounts.get(position);

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
