package com.vardroid.duallistselector.list;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.vardroid.duallistselector.R;
import com.vardroid.duallistselector.util.SelectorListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.NonNull;

class SelectorListAdapter extends RecyclerView.Adapter<SelectorListAdapter.ViewHolder> {

    private final List<SelectorListAdapterItem> items = new ArrayList<>();

    private final Context context;

    private SelectorListConfig config;

    private Listener listener;

    public SelectorListAdapter(
            @NonNull Context context) {
        this(
                context,
                SelectorListConfig.getDefault(context));
    }

    public SelectorListAdapter(
            @NonNull Context context,
            @NonNull SelectorListConfig config) {
        this.context = context;
        this.config = config;
    }

    @Override
    public void onViewRecycled(
            @androidx.annotation.NonNull @NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        cleanupImage(holder);
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @androidx.annotation.NonNull ViewGroup parent,
            int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_selector_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(
            @androidx.annotation.NonNull ViewHolder holder,
            int position) {
        final SelectorListItem item = items.get(position).getItem();

        bindSelection(holder);
        bindImage(holder, item);
        bindPrimaryText(holder, item);
        bindSecondaryText(holder, item);
    }

    private void bindSelection(
            @NonNull ViewHolder holder) {
        holder.itemView.setOnClickListener(view -> {
            int selectedPosition = holder.getAdapterPosition();
            if (selectedPosition >= 0) {
                SelectorListAdapterItem selectedItem = items.get(selectedPosition);
                if (!selectedItem.isSelected()) {
                    selectedItem = selectedItem.toBuilder()
                            .selected(true)
                            .build();
                    items.set(selectedPosition, selectedItem);
                    if (listener != null) {
                        listener.onItemSelected(selectedItem);
                    }
                }
            }
        });
    }

    private void bindImage(
            @NonNull ViewHolder holder,
            @NonNull SelectorListItem item) {
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = config.getImageSize();
        layoutParams.height = config.getImageSize();
        holder.imageView.setLayoutParams(layoutParams);

        cleanupImage(holder);
        holder.defaultImageHolder = new DefaultImageHolder(
                context,
                config.getImageSize(),
                item.getPrimaryText(),
                item.getId());

        if (item.getImageLoader() == null) {
            holder.imageView.setImageDrawable(holder.defaultImageHolder.get());
        } else {
            item.getImageLoader().loadImage(
                    holder.imageView,
                    holder.defaultImageHolder);
        }
    }

    private void cleanupImage(
            @NonNull ViewHolder holder) {
        if (holder.defaultImageHolder != null) {
            holder.defaultImageHolder.recycle();
            holder.defaultImageHolder = null;
        }
        holder.imageView.setImageDrawable(null);
    }

    private void bindPrimaryText(
            @NonNull ViewHolder holder,
            @NonNull SelectorListItem item) {
        String primaryText = item.getPrimaryText();
        holder.primaryTextView.setText(primaryText);
        holder.primaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getPrimaryTextSize());
        holder.primaryTextView.setTextColor(config.getPrimaryTextColor());
    }

    private void bindSecondaryText(
            @NonNull ViewHolder holder,
            @NonNull SelectorListItem item) {
        String secondaryText = item.getSecondaryText();
        holder.secondaryTextView.setText(secondaryText);
        holder.secondaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.getSecondaryTextSize());
        holder.secondaryTextView.setTextColor(config.getSecondaryTextColor());
        holder.secondaryTextView.setVisibility(secondaryText == null
                ? View.GONE
                : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public SelectorListConfig getConfig() {
        return config;
    }

    public void setConfig(
            @NonNull SelectorListConfig config) {
        this.config = config;
        notifyDataSetChanged();
    }

    @NonNull
    public ImmutableList<SelectorListAdapterItem> getItems() {
        return ImmutableList.copyOf(items);
    }

    public void setItems(
            @NonNull ImmutableList<SelectorListAdapterItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public Optional<Integer> addItem(
            @NonNull SelectorListAdapterItem item) {
        item = item.reset();
        switch (config.getInsertionOrder()) {
            case BEGINNING:
                items.add(0, item);
                break;
            case END:
                items.add(item);
                break;
            default:
                items.add(item);
                Collections.sort(items, SelectorListAdapterItemComparator.getInstance());
                break;
        }

        Optional<Integer> position = SelectorListUtils.findPosition(
                ImmutableList.copyOf(items),
                item);
        if (position.isPresent()) {
            notifyItemInserted(position.get());
        }

        return position;
    }

    public void removeItem(
            @NonNull SelectorListAdapterItem item) {
        Optional<Integer> position = SelectorListUtils.findPosition(
                ImmutableList.copyOf(items),
                item);
        if (position.isPresent()) {
            items.remove(position.get().intValue());
            notifyItemRemoved(position.get());
        }
    }

    public void setListener(
            @Nullable Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemSelected(
                @NonNull SelectorListAdapterItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private DefaultImageHolder defaultImageHolder;

        private final AppCompatImageView imageView;

        private final TextView primaryTextView;

        private final TextView secondaryTextView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.imageView = rootView.findViewById(R.id.image_view);
            this.primaryTextView = rootView.findViewById(R.id.primary_text_view);
            this.secondaryTextView = rootView.findViewById(R.id.secondary_text_view);
        }
    }
}