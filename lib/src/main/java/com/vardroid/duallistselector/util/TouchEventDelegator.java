package com.vardroid.duallistselector.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import lombok.NonNull;

public class TouchEventDelegator {

    private static final float TAP_THRESHOLD = 30;

    @NonNull
    private final WeakReference<View> horizontalView;

    @NonNull
    private final WeakReference<View> verticalView;

    @NonNull
    private final WeakReference<View> targetView;
    private final Rect targetViewRect = new Rect();
    private final int[] targetViewLocationOnScreen = new int[2];

    @NonNull
    private ScrollType scrollType = ScrollType.NONE;
    private float downTouchX;
    private float downTouchY;

    public TouchEventDelegator(
            @NonNull View horizontalView,
            @NonNull View verticalView,
            @Nullable View targetView) {
        this.horizontalView = new WeakReference<>(horizontalView);
        this.verticalView = new WeakReference<>(verticalView);
        this.targetView = new WeakReference<>(targetView);
    }

    public boolean delegateTouchEvent(
            @NonNull MotionEvent event) {
        boolean relevantTouch = true;
        if (targetView.get() != null) {
            relevantTouch = isViewInBounds(
                    targetView.get(),
                    (int) event.getRawX(),
                    (int) event.getRawY());
        }
        if (relevantTouch) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    scrollType = ScrollType.NONE;
                    downTouchX = event.getX();
                    downTouchY = event.getY();
                    delegateVerticalTouchEvent(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (scrollType == ScrollType.NONE) {
                        float diffX = Math.abs(downTouchX - event.getX());
                        float diffY = Math.abs(downTouchY - event.getY());
                        if (diffX > diffY) {
                            scrollType = ScrollType.HORIZONTAL;
                        } else {
                            scrollType = ScrollType.VERTICAL;
                        }
                    }
                    delegateTouchEventInner(event);
                    break;
                case MotionEvent.ACTION_UP:
                    float diffX = Math.abs(downTouchX - event.getX());
                    float diffY = Math.abs(downTouchY - event.getY());
                    if (diffX < TAP_THRESHOLD && diffY < TAP_THRESHOLD) { // It seems tapping, not scrolling
                        delegateVerticalTouchEvent(event);
                    } else {
                        delegateTouchEventInner(event);
                    }
                    break;
                default:
                    delegateVerticalTouchEvent(event);
                    break;
            }
        }

        return true;
    }

    private void delegateTouchEventInner(
            @NonNull MotionEvent event) {
        switch (scrollType) {
            case HORIZONTAL:
                delegateHorizontalTouchEvent(event);
                break;
            case VERTICAL:
                delegateVerticalTouchEvent(event);
                break;
        }
    }

    private void delegateHorizontalTouchEvent(
            @NonNull MotionEvent event) {
        if (horizontalView.get() != null) {
            horizontalView.get().dispatchTouchEvent(event);
        }
    }

    private void delegateVerticalTouchEvent(
            @NonNull MotionEvent event) {
        if (verticalView.get() != null) {
            verticalView.get().dispatchTouchEvent(event);
        }
    }

    private boolean isViewInBounds(
            @NonNull View view,
            int x,
            int y) {
        view.getDrawingRect(targetViewRect);
        view.getLocationOnScreen(targetViewLocationOnScreen);
        targetViewRect.offset(targetViewLocationOnScreen[0], targetViewLocationOnScreen[1]);
        return targetViewRect.contains(x, y);
    }

    private enum ScrollType {
        HORIZONTAL,
        VERTICAL,
        NONE,
        ;
    }
}