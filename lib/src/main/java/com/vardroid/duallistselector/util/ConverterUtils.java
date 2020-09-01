package com.vardroid.duallistselector.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.NonNull;

public class ConverterUtils {

    private ConverterUtils() {
    }

    public static <I, O> O convert(
            @Nullable I in,
            @NonNull Converter<I, O> converter) {
        if (in == null) {
            return null;
        }
        return converter.convert(in);
    }

    public static <I, O> List<O> convertList(
            @Nullable Collection<I> list,
            @NonNull  Converter<I, O> converter) {
        if (list == null) {
            return null;
        }
        List<O> outList = new ArrayList<>();
        for (I in : list) {
            O out = convert(in, converter);
            if (out != null) {
                outList.add(out);
            }
        }
        return outList;
    }

}
