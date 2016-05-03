package fr.xgouchet.gitstorageprovider.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import fr.xgouchet.gitsp.git.LocalRepo;
import fr.xgouchet.gitstorageprovider.R;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class LocalRepositoriesAdapter extends RecyclerView.Adapter<LocalRepositoriesAdapter.ViewHolder> {


    /**
     * Internal View holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        LocalRepo mLocalRepository;

        private final View mRootView;

        @BindView(android.R.id.title)
        TextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            bind(this, itemView);
        }

    }

    private List<LocalRepo> mLocalRepositories;

    public void setLocalRepositories(List<LocalRepo> repositories) {
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
        LocalRepo localRepository = mLocalRepositories.get(position);

        holder.mTitleView.setText(localRepository.getName());
        holder.mLocalRepository = localRepository;
    }
}
