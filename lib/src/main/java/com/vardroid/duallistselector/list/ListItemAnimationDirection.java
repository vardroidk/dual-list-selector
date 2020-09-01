package com.vardroid.duallistselector.list;

import androidx.annotation.Nullable;

import lombok.NonNull;

public enum ListItemAnimationDirection {

    LEFT,

    RIGHT,

    ;

    @Nullable
    public static ListItemAnimationDirection findByName(
            @Nullable String name) {
        ListItemAnimationDirection result = null;
        for (ListItemAnimationDirection item : values()) {
            if (item.name().equals(name)) {
                result = item;
                break;
            }
        }
        return result;
    }

    @NonNull
    public static String getName(
            @Nullable ListItemAnimationDirection item) {
        return item == null
                ? ""
                : item.name();
    }
}
