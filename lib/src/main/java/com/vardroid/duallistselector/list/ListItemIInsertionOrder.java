package com.vardroid.duallistselector.list;

import androidx.annotation.Nullable;

import lombok.NonNull;

public enum ListItemIInsertionOrder {

    BEGINNING,

    END,

    ORIGINAL,

    ;

    @Nullable
    public static ListItemIInsertionOrder findByName(
            @Nullable String name) {
        ListItemIInsertionOrder result = null;
        for (ListItemIInsertionOrder item : values()) {
            if (item.name().equals(name)) {
                result = item;
                break;
            }
        }
        return result;
    }

    @NonNull
    public static String getName(
            @Nullable ListItemIInsertionOrder item) {
        return item == null
                ? ""
                : item.name();
    }
}
