package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.xgouchet.gitstorageprovider.R;
import fr.xgouchet.gitstorageprovider.core.git.LocalRepository;

/**
 * @author Xavier Gouchet
 */
public class LocalRepositoriesAdapter extends RecyclerView.Adapter<LocalRepositoriesAdapter.ViewHolder> {


    /**
     * Internal View holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        LocalRepository mLocalRepository;

        private final View mRootView;

        @InjectView(android.R.id.title)
        TextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.inject(this, itemView);
        }

    }

    private List<LocalRepository> mLocalRepositories;

    public void setLocalRepositories(List<LocalRepository> repositories) {
        mLocalRepositories = repositories;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local_repos, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (mLocalRepositories == null) {
            return 0;
        } else {
            return mLocalRepositories.size();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalRepository localRepository = mLocalRepositories.get(position);

        holder.mTitleView.setText(localRepository.getName());
        holder.mLocalRepository = localRepository;
    }
}
