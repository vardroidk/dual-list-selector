package com.vardroid.duallistselector.list;

import androidx.annotation.Nullable;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class SelectorListItem {

    @NonNull
    @Getter
    private final String id;

    @NonNull
    @Getter
    private final String primaryText;

    @Nullable
    @Getter
    private final String secondaryText;

    @Nullable
    @Getter
    private final SelectorListItemImageLoader imageLoader;

}
