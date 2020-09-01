package com.vardroid.duallistselector.list;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import com.vardroid.duallistselector.util.LetterImageFactory;

import javax.annotation.Nonnull;

public class DefaultImageHolder {

    private final Context context;

    private final int imageSize;

    private final String text;

    private final String id;

    private BitmapDrawable defaultImage;

    public DefaultImageHolder(
            @Nonnull Context context,
            int imageSize,
            @Nonnull String text,
            @Nonnull String id) {
        this.context = context;
        this.text = text;
        this.imageSize = imageSize;
        this.id = id;
    }

    public void recycle() {
        if (defaultImage != null) {
            defaultImage.getBitmap().recycle();
            defaultImage = null;
        }
    }

    public synchronized BitmapDrawable get() {
        if (defaultImage == null) {
            defaultImage = create();
        }
        return defaultImage;
    }

    private BitmapDrawable create() {
        return LetterImageFactory.create(
                context,
                imageSize,
                text,
                id);
    }
}
