package fr.xgouchet.gitsp.ui.fragments.stateful;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.xgouchet.gitsp.R;

/**
 * @author Xavier Gouchet
 */
public abstract class SimpleStateDelegate implements StateDelegate {


    @Nullable
    private CharSequence loadingMessage;
    @Nullable
    private CharSequence emptyMessage;
    @Nullable
    private Drawable emptyIcon;
    @Nullable
    private Throwable failure;

    public void setLoading(@Nullable CharSequence message) {
        loadingMessage = message;
    }

    public void setEmpty(@Nullable CharSequence message, @Nullable Drawable icon) {
        emptyMessage = message;
        emptyIcon = icon;
    }

    public void setFailure(@Nullable Throwable failure) {
        this.failure = failure;
        if (failure != null) {
            failure.printStackTrace();
        }
    }

    @NonNull
    @Override
    public View createEmptyView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_default, parent, false);
    }

    @NonNull
    @Override
    public View createLoadingView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_default, parent, false);
    }

    @NonNull
    @Override
    public View createErrorView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.error_default, parent, false);
    }

    @Override
    public void updateEmptyView(@NonNull View emptyView) {
        TextView emptyText = (TextView) emptyView.findViewById(android.R.id.message);
        if (emptyText == null) {
            return;
        }

        emptyText.setText(emptyMessage);
        emptyText.setCompoundDrawablesWithIntrinsicBounds(null, emptyIcon, null, null);
    }

    @Override
    public void updateLoadingView(@NonNull View loadingView) {
    }

    @Override
    public void updateErrorView(@NonNull View errorView) {
        TextView errorText = (TextView) errorView.findViewById(android.R.id.message);
        if (errorText == null) {
            return;
        }

        if (failure == null) {
            errorText.setText(R.string.error_empty_failure);
        } else {
            errorText.setText(failure.getMessage());
        }

    }


}
