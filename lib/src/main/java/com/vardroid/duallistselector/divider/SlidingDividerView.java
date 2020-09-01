package com.vardroid.duallistselector.divider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.vardroid.duallistselector.R;
import com.vardroid.duallistselector.util.TouchEventDelegator;

import lombok.NonNull;

public class SlidingDividerView extends LinearLayout {

    private static final int SCROLLING_SNAP_DIFF = 200;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final SavedState savedState = new SavedState();

    private Listener listener;

    private View containerView;
    private HorizontalScrollView scrollView;
    private ViewGroup leftView;
    private View leftViewContent;
    private ViewGroup rightView;
    private View rightViewContent;
    private View dividerView;
    private View dividerHandlebarView;

    private Integer scrollX;

    private TouchEventDelegator touchEventDelegator;

    public SlidingDividerView(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(true);
        View rootView = inflate(getContext(), R.layout.layout_sliding_divider_view, this);
        inflateViews(rootView);
        initViews(attrs);
    }

    @Override
    protected void onRestoreInstanceState(
            Parcelable state) {
        SavedState superSavedState = (SavedState) state;
        super.onRestoreInstanceState(superSavedState.superSavedState);
        savedState.update(superSavedState);

        // List view
        setCollapsedViewWidth(savedState.collapsedViewWidth);
        setDividerBackgroundColorInt(savedState.dividerBackgroundColorInt);
        setDividerHandlebarColorInt(savedState.dividerHandlebarColorInt);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        savedState.superSavedState = super.onSaveInstanceState();
        return savedState;
    }

    private void inflateViews(
            @NonNull View rootView) {
        containerView = rootView.findViewById(R.id.container_view);
        scrollView = rootView.findViewById(R.id.scroll_view);
        leftView = rootView.findViewById(R.id.left_view);
        rightView = rootView.findViewById(R.id.right_view);
        dividerView = rootView.findViewById(R.id.divider_view);
        dividerHandlebarView = rootView.findViewById(R.id.divider_handlebar_view);
    }

    private void initViews(
            @NonNull AttributeSet attrs) {
        initAttributes(attrs);
        initScrollView();
    }

    private void initAttributes(
            @NonNull AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.SlidingDividerView);

        int defaultCollapsedViewWidth = getResources().getDimensionPixelSize(R.dimen.default_collapsed_view_width);
        setCollapsedViewWidth(typedArray.getDimensionPixelSize(
                R.styleable.SlidingDividerView_sdv_collapsed_view_width,
                defaultCollapsedViewWidth));

        int defaultDividerBackgroundColorInt = getColorInt(R.color.default_divider_background_color);
        setDividerBackgroundColorInt(typedArray.getColor(
                R.styleable.SlidingDividerView_sd_divider_background_color,
                defaultDividerBackgroundColorInt));

        int defaultDividerHandlebarColorInt = getColorInt(R.color.default_divider_handlebar_color);
        setDividerHandlebarColorInt(typedArray.getColor(
                R.styleable.SlidingDividerView_sd_divider_handlebar_color,
                defaultDividerHandlebarColorInt));

        typedArray.recycle();
    }

    public View getScrollView() {
        return scrollView;
    }

    public void setTouchEventDelegator(
            @Nullable TouchEventDelegator touchEventDelegator) {
        this.touchEventDelegator = touchEventDelegator;
    }

    @Override
    public boolean dispatchTouchEvent(
            MotionEvent event) {
        super.dispatchTouchEvent(event);
        return touchEventDelegator == null
                ? super.dispatchTouchEvent(event)
                : touchEventDelegator.delegateTouchEvent(event);
    }

    public void setListener(
            @Nullable Listener listener) {
        this.listener = listener;
    }

    public int getCollapsedViewWidth() {
        return savedState.collapsedViewWidth;
    }

    public void setCollapsedViewWidth(
            int collapsedViewWidth) {
        if (collapsedViewWidth <= 0) {
            throw new IllegalArgumentException("Parameter 'collapsedViewWidth' can not be less or equal than 0!");
        }
        savedState.collapsedViewWidth = collapsedViewWidth;
    }

    @ColorInt
    public int getDividerBackgroundColorInt() {
        return savedState.dividerBackgroundColorInt;
    }

    public void setDividerBackgroundColorRes(
            @ColorRes int dividerBackgroundColorRes) {
        setDividerBackgroundColorInt(
                getColorInt(dividerBackgroundColorRes));
    }

    public void setDividerBackgroundColorInt(
            @ColorInt int dividerBackgroundColorInt) {
        savedState.dividerBackgroundColorInt = dividerBackgroundColorInt;
        dividerView.setBackgroundColor(dividerBackgroundColorInt);
    }

    @ColorInt
    public int getDividerHandlebarColorInt() {
        return savedState.dividerHandlebarColorInt;
    }

    public void setDividerHandlebarColorRes(
            @ColorRes int dividerHandlebarColorRes) {
        setDividerHandlebarColorInt(
                getColorInt(dividerHandlebarColorRes));
    }

    public void setDividerHandlebarColorInt(
            @ColorInt int dividerHandlebarColorInt) {
        savedState.dividerHandlebarColorInt = dividerHandlebarColorInt;
        dividerHandlebarView.setBackgroundColor(dividerHandlebarColorInt);
    }

    private int getColorInt(
            @ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initScrollView() {
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                onTouchActionMove();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                onTouchActionUp();
            }
            return false;
        });
    }

    private void onTouchActionMove() {
        if (scrollX == null) {
            scrollX = scrollView.getScrollX();
        }
    }

    private void onTouchActionUp() {
        if (scrollX != null) {
            int newScrollX = scrollView.getScrollX();
            if (newScrollX < scrollX) {
                if ((scrollX - newScrollX) > SCROLLING_SNAP_DIFF) {
                    scrollToStart();
                } else {
                    scrollToEnd();
                }
            } else if (newScrollX > scrollX) {
                if ((newScrollX - scrollX) > SCROLLING_SNAP_DIFF) {
                    scrollToEnd();
                } else {
                    scrollToStart();
                }
            }
            scrollX = null;
        }
    }

    private void scrollToStart() {
        scrollView.post(() -> {
            scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            if (listener != null) {
                listener.onLeftViewSnapped();
            }
        });
    }

    private void scrollToEnd() {
        scrollView.post(() -> {
            scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            if (listener != null) {
                listener.onRightViewSnapped();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 3) { // Container + left + right views
            throw new IllegalStateException("Sliding divider must contain exactly two (left and right) views!");
        }

        boolean leftViewAdded = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView != containerView) {
                if (leftViewAdded) {
                    rightViewContent = childView;
                } else {
                    leftViewAdded = true;
                    leftViewContent = childView;
                }
            }
        }

        removeAllViews();

        addView(containerView);

        ViewGroup.LayoutParams leftViewLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        leftView.addView(leftViewContent, leftViewLayoutParams);

        ViewGroup.LayoutParams rightViewLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rightView.addView(rightViewContent, rightViewLayoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        handler.post(() -> {
            int expandedWidth = calculateExpandedWidth();
            setExpandedViewWidth(leftView, expandedWidth);
            setExpandedViewWidth(rightView, expandedWidth);
        });
    }

    private int calculateExpandedWidth() {
        int viewWidth = getMeasuredWidth();
        int dividerWidth = dividerView.getWidth();
        int collapsedViewWidth = Math.min(savedState.collapsedViewWidth, viewWidth - dividerWidth);
        return viewWidth - dividerWidth - collapsedViewWidth;
    }

    public int getExpandedViewWidth() {
        return calculateExpandedWidth();
    }

    private void setExpandedViewWidth(
            @NonNull View view,
            int expandedViewWidth) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = expandedViewWidth;
        view.setLayoutParams(layoutParams);
    }

    public interface Listener {

        void onLeftViewSnapped();

        void onRightViewSnapped();
    }

    private static class SavedState implements Parcelable {

        Parcelable superSavedState;

        int collapsedViewWidth;
        int dividerBackgroundColorInt;
        int dividerHandlebarColorInt;

        SavedState() {
        }

        public SavedState(
                Parcel parcel) {
            // List view
            collapsedViewWidth = parcel.readInt();
            dividerBackgroundColorInt = parcel.readInt();
            dividerHandlebarColorInt = parcel.readInt();
        }

        public void update(
                SavedState savedState) {
            if (savedState != null) {
                // List view
                collapsedViewWidth = savedState.collapsedViewWidth;
                dividerBackgroundColorInt = savedState.dividerBackgroundColorInt;
                dividerHandlebarColorInt = savedState.dividerHandlebarColorInt;
            }
        }

        @Override
        public void writeToParcel(
                Parcel out,
                int flags) {
            // List view
            out.writeInt(collapsedViewWidth);
            out.writeInt(dividerBackgroundColorInt);
            out.writeInt(dividerHandlebarColorInt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}