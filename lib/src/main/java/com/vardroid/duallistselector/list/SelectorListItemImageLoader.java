package com.vardroid.duallistselector.list;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

public interface SelectorListItemImageLoader {

    void loadImage(
            @NonNull AppCompatImageView imageView,
            @NonNull DefaultImageHolder defaultImageHolder);

}
