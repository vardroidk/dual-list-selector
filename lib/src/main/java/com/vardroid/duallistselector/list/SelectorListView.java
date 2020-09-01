package com.vardroid.duallistselector.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.vardroid.duallistselector.R;
import com.vardroid.duallistselector.util.ListItemDividerDecoration;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import lombok.NonNull;

public class SelectorListView extends LinearLayout {

    private final SavedState savedState = new SavedState();

    private Listener listener;

    @NonNull
    private SelectorListAdapter adapter;

    private TextView titleView;
    private TextView emptyListTextView;
    private RecyclerView recyclerView;

    public SelectorListView(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(true);
        View rootView = inflate(getContext(), R.layout.layout_selector_list_view, this);
        inflateViews(rootView);
        initViews(attrs);
    }

    @Override
    protected void onRestoreInstanceState(
            Parcelable state) {
        SavedState superSavedState = (SavedState) state;
        super.onRestoreInstanceState(superSavedState.superSavedState);
        savedState.update(superSavedState);

        setFirstVisibleItemPosition(savedState.firstVisibleItemPosition);
        // List view attributes
        setListBackgroundColorInt(savedState.listBackgroundColor);
        setShowTitle(savedState.showTitle);
        setTitleSize(savedState.titleSize);
        setTitleColorInt(savedState.titleColor);
        setTitleText(savedState.titleText);
        setShowEmptyListText(savedState.showEmptyListText);
        setEmptyListText(savedState.emptyListText);
        setScrollToInsertedListItem(savedState.scrollToInsertedListItem);
        setShowListItemAnimation(savedState.showListItemAnimation);
        setAnimationDirection(savedState.animationDirection);
        setShowListItemDivider(savedState.showListItemDivider);
        setListItemDividerHeight(savedState.listItemDividerHeight);
        setListItemDividerColorInt(savedState.listItemDividerColor);
        // List item attributes
        setListItemInsertionOrder(savedState.listItemInsertionOrder);
        setListItemImageSize(savedState.listItemImageSize);
        setListItemPrimaryTextSize(savedState.listItemPrimaryTextSize);
        setListItemPrimaryTextColorInt(savedState.listItemPrimaryTextColor);
        setListItemSecondaryTextSize(savedState.listItemSecondaryTextSize);
        setListItemSecondaryTextColorInt(savedState.listItemSecondaryTextColor);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        savedState.superSavedState = super.onSaveInstanceState();
        savedState.firstVisibleItemPosition = getFirstVisibleItemPosition();
        return savedState;
    }

    private void inflateViews(
            @NonNull View rootView) {
        titleView = rootView.findViewById(R.id.title_view);
        emptyListTextView = rootView.findViewById(R.id.empty_list_text);
        recyclerView = rootView.findViewById(R.id.recycler_view);
    }

    private void initViews(
            @NonNull AttributeSet attrs) {
        initList();
        initAttributes(attrs);
    }

    private void initList() {
        adapter = new SelectorListAdapter(getContext());
        adapter.setListener(item -> {
            if (listener != null) {
                listener.onItemSelected(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void initAttributes(
            @NonNull AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.SelectorListView);

        int defaultListBackgroundColorInt = getColorInt(R.color.default_list_background_color);
        setListBackgroundColorInt(typedArray.getColor(
                R.styleable.SelectorListView_sl_background_color,
                defaultListBackgroundColorInt));

        boolean defaultShowTitle = getResources().getBoolean(R.bool.default_show_title);
        setShowTitle(typedArray.getBoolean(
                R.styleable.SelectorListView_sl_show_title,
                defaultShowTitle));

        float defaultTitleSize = getResources().getDimension(R.dimen.default_title_size);
        setTitleSize(typedArray.getDimension(
                R.styleable.SelectorListView_sl_title_size,
                defaultTitleSize));

        int defaultTitleColorInt = getColorInt(R.color.default_title_color);
        setTitleColorInt(typedArray.getColor(
                R.styleable.SelectorListView_sl_title_color,
                defaultTitleColorInt));

        setTitleText(typedArray.getString(
                R.styleable.SelectorListView_sl_title_text));

        boolean defaultShowEmptyListText = getResources().getBoolean(R.bool.default_show_empty_list_text);
        setShowEmptyListText(typedArray.getBoolean(
                R.styleable.SelectorListView_sl_show_empty_list_text,
                defaultShowEmptyListText));

        setEmptyListText(typedArray.getString(
                R.styleable.SelectorListView_sl_empty_list_text));

        int listItemInsertionOrderIndex = typedArray.getInt(
                R.styleable.SelectorListView_sl_list_item_insertion_order,
                ListItemIInsertionOrder.BEGINNING.ordinal());
        setListItemInsertionOrder(ListItemIInsertionOrder.values()[listItemInsertionOrderIndex]);

        boolean defaultScrollToInsertedListItem = getResources().getBoolean(R.bool.default_scroll_to_inserted_list_item);
        setScrollToInsertedListItem(typedArray.getBoolean(
                R.styleable.SelectorListView_sl_scroll_to_inserted_list_item,
                defaultScrollToInsertedListItem));

        boolean defaultShowListItemAnimation = getResources().getBoolean(R.bool.default_show_list_item_animation);
        setShowListItemAnimation(typedArray.getBoolean(
                R.styleable.SelectorListView_sl_show_list_item_animation,
                defaultShowListItemAnimation));

        boolean defaultShowListItemDivider = getResources().getBoolean(R.bool.default_show_list_item_divider);
        setShowListItemDivider(typedArray.getBoolean(
                R.styleable.SelectorListView_sl_show_list_item_divider,
                defaultShowListItemDivider));

        float defaultListItemDividerHeight = getResources().getDimension(R.dimen.default_list_item_divider_height);
        setListItemDividerHeight(typedArray.getDimension(
                R.styleable.SelectorListView_sl_list_item_divider_height,
                defaultListItemDividerHeight));

        int defaultListItemDividerColorInt = getColorInt(R.color.default_list_item_divider_color);
        setListItemDividerColorInt(typedArray.getColor(
                R.styleable.SelectorListView_sl_list_item_divider_color,
                defaultListItemDividerColorInt));

        int defaultListItemImageSize = getResources().getDimensionPixelSize(R.dimen.default_list_item_image_size);
        setListItemImageSize(typedArray.getDimensionPixelSize(
                R.styleable.SelectorListView_sl_list_item_image_size,
                defaultListItemImageSize));

        float defaultListItemPrimaryTextSize = getResources().getDimension(R.dimen.default_list_item_primary_text_size);
        setListItemPrimaryTextSize(typedArray.getDimension(
                R.styleable.SelectorListView_sl_list_item_primary_text_size,
                defaultListItemPrimaryTextSize));

        int defaultListItemPrimaryTextColorInt = getColorInt(R.color.default_list_item_primary_text_color);
        setListItemPrimaryTextColorInt(typedArray.getColor(
                R.styleable.SelectorListView_sl_list_item_primary_text_color,
                defaultListItemPrimaryTextColorInt));

        float defaultListItemSecondaryTextSize = getResources().getDimension(R.dimen.default_list_item_secondary_text_size);
        setListItemSecondaryTextSize(typedArray.getDimension(
                R.styleable.SelectorListView_sl_list_item_secondary_text_size,
                defaultListItemSecondaryTextSize));

        int defaultListItemSecondaryTextColorInt = getColorInt(R.color.default_list_item_secondary_text_color);
        setListItemSecondaryTextColorInt(typedArray.getColor(
                R.styleable.SelectorListView_sl_list_item_secondary_text_color,
                defaultListItemSecondaryTextColorInt));

        typedArray.recycle();
    }

    private int getFirstVisibleItemPosition() {
        int firstVisibleItemPosition = 0;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItemPosition = Math.max(
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition(),
                    0);
        }
        return firstVisibleItemPosition;
    }

    private void setFirstVisibleItemPosition(
            int firstVisibleItemPosition) {
        savedState.firstVisibleItemPosition = firstVisibleItemPosition;
        recyclerView.scrollToPosition(firstVisibleItemPosition);
    }

    @ColorInt
    public int getListBackgroundColor() {
        return savedState.listBackgroundColor;
    }

    public void setListBackgroundColorRes(
            @ColorRes int listBackgroundColorRes) {
        setListBackgroundColorInt(
                getColorInt(listBackgroundColorRes));
    }

    public void setListBackgroundColorInt(
            @ColorInt int listBackgroundColorInt) {
        savedState.listBackgroundColor = listBackgroundColorInt;
        setBackgroundColor(listBackgroundColorInt);
    }

    public boolean isShowTitle() {
        return savedState.showTitle;
    }

    public void setShowTitle(
            boolean showTitle) {
        savedState.showTitle = showTitle;
        titleView.setVisibility(showTitle
                ? View.VISIBLE
                : View.GONE);
    }

    public float getTitleSize() {
        return savedState.titleSize;
    }

    public void setTitleSize(
            float titleSize) {
        savedState.titleSize = titleSize;
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }

    @ColorInt
    public int getTitleColor() {
        return savedState.titleColor;
    }

    public void setTitleColorRes(
            @ColorRes int titleColorRes) {
        setTitleColorInt(
                getColorInt(titleColorRes));
    }

    public void setTitleColorInt(
            @ColorInt int titleColorInt) {
        savedState.titleColor = titleColorInt;
        titleView.setTextColor(titleColorInt);
    }

    public String getTitleText() {
        return savedState.titleText;
    }

    public void setTitleText(
            String titleText) {
        savedState.titleText = titleText;
        titleView.setText(titleText);
    }

    public boolean isShowEmptyListText() {
        return savedState.showEmptyListText;
    }

    public void setShowEmptyListText(
            boolean showEmptyListText) {
        savedState.showEmptyListText = showEmptyListText;
        updateEmptyTextVisibility();
    }

    public String getEmptyListText() {
        return savedState.emptyListText;
    }

    public void setEmptyListText(
            String emptyListText) {
        savedState.emptyListText = emptyListText;
        emptyListTextView.setText(emptyListText);
    }

    public ListItemIInsertionOrder getListItemInsertionOrder() {
        return savedState.listItemInsertionOrder;
    }

    public void setListItemInsertionOrder(
            @NonNull ListItemIInsertionOrder listItemInsertionOrder) {
        savedState.listItemInsertionOrder = listItemInsertionOrder;
        setConfig(getConfig().toBuilder()
                .insertionOrder(listItemInsertionOrder)
                .build());
    }

    public boolean isScrollToInsertedListItem() {
        return savedState.scrollToInsertedListItem;
    }

    public void setScrollToInsertedListItem(
            boolean scrollToInsertedListItem) {
        savedState.scrollToInsertedListItem = scrollToInsertedListItem;
    }

    public boolean isShowListItemAnimation() {
        return savedState.showListItemAnimation;
    }

    public void setShowListItemAnimation(
            boolean showListItemAnimation) {
        savedState.showListItemAnimation = showListItemAnimation;
        setAnimation();
    }

    public ListItemAnimationDirection getAnimationDirection() {
        return savedState.animationDirection;
    }

    public void setAnimationDirection(
            @NonNull ListItemAnimationDirection animationDirection) {
        savedState.animationDirection = animationDirection;
        setAnimation();
    }

    private void setAnimation() {
        if (savedState.showListItemAnimation && savedState.animationDirection != null) {
            switch (savedState.animationDirection) {
                case LEFT:
                    recyclerView.setItemAnimator(new SlideInLeftAnimator());
                    break;
                case RIGHT:
                    recyclerView.setItemAnimator(new SlideInRightAnimator());
                    break;
            }
        } else {
            recyclerView.setItemAnimator(null);
        }
    }

    public boolean isShowListItemDivider() {
        return savedState.showListItemDivider;
    }

    public void setShowListItemDivider(
            boolean showListItemDivider) {
        savedState.showListItemDivider = showListItemDivider;
        setListItemDivider();
    }

    public float getListItemDividerHeight() {
        return savedState.listItemDividerHeight;
    }

    public void setListItemDividerHeight(
            float listItemDividerHeight) {
        savedState.listItemDividerHeight = listItemDividerHeight;
        setListItemDivider();
    }

    @ColorInt
    public int getListItemDividerColor() {
        return savedState.listItemDividerColor;
    }

    public void setListItemDividerColorRes(
            @ColorRes int listItemDividerColorRes) {
        setListItemDividerColorInt(
                getColorInt(listItemDividerColorRes));
    }

    public void setListItemDividerColorInt(
            @ColorInt int listItemDividerColorInt) {
        savedState.listItemDividerColor = listItemDividerColorInt;
        setListItemDivider();
    }

    private void setListItemDivider() {
        removeListItemDecorations();
        if (savedState.showListItemDivider) {
            float listItemPadding = getResources().getDimension(R.dimen.list_item_padding_vertical);
            ListItemDividerDecoration dividerDecoration = new ListItemDividerDecoration(
                    savedState.listItemDividerColor,
                    savedState.listItemDividerHeight,
                    listItemPadding);
            recyclerView.addItemDecoration(dividerDecoration);
            recyclerView.getItemDecorationCount();
        }
    }

    private void removeListItemDecorations() {
        for (int i = 0; i < recyclerView.getItemDecorationCount(); i++) {
            recyclerView.removeItemDecorationAt(i);
        }
    }

    public int getListItemImageSize() {
        return savedState.listItemImageSize;
    }

    public void setListItemImageSize(
            int listItemImageSize) {
        savedState.listItemImageSize = listItemImageSize;
        setConfig(getConfig().toBuilder()
                .imageSize(listItemImageSize)
                .build());
    }

    public float getListItemPrimaryTextSize() {
        return savedState.listItemPrimaryTextSize;
    }

    public void setListItemPrimaryTextSize(
            float listItemPrimaryTextSize) {
        savedState.listItemPrimaryTextSize = listItemPrimaryTextSize;
        setConfig(getConfig().toBuilder()
                .primaryTextSize(listItemPrimaryTextSize)
                .build());
    }

    @ColorInt
    public int getListItemPrimaryTextColor() {
        return savedState.listItemPrimaryTextColor;
    }

    public void setListItemPrimaryTextColorRes(
            @ColorRes int listItemPrimaryTextColorRes) {
        setListItemPrimaryTextColorInt(
                getColorInt(listItemPrimaryTextColorRes));
    }

    public void setListItemPrimaryTextColorInt(
            @ColorInt int listItemPrimaryTextColorInt) {
        savedState.listItemPrimaryTextColor = listItemPrimaryTextColorInt;
        setConfig(getConfig().toBuilder()
                .primaryTextColor(listItemPrimaryTextColorInt)
                .build());
    }

    public float getListItemSecondaryTextSize() {
        return savedState.listItemSecondaryTextSize;
    }

    public void setListItemSecondaryTextSize(
            float listItemSecondaryTextSize) {
        savedState.listItemSecondaryTextSize = listItemSecondaryTextSize;
        setConfig(getConfig().toBuilder()
                .secondaryTextSize(listItemSecondaryTextSize)
                .build());
    }

    @ColorInt
    public int getListItemSecondaryTextColor() {
        return savedState.listItemSecondaryTextColor;
    }

    public void setListItemSecondaryTextColorRes(
            @ColorRes int listItemSecondaryTextColorRes) {
        setListItemSecondaryTextColorInt(
                getColorInt(listItemSecondaryTextColorRes));
    }

    public void setListItemSecondaryTextColorInt(
            @ColorInt int listItemSecondaryTextColorInt) {
        savedState.listItemSecondaryTextColor = listItemSecondaryTextColorInt;
        setConfig(getConfig().toBuilder()
                .secondaryTextColor(listItemSecondaryTextColorInt)
                .build());
    }

    @NonNull
    public ImmutableList<SelectorListAdapterItem> getItems() {
        return adapter.getItems();
    }

    public void setItems(
            @NonNull ImmutableList<SelectorListAdapterItem> items) {
        adapter.setItems(items);
        updateEmptyTextVisibility();
    }

    public void addItem(
            @NonNull SelectorListAdapterItem item) {
        Optional<Integer> position = adapter.addItem(item);
        if (position.isPresent() && savedState.scrollToInsertedListItem) {
            recyclerView.scrollToPosition(position.get());
        }
        updateEmptyTextVisibility();
    }

    public void removeItem(
            @NonNull SelectorListAdapterItem item) {
        adapter.removeItem(item);
        updateEmptyTextVisibility();
    }

    private void updateEmptyTextVisibility() {
        boolean emptyList = adapter.getItems().isEmpty();
        emptyListTextView.setVisibility(emptyList && savedState.showEmptyListText
                ? View.VISIBLE
                : View.GONE);
    }

    public void setListener(
            @Nullable Listener listener) {
        this.listener = listener;
    }

    private int getColorInt(
            @ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private SelectorListConfig getConfig() {
        return adapter.getConfig();
    }

    private void setConfig(
            @NonNull SelectorListConfig config) {
        adapter.setConfig(config);
    }

    public interface Listener {
        void onItemSelected(
                @NonNull SelectorListAdapterItem item);
    }

    private static class SavedState implements Parcelable {

        Parcelable superSavedState;

        int firstVisibleItemPosition;
        // List view attributes
        @ColorInt
        int listBackgroundColor;
        boolean showTitle;
        float titleSize;
        @ColorInt
        int titleColor;
        String titleText;
        boolean showEmptyListText;
        String emptyListText;
        boolean scrollToInsertedListItem;
        boolean showListItemAnimation;
        ListItemAnimationDirection animationDirection;
        boolean showListItemDivider;
        float listItemDividerHeight;
        @ColorInt
        int listItemDividerColor;
        // List item attributes
        ListItemIInsertionOrder listItemInsertionOrder;
        int listItemImageSize;
        float listItemPrimaryTextSize;
        int listItemPrimaryTextColor;
        float listItemSecondaryTextSize;
        int listItemSecondaryTextColor;

        SavedState() {
        }

        public SavedState(
                Parcel parcel) {
            firstVisibleItemPosition = parcel.readInt();
            // List view attributes
            listBackgroundColor = parcel.readInt();
            showTitle = parcel.readInt() == 1;
            titleSize = parcel.readFloat();
            titleColor = parcel.readInt();
            titleText = parcel.readString();
            showEmptyListText = parcel.readInt() == 1;
            emptyListText = parcel.readString();
            scrollToInsertedListItem = parcel.readInt() == 1;
            showListItemAnimation = parcel.readInt() == 1;
            animationDirection = ListItemAnimationDirection.findByName(parcel.readString());
            showListItemDivider = parcel.readInt() == 1;
            listItemDividerHeight = parcel.readFloat();
            listItemDividerColor = parcel.readInt();
            // List item attributes
            listItemInsertionOrder = ListItemIInsertionOrder.findByName(parcel.readString());
            listItemImageSize = parcel.readInt();
            listItemPrimaryTextSize = parcel.readFloat();
            listItemPrimaryTextColor = parcel.readInt();
            listItemSecondaryTextSize = parcel.readFloat();
            listItemSecondaryTextColor = parcel.readInt();
        }

        public void update(
                SavedState savedState) {
            if (savedState != null) {
                firstVisibleItemPosition = savedState.firstVisibleItemPosition;
                // List view attributes
                listBackgroundColor = savedState.listBackgroundColor;
                showTitle = savedState.showTitle;
                titleSize = savedState.titleSize;
                titleColor = savedState.titleColor;
                titleText = savedState.titleText;
                showEmptyListText = savedState.showEmptyListText;
                emptyListText = savedState.emptyListText;
                scrollToInsertedListItem = savedState.scrollToInsertedListItem;
                showListItemAnimation = savedState.showListItemAnimation;
                animationDirection = savedState.animationDirection;
                showListItemDivider = savedState.showListItemDivider;
                listItemDividerHeight = savedState.listItemDividerHeight;
                listItemDividerColor = savedState.listItemDividerColor;
                // List item attributes
                listItemInsertionOrder = savedState.listItemInsertionOrder;
                listItemImageSize = savedState.listItemImageSize;
                listItemPrimaryTextSize = savedState.listItemPrimaryTextSize;
                listItemPrimaryTextColor = savedState.listItemPrimaryTextColor;
                listItemSecondaryTextSize = savedState.listItemSecondaryTextSize;
                listItemSecondaryTextColor = savedState.listItemSecondaryTextColor;
            }
        }

        @Override
        public void writeToParcel(
                Parcel out,
                int flags) {
            out.writeInt(firstVisibleItemPosition);
            // List view attributes
            out.writeInt(listBackgroundColor);
            out.writeInt(showTitle ? 1 : 0);
            out.writeFloat(titleSize);
            out.writeInt(titleColor);
            out.writeString(titleText);
            out.writeInt(showEmptyListText ? 1 : 0);
            out.writeString(emptyListText);
            out.writeInt(scrollToInsertedListItem ? 1 : 0);
            out.writeInt(showListItemAnimation ? 1 : 0);
            out.writeString(ListItemAnimationDirection.getName(animationDirection));
            out.writeInt(showListItemDivider ? 1 : 0);
            out.writeFloat(listItemDividerHeight);
            out.writeInt(listItemDividerColor);
            // List item attributes
            out.writeString(ListItemIInsertionOrder.getName(listItemInsertionOrder));
            out.writeInt(listItemImageSize);
            out.writeFloat(listItemPrimaryTextSize);
            out.writeInt(listItemPrimaryTextColor);
            out.writeFloat(listItemSecondaryTextSize);
            out.writeInt(listItemSecondaryTextColor);
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