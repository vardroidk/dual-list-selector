package com.vardroid.duallistselector.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemDividerDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;

    private final float marginLeftInPixel;

    public ListItemDividerDecoration(
            @ColorInt int colorInt,
            @FloatRange(from = 0, fromInclusive = false) float heightInPixel,
            @FloatRange(from = 0, fromInclusive = false) float marginLeftInPixel) {
        this.paint = new Paint();
        this.paint.setColor(colorInt);
        this.paint.setStrokeWidth(heightInPixel);
        this.marginLeftInPixel = marginLeftInPixel;
    }

    @Override
    public void getItemOffsets(
            @androidx.annotation.NonNull Rect outRect,
            @androidx.annotation.NonNull View view,
            @androidx.annotation.NonNull RecyclerView parent,
            @androidx.annotation.NonNull RecyclerView.State state) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int position = layoutParams.getViewAdapterPosition();

        // Add a divider to any view but the last one
        if (position < state.getItemCount()) {
            outRect.set(
                    0,
                    0,
                    0,
                    (int) paint.getStrokeWidth());
        } else {
            outRect.setEmpty();
        }
    }

    @Override
    public void onDraw(
            @androidx.annotation.NonNull Canvas canvas,
            @androidx.annotation.NonNull RecyclerView parent,
            @androidx.annotation.NonNull RecyclerView.State state) {
        final int halfDividerHeight = (int) (paint.getStrokeWidth() / 2);

        // This will iterate over every visible view and draw the divider
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View view = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            final int position = layoutParams.getViewAdapterPosition();

            // Add a divider to any view but the last one
            if (position < state.getItemCount()) {
                canvas.drawLine(
                        view.getLeft() + marginLeftInPixel,
                        view.getBottom() + halfDividerHeight,
                        view.getRight(),
                        view.getBottom() + halfDividerHeight,
                        paint);
            }
        }
    }
}
