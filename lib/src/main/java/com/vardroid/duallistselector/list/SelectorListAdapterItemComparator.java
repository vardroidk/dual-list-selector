package com.vardroid.duallistselector.list;

import com.vardroid.duallistselector.util.ComparatorUtils;

import java.util.Comparator;

import lombok.NonNull;

class SelectorListAdapterItemComparator implements Comparator<SelectorListAdapterItem> {

    private static final SelectorListAdapterItemComparator INSTANCE = new SelectorListAdapterItemComparator();

    public static SelectorListAdapterItemComparator getInstance() {
        return INSTANCE;
    }

    private SelectorListAdapterItemComparator() {}

    @Override
    public int compare(
            @NonNull SelectorListAdapterItem itemLeft,
            @NonNull SelectorListAdapterItem itemRight) {
        int result;
        if (itemLeft == null && itemRight == null) {
            result = 0;
        } else if (itemLeft == null) {
            result = 1;
        } else if (itemRight == null) {
            result = -1;
        } else {
            result = ComparatorUtils.compare(
                    itemLeft.getOrder(),
                    itemRight.getOrder());
        }
        return result;
    }
}
