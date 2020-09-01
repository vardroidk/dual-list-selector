package com.vardroid.duallistselector.util;

import lombok.NonNull;

public interface Converter<I, O> {

    @NonNull
    O convert(
            @NonNull I in);
}
