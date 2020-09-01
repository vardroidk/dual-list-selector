package com.vardroid.duallistselector.list;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(toBuilder = true)
public class SelectorListAdapterItem {

    @NonNull
    @Getter
    private final SelectorListItem item;

    @Getter
    private final int order;

    @Getter
    private final boolean selected;

    @NonNull
    public SelectorListAdapterItem reset() {
        return this.toBuilder()
                .selected(false)
                .build();
    }

}
