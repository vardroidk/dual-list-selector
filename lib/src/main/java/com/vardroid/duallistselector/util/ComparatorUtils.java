package com.vardroid.duallistselector.util;

import androidx.annotation.Nullable;

public final class ComparatorUtils {

    private ComparatorUtils() {
    }

    public static <T> int compare(
            @Nullable Comparable<T> objectLeft,
            @Nullable T objectRight) {
        int result;
        if (objectLeft == null && objectRight == null) {
            result = 0;
        } else if (objectLeft == null) {
            result = 1;
        } else if (objectRight == null) {
            result = -1;
        } else {
            result = objectLeft.compareTo(objectRight);
        }
        return result;
    }

}
