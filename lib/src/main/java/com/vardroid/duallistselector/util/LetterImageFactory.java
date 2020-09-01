package com.vardroid.duallistselector.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;

import lombok.NonNull;

public class LetterImageFactory {

    private static final int[] COLORS = new int[] {
            -0xd50000,
            -0xc51162,
            -0xaa00ff,
            -0x6200ea,
            -0x304ffe,
            -0x2962ff,
            -0x0091ea,
            -0x00b8d4,
            -0x00bfa5,
            -0x00c853,
            -0x64dd17,
            -0xaeea00,
            -0xffd600,
            -0xffab00,
            -0xff6d00,
            -0xdd2c00,
            -0x263238,
            -0x263238,
            -0x3e2723,
            -0x000000
    };

    public static BitmapDrawable create(
            @NonNull Context context,
            int imageSize,
            @NonNull String text,
            @NonNull String id) {
        String letterText = getFirstCharacter(text);
        float texSize = calculateTextSize(imageSize);
        TextPaint textPaint = createTextPainter(context, texSize);

        Rect rectangle = new Rect(0, 0, imageSize, imageSize);
        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = createPaint();
        paint.setColor(getColor(id));
        canvas.drawRect(rectangle, paint);
        paint.setColor(Color.TRANSPARENT);

        RectF bounds = new RectF(rectangle);
        bounds.right = textPaint.measureText(letterText, 0, 1);
        bounds.bottom = textPaint.descent() - textPaint.ascent();
        bounds.left += (rectangle.width() - bounds.right) / 2.0f;
        bounds.top += (rectangle.height() - bounds.bottom) / 2.0f;

        canvas.drawCircle(
                ((float) imageSize) / 2,
                ((float) imageSize) / 2,
                ((float) imageSize) / 2,
                paint);
        canvas.drawText(
                letterText,
                bounds.left,
                bounds.top - textPaint.ascent(),
                textPaint);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    @NonNull
    private static String getFirstCharacter(
            @NonNull String text) {
        return text.substring(0, 1).toUpperCase();
    }

    @NonNull
    private static TextPaint createTextPainter(
            @NonNull Context context,
            float textSize) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize * context.getResources().getDisplayMetrics().scaledDensity);
        return textPaint;
    }

    @NonNull
    private static Paint createPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        return paint;
    }

    private static float calculateTextSize(
            int imageSize) {
        return (float) (imageSize / 3.5);
    }

    private static int getColor(
            @NonNull String id) {
        int colorIndex = id.hashCode() % COLORS.length;
        return COLORS[colorIndex];
    }
}