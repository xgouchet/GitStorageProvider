package fr.xgouchet.gitsp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier Gouchet
 */
public final class ListRV {

    public interface ItemClickListener<T> {

        void onItemClicked(@Nullable T item, int position);
    }

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

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Nullable
        private T item;
        private final ItemClickListener<T> itemClickListener;

        private int position;

        public ViewHolder(View itemView) {
            this(itemView, null);
        }

        public ViewHolder(@NonNull View itemView, @Nullable ItemClickListener<T> itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;

            if (itemClickListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        public void bindItem(@Nullable T item, int position) {
            this.item = item;
            this.position = position;
            onItemBound(item, position);
        }

        protected abstract void onItemBound(@Nullable T item, int position);

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClicked(item, position);
            }
        }
    }
}
