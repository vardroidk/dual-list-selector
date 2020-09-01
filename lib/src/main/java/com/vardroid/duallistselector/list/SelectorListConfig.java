package com.vardroid.duallistselector.list;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.vardroid.duallistselector.R;

import javax.annotation.concurrent.Immutable;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Immutable
@Builder(toBuilder = true)
class SelectorListConfig {

    public static SelectorListConfig getDefault(
            @NonNull Context context) {
        Resources resources = context.getResources();
        int defaultListItemImageSize = resources.getDimensionPixelSize(R.dimen.default_list_item_image_size);
        float defaultListItemPrimaryTextSize = resources.getDimension(R.dimen.default_list_item_primary_text_size);
        @ColorInt int defaultListItemPrimaryTextColor = ContextCompat.getColor(context, R.color.default_list_item_primary_text_color);
        float defaultListItemSecondaryTextSize = resources.getDimension(R.dimen.default_list_item_secondary_text_size);
        @ColorInt int defaultListItemSecondaryTextColor = ContextCompat.getColor(context, R.color.default_list_item_secondary_text_color);
        return SelectorListConfig.builder()
                .insertionOrder(ListItemIInsertionOrder.BEGINNING)
                .imageSize(defaultListItemImageSize)
                .primaryTextSize(defaultListItemPrimaryTextSize)
                .primaryTextColor(defaultListItemPrimaryTextColor)
                .secondaryTextSize(defaultListItemSecondaryTextSize)
                .secondaryTextColor(defaultListItemSecondaryTextColor)
                .build();
    }

    @NonNull
    private final ListItemIInsertionOrder insertionOrder;

    private final int imageSize;

    private final float primaryTextSize;

    @ColorInt
    private final int primaryTextColor;

    private final float secondaryTextSize;

    @ColorInt
    private final int secondaryTextColor;

}


