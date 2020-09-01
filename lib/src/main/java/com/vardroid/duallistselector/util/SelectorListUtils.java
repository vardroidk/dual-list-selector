package com.vardroid.duallistselector.util;

import androidx.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.vardroid.duallistselector.list.SelectorListAdapterItem;
import com.vardroid.duallistselector.list.SelectorListItem;

import java.util.List;

import lombok.NonNull;

public final class SelectorListUtils {

    private SelectorListUtils() {

    }

    @NonNull
    public static ImmutableList<String> findItemIds(
            @Nullable ImmutableList<SelectorListAdapterItem> items) {
        ImmutableList.Builder<String> itemIds = new ImmutableList.Builder<>();
        if (items != null) {
            for (SelectorListAdapterItem item : items) {
                itemIds.add(item.getItem().getId());
            }
        }
        return itemIds.build();
    }

    @NonNull
    public static ImmutableList<SelectorListAdapterItem> findItems(
            @Nullable ImmutableList<SelectorListAdapterItem> items,
            @Nullable ImmutableList<String> itemIds) {
        ImmutableList.Builder<SelectorListAdapterItem> matchingItems = new ImmutableList.Builder<>();
        if (items != null) {
            for (SelectorListAdapterItem item : items) {
                if (itemIds == null || itemIds.contains(item.getItem().getId())) {
                    matchingItems.add(item);
                }
            }
        }
        return matchingItems.build();
    }

    public static boolean checkUniqueItems(
            @Nullable ImmutableList<SelectorListItem> items) {
        ImmutableSet.Builder<String> itemIds = new ImmutableSet.Builder<>();
        if (items != null) {
            for (SelectorListItem item : items) {
                itemIds.add(item.getId());
            }
        }
        return items == null || items.size() == itemIds.build().size();
    }

    public static ImmutableList<SelectorListAdapterItem> toAdapterItems(
            @NonNull List<SelectorListItem> items) {
        ImmutableList.Builder<SelectorListAdapterItem> result = new ImmutableList.Builder<>();
        for (int i = 0; i < items.size(); i++) {
            SelectorListItem item = items.get(i);
            result.add(SelectorListAdapterItem.builder()
                    .item(item)
                    .order(i)
                    .selected(false)
                    .build());
        }
        return result.build();
    }

    public static ImmutableList<SelectorListItem> toItems(
            @NonNull List<SelectorListAdapterItem> items) {
        ImmutableList.Builder<SelectorListItem> result = new ImmutableList.Builder<>();
        for (SelectorListAdapterItem item : items) {
            result.add(item.getItem());
        }
        return result.build();
    }

    public static Optional<Integer> findPosition(
            @NonNull ImmutableList<SelectorListAdapterItem> items,
            @NonNull SelectorListAdapterItem item) {
        Integer position = null;

        String itemId = item.getItem().getId();
        for (int i = 0; i < items.size(); i++) {
            SelectorListAdapterItem currentItem = items.get(i);
            if (itemId.equals(currentItem.getItem().getId())) {
                position = i;
                break;
            }
        }

        return Optional.fromNullable(position);
    }
}
