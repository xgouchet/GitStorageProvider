package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.actions.ActionQueueExecutor;

/**
 * @author Xavier Gouchet
 */
public class LocalReposAdapter extends RecyclerView.Adapter<LocalReposAdapter.LocalReposViewHolder>  {



    /**
     * Internal View holder
     */
    static class LocalReposViewHolder extends RecyclerView.ViewHolder {

        private final View mRootView;

        @InjectView(android.R.id.title)
        TextView mTitleView;

        public LocalReposViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.inject(this, itemView);
        }

    }

    @Override
    public LocalReposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local_repos, parent, false);
        return new LocalReposViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(LocalReposViewHolder holder, int position) {

    }
}
