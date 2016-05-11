package fr.xgouchet.gitsp.ui.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public abstract class TextIconViewHolder<T> extends ListRV.ViewHolder<T> {

    @BindView(android.R.id.title)
    TextView titleView;
    @BindView(android.R.id.icon)
    ImageView iconView;

    public TextIconViewHolder(View itemView) {
        super(itemView);
        bind(this, itemView);
    }

    @Override
    protected void onItemBound(T item, int position) {
        titleView.setText(getText(item, position));

        iconView.setImageResource(getIcon(item, position));
    }

    @Nullable
    protected abstract String getText(T item, int position);

    protected abstract int getIcon(T item, int position);
}
