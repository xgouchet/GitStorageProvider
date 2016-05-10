package fr.xgouchet.gitsp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier Gouchet
 */
public final class ListRV {

    public static abstract class Adapter<T> extends RecyclerView.Adapter<ListRV.ViewHolder<T>> {

        private final List<T> items = new ArrayList<>();

        @Override
        public void onBindViewHolder(ViewHolder<T> holder, int position) {
            T item = items.get(position);
            holder.bindItem(item, position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public void addItem(T item) {
            int position = items.size();
            items.add(item);
            notifyItemInserted(position);
        }
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        T item;

        private final View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bindItem(T item, int position) {
            this.item = item;
            onItemBound(item, position);
        }

        protected abstract void onItemBound(T item, int position);
    }
}
