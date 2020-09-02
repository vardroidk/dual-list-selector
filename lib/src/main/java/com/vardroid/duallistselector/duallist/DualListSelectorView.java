package com.vardroid.duallistselector.duallist;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.common.collect.ImmutableList;
import com.vardroid.duallistselector.R;
import com.vardroid.duallistselector.divider.SlidingDividerView;
import com.vardroid.duallistselector.list.ListItemAnimationDirection;
import com.vardroid.duallistselector.list.ListItemIInsertionOrder;
import com.vardroid.duallistselector.list.SelectorListAdapterItem;
import com.vardroid.duallistselector.list.SelectorListItem;
import com.vardroid.duallistselector.list.SelectorListView;
import com.vardroid.duallistselector.util.SelectorListUtils;
import com.vardroid.duallistselector.util.TouchEventDelegator;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class DualListSelectorView extends LinearLayout {

    private final SavedState savedState = new SavedState();

    private SlidingDividerView slidingDividerView;
    private View transparentView;
    private SelectorListView selectableListView;
    private SelectorListView selectedListView;

    private SelectionChangeListener selectionChangeListener;
    private SnapChangeListener snapChangeListener;

    public interface SelectionChangeListener {

        void onItemSelected(
                SelectorListItem item);

        void onItemDeselected(
                SelectorListItem item);

    }

    public interface SnapChangeListener {

        void onSelectableListSnapped();

        void onSelectedListSnapped();

    }

    public DualListSelectorView(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        View rootView = inflate(getContext(), R.layout.layout_dual_list_selector_view, this);
        inflateViews(rootView);
        initViews(attrs);
    }

    @Override
    protected void onRestoreInstanceState(
            Parcelable state) {
        SavedState superSavedState = (SavedState) state;
        super.onRestoreInstanceState(superSavedState.superSavedState);
        savedState.update(superSavedState);
        setSavedItems();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        savedState.superSavedState = super.onSaveInstanceState();
        savedState.selectableListItemIds = SelectorListUtils.findItemIds(selectableListView.getItems());
        savedState.selectedListItemIds = SelectorListUtils.findItemIds(selectedListView.getItems());
        return savedState;
    }

    private void inflateViews(
            @NonNull View rootView) {
        slidingDividerView = rootView.findViewById(R.id.sliding_divider_view);
        transparentView = rootView.findViewById(R.id.transparent_view);
        selectableListView = rootView.findViewById(R.id.selectable_list_view);
        selectedListView = rootView.findViewById(R.id.selected_list_view);
    }

    private void initViews(
            @NonNull AttributeSet attrs) {
        initAttributes(attrs);
        initSlidingDividerView();
        initSelectableListView();
        initSelectedListView();
    }

    private void initAttributes(
            @NonNull AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.DualListSelectorView);

        int defaultListBackgroundColorInt = getColorInt(R.color.default_list_background_color);
        setListBackgroundColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_list_background_color,
                defaultListBackgroundColorInt));

        boolean defaultShowTitle = getResources().getBoolean(R.bool.default_show_title);
        setShowTitle(typedArray.getBoolean(
                R.styleable.DualListSelectorView_dls_show_title,
                defaultShowTitle));

        float defaultTitleSize = getResources().getDimension(R.dimen.default_title_size);
        setTitleSize(typedArray.getDimension(
                R.styleable.DualListSelectorView_dls_title_size,
                defaultTitleSize));

        int defaultTitleColorInt = getColorInt(R.color.default_title_color);
        setTitleColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_title_color,
                defaultTitleColorInt));

        setSelectableListTitleText(typedArray.getString(
                R.styleable.DualListSelectorView_dls_selectable_list_title_text));

        setSelectedListTitleText(typedArray.getString(
                R.styleable.DualListSelectorView_dls_selected_list_title_text));

        boolean defaultShowEmptyListText = getResources().getBoolean(R.bool.default_show_empty_list_text);
        setShowEmptyListText(typedArray.getBoolean(
                R.styleable.DualListSelectorView_dls_show_empty_list_text,
                defaultShowEmptyListText));

        setSelectableEmptyListText(typedArray.getString(
                R.styleable.DualListSelectorView_dls_selectable_empty_list_text));

        setSelectedEmptyListText(typedArray.getString(
                R.styleable.DualListSelectorView_dls_selected_empty_list_text));

        int selectedListItemInsertionOrderIndex = typedArray.getInt(
                R.styleable.DualListSelectorView_dls_selected_list_item_insertion_order,
                ListItemIInsertionOrder.BEGINNING.ordinal());
        setSelectedListItemInsertionOrder(ListItemIInsertionOrder.values()[selectedListItemInsertionOrderIndex]);

        boolean defaultScrollToInsertedListItem = getResources().getBoolean(R.bool.default_scroll_to_inserted_list_item);
        setScrollToInsertedListItem(typedArray.getBoolean(
                R.styleable.DualListSelectorView_dls_scroll_to_inserted_list_item,
                defaultScrollToInsertedListItem));

        boolean defaultShowListItemAnimation = getResources().getBoolean(R.bool.default_show_list_item_animation);
        setShowListItemAnimation(typedArray.getBoolean(
                R.styleable.DualListSelectorView_dls_show_list_item_animation,
                defaultShowListItemAnimation));

        boolean defaultShowListItemDivider = getResources().getBoolean(R.bool.default_show_list_item_divider);
        setShowListItemDivider(typedArray.getBoolean(
                R.styleable.DualListSelectorView_dls_show_list_item_divider,
                defaultShowListItemDivider));

        float defaultListItemDividerHeight = getResources().getDimension(R.dimen.default_list_item_divider_height);
        setListItemDividerHeight(typedArray.getDimension(
                R.styleable.DualListSelectorView_dls_list_item_divider_height,
                defaultListItemDividerHeight));

        int defaultListItemDividerColorInt = getColorInt(R.color.default_list_item_divider_color);
        setListItemDividerColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_list_item_divider_color,
                defaultListItemDividerColorInt));

        int defaultListItemImageSize = getResources().getDimensionPixelSize(R.dimen.default_list_item_image_size);
        setListItemImageSize(typedArray.getDimensionPixelSize(
                R.styleable.DualListSelectorView_dls_list_item_image_size,
                defaultListItemImageSize));

        float defaultListItemPrimaryTextSize = getResources().getDimension(R.dimen.default_list_item_primary_text_size);
        setListItemPrimaryTextSize(typedArray.getDimension(
                R.styleable.DualListSelectorView_dls_list_item_primary_text_size,
                defaultListItemPrimaryTextSize));

        int defaultListItemPrimaryTextColorInt = getColorInt(R.color.default_list_item_primary_text_color);
        setListItemPrimaryTextColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_list_item_primary_text_color,
                defaultListItemPrimaryTextColorInt));

        float defaultListItemSecondaryTextSize = getResources().getDimension(R.dimen.default_list_item_secondary_text_size);
        setListItemSecondaryTextSize(typedArray.getDimension(
                R.styleable.DualListSelectorView_dls_list_item_secondary_text_size,
                defaultListItemSecondaryTextSize));

        int defaultListItemSecondaryTextColorInt = getColorInt(R.color.default_list_item_secondary_text_color);
        setListItemSecondaryTextColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_list_item_secondary_text_color,
                defaultListItemSecondaryTextColorInt));

        int defaultDividerBackgroundColorInt = getColorInt(R.color.default_divider_background_color);
        setDividerBackgroundColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_divider_background_color,
                defaultDividerBackgroundColorInt));

        int defaultDividerHandlebarColorInt = getColorInt(R.color.default_divider_handlebar_color);
        setDividerHandlebarColorInt(typedArray.getColor(
                R.styleable.DualListSelectorView_dls_divider_handlebar_color,
                defaultDividerHandlebarColorInt));

        typedArray.recycle();
    }

    private void initSlidingDividerView() {
        slidingDividerView.setTouchEventDelegator(new TouchEventDelegator(
                slidingDividerView.getScrollView(),
                selectableListView,
                transparentView
        ));
        slidingDividerView.setListener(new SlidingDividerView.Listener() {
            @Override
            public void onLeftViewSnapped() {
                if (snapChangeListener != null) {
                    snapChangeListener.onSelectableListSnapped();
                }
            }

            @Override
            public void onRightViewSnapped() {
                if (snapChangeListener != null) {
                    snapChangeListener.onSelectedListSnapped();
                }
            }
        });
    }

    private void initSelectableListView() {
        selectableListView.setAnimationDirection(ListItemAnimationDirection.RIGHT);
        selectableListView.setListItemInsertionOrder(ListItemIInsertionOrder.ORIGINAL);
        selectableListView.setListener(item -> {
            selectableListView.removeItem(item);
            selectedListView.addItem(item);
            if (selectionChangeListener != null) {
                selectionChangeListener.onItemSelected(item.getItem());
            }
        });

        // Sliding divider view is not yet rendered, therefore we have to listen for layout change to gain size info
        slidingDividerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                slidingDividerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup.LayoutParams selectableListViewLayoutParams = selectableListView.getLayoutParams();
                selectableListViewLayoutParams.width = slidingDividerView.getExpandedViewWidth();
                selectableListView.setLayoutParams(selectableListViewLayoutParams);
            }
        });
    }

    private void initSelectedListView() {
        selectedListView.setAnimationDirection(ListItemAnimationDirection.LEFT);
        selectedListView.setListener(item -> {
            selectableListView.addItem(item);
            selectedListView.removeItem(item);
            if (selectionChangeListener != null) {
                selectionChangeListener.onItemDeselected(item.getItem());
            }
        });
    }

    @ColorInt
    public int getListBackgroundColor() {
        return selectableListView.getListBackgroundColor();
    }

    public void setListBackgroundColorRes(
            @ColorRes int listBackgroundColorRes) {
        selectableListView.setListBackgroundColorRes(listBackgroundColorRes);
        selectedListView.setListBackgroundColorRes(listBackgroundColorRes);
    }

    public void setListBackgroundColorInt(
            @ColorInt int listBackgroundColorInt) {
        selectableListView.setListBackgroundColorInt(listBackgroundColorInt);
        selectedListView.setListBackgroundColorInt(listBackgroundColorInt);
    }

    public boolean isShowTitle() {
        return selectableListView.isShowTitle();
    }

    public void setShowTitle(
            boolean showTitle) {
        selectableListView.setShowTitle(showTitle);
        selectedListView.setShowTitle(showTitle);
    }

    public float getTitleSize() {
        return selectableListView.getTitleSize();
    }

    public void setTitleSize(
            float titleSize) {
        selectableListView.setTitleSize(titleSize);
        selectedListView.setTitleSize(titleSize);
    }

    @ColorInt
    public int getTitleColor() {
        return selectableListView.getTitleColor();
    }

    public void setTitleColorRes(
            @ColorRes int titleColorRes) {
        selectableListView.setTitleColorRes(titleColorRes);
        selectedListView.setTitleColorRes(titleColorRes);
    }

    public void setTitleColorInt(
            @ColorInt int titleColorInt) {
        selectableListView.setTitleColorInt(titleColorInt);
        selectedListView.setTitleColorInt(titleColorInt);
    }

    public String getSelectableListTitleText() {
        return selectableListView.getTitleText();
    }

    public void setSelectableListTitleText(
            @Nullable String title) {
        selectableListView.setTitleText(title);
    }

    public String getSelectedListTitleText() {
        return selectedListView.getTitleText();
    }

    public void setSelectedListTitleText(
            @Nullable String title) {
        selectedListView.setTitleText(title);
    }

    public boolean isShowEmptyListText() {
        return selectableListView.isShowEmptyListText();
    }

    public void setShowEmptyListText(
            boolean showEmptyListText) {
        selectableListView.setShowEmptyListText(showEmptyListText);
        selectedListView.setShowEmptyListText(showEmptyListText);
    }

    public String getSelectableEmptyListText() {
        return selectableListView.getEmptyListText();
    }

    public void setSelectableEmptyListText(
            @Nullable String emptyListText) {
        selectableListView.setEmptyListText(emptyListText);
    }

    public String getSelectedEmptyListText() {
        return selectedListView.getEmptyListText();
    }

    public void setSelectedEmptyListText(
            @Nullable String emptyListText) {
        selectedListView.setEmptyListText(emptyListText);
    }

    public ListItemIInsertionOrder getSelectedListItemInsertionOrder() {
        return selectedListView.getListItemInsertionOrder();
    }

    public void setSelectedListItemInsertionOrder(
            @NonNull ListItemIInsertionOrder selectedListItemInsertionOrder) {
        selectedListView.setListItemInsertionOrder(selectedListItemInsertionOrder);
    }

    public boolean isScrollToInsertedListItem() {
        return selectableListView.isScrollToInsertedListItem();
    }

    public void setScrollToInsertedListItem(
            boolean scrollToInsertedListItem) {
        selectableListView.setScrollToInsertedListItem(scrollToInsertedListItem);
        selectedListView.setScrollToInsertedListItem(scrollToInsertedListItem);
    }

    public boolean isShowListItemAnimation() {
        return selectableListView.isShowListItemAnimation();
    }

    public void setShowListItemAnimation(
            boolean showListItemAnimation) {
        selectableListView.setShowListItemAnimation(showListItemAnimation);
        selectedListView.setShowListItemAnimation(showListItemAnimation);
    }

    public boolean isShowListItemDivider() {
        return selectableListView.isShowListItemDivider();
    }

    public void setShowListItemDivider(
            boolean showListItemDivider) {
        selectableListView.setShowListItemDivider(showListItemDivider);
        selectedListView.setShowListItemDivider(showListItemDivider);
    }

    public float getListItemDividerHeight() {
        return selectableListView.getListItemDividerHeight();
    }

    public void setListItemDividerHeight(
            float listItemDividerHeight) {
        selectableListView.setListItemDividerHeight(listItemDividerHeight);
        selectedListView.setListItemDividerHeight(listItemDividerHeight);
    }

    @ColorInt
    public int getListItemDividerColor() {
        return selectableListView.getListItemDividerColor();
    }

    public void setListItemDividerColorRes(
            @ColorRes int listItemDividerColorRes) {
        selectableListView.setListItemDividerColorRes(listItemDividerColorRes);
        selectedListView.setListItemDividerColorRes(listItemDividerColorRes);
    }

    public void setListItemDividerColorInt(
            @ColorInt int listItemDividerColorInt) {
        selectableListView.setListItemDividerColorInt(listItemDividerColorInt);
        selectedListView.setListItemDividerColorInt(listItemDividerColorInt);
    }

    public int getListItemImageSize() {
        return selectableListView.getListItemImageSize();
    }

    public void setListItemImageSize(
            int listItemImageSize) {
        selectableListView.setListItemImageSize(listItemImageSize);
        selectedListView.setListItemImageSize(listItemImageSize);
        calculateAndSetCollapsedViewWidth(listItemImageSize);
    }

    private void calculateAndSetCollapsedViewWidth(
            int listItemImageSize) {
        int listItemImageMarginHorizontal = getResources().getDimensionPixelSize(R.dimen.list_item_margin_horizontal);
        int collapsedViewWidth = listItemImageSize + (2 * listItemImageMarginHorizontal);
        setCollapsedViewWidth(collapsedViewWidth);
    }

    private void setCollapsedViewWidth(
            int collapsedViewWidth) {
        slidingDividerView.setCollapsedViewWidth(collapsedViewWidth);
    }

    public float getListItemPrimaryTextSize() {
        return selectableListView.getListItemPrimaryTextSize();
    }

    public void setListItemPrimaryTextSize(
            float listItemPrimaryTextSize) {
        selectableListView.setListItemPrimaryTextSize(listItemPrimaryTextSize);
        selectedListView.setListItemPrimaryTextSize(listItemPrimaryTextSize);
    }

    @ColorInt
    public int getListItemPrimaryTextColor() {
        return selectableListView.getListItemPrimaryTextColor();
    }

    public void setListItemPrimaryTextColorRes(
            @ColorRes int listItemPrimaryTextColorRes) {
        selectableListView.setListItemPrimaryTextColorRes(listItemPrimaryTextColorRes);
        selectedListView.setListItemPrimaryTextColorRes(listItemPrimaryTextColorRes);
    }

    public void setListItemPrimaryTextColorInt(
            @ColorInt int listItemPrimaryTextColorInt) {
        selectableListView.setListItemPrimaryTextColorInt(listItemPrimaryTextColorInt);
        selectedListView.setListItemPrimaryTextColorInt(listItemPrimaryTextColorInt);
    }

    public float getListItemSecondaryTextSize() {
        return selectableListView.getListItemSecondaryTextSize();
    }

    public void setListItemSecondaryTextSize(
            float listItemSecondaryTextSize) {
        selectableListView.setListItemSecondaryTextSize(listItemSecondaryTextSize);
        selectedListView.setListItemSecondaryTextSize(listItemSecondaryTextSize);
    }

    @ColorInt
    public int getListItemSecondaryTextColor() {
        return selectableListView.getListItemSecondaryTextColor();
    }

    public void setListItemSecondaryTextColorRes(
            @ColorRes int listItemSecondaryTextColorRes) {
        selectableListView.setListItemSecondaryTextColorRes(listItemSecondaryTextColorRes);
        selectedListView.setListItemSecondaryTextColorRes(listItemSecondaryTextColorRes);
    }

    public void setListItemSecondaryTextColorInt(
            @ColorInt int listItemSecondaryTextColorInt) {
        selectableListView.setListItemSecondaryTextColorInt(listItemSecondaryTextColorInt);
        selectedListView.setListItemSecondaryTextColorInt(listItemSecondaryTextColorInt);
    }

    @ColorInt
    public int getDividerBackgroundColorInt() {
        return slidingDividerView.getDividerBackgroundColorInt();
    }

    public void setDividerBackgroundColorRes(
            @ColorRes int dividerBackgroundColorRes) {
        slidingDividerView.setDividerBackgroundColorRes(dividerBackgroundColorRes);
    }

    public void setDividerBackgroundColorInt(
            @ColorInt int dividerBackgroundColorInt) {
        slidingDividerView.setDividerBackgroundColorInt(dividerBackgroundColorInt);
    }

    @ColorInt
    public int getDividerHandlebarColorInt() {
        return slidingDividerView.getDividerHandlebarColorInt();
    }

    public void setDividerHandlebarColorRes(
            @ColorRes int dividerHandlebarColorRes) {
        slidingDividerView.setDividerHandlebarColorRes(dividerHandlebarColorRes);
    }

    public void setDividerHandlebarColorInt(
            @ColorInt int dividerHandlebarColorInt) {
        slidingDividerView.setDividerHandlebarColorInt(dividerHandlebarColorInt);
    }

    private void setSavedItems() {
        ImmutableList<SelectorListAdapterItem> mergedItems = new ImmutableList.Builder<SelectorListAdapterItem>()
                .addAll(selectableListView.getItems())
                .addAll(selectedListView.getItems())
                .build();

        if (savedState.isListItemIdsAvailable()) {
            ImmutableList<SelectorListAdapterItem> selectableItems = SelectorListUtils.findItems(
                    mergedItems,
                    savedState.selectableListItemIds);
            ImmutableList<SelectorListAdapterItem> selectedItems = SelectorListUtils.findItems(
                    mergedItems,
                    savedState.selectedListItemIds);

            selectableListView.setItems(selectableItems);
            selectedListView.setItems(selectedItems);
        }
    }

    public void setItems(
            @NonNull List<SelectorListItem> selectableItems) {
        setItems(selectableItems, new ArrayList<>());
    }

    public void setItems(
            @NonNull List<SelectorListItem> selectableItems,
            @NonNull List<SelectorListItem> selectedItems) {
        ImmutableList<SelectorListItem> mergedItems = new ImmutableList.Builder<SelectorListItem>()
                .addAll(selectableItems)
                .addAll(selectedItems)
                .build();
        if (!SelectorListUtils.checkUniqueItems(mergedItems)) {
            throw new IllegalStateException("Selectable and selected items must have unique ids!");
        }

        selectableListView.setItems(SelectorListUtils.toAdapterItems(selectableItems));
        selectedListView.setItems(SelectorListUtils.toAdapterItems(selectedItems));
    }

    public List<SelectorListItem> getSelectableItems() {
        return new ArrayList<>(SelectorListUtils.toItems(selectableListView.getItems()));
    }

    public List<SelectorListItem> getSelectedItems() {
        return new ArrayList<>(SelectorListUtils.toItems(selectedListView.getItems()));
    }

    public void setSelectionChangeListener(
            @Nullable SelectionChangeListener selectionChangeListener) {
        this.selectionChangeListener = selectionChangeListener;
    }

    public void setSnapChangeListener(
            @Nullable SnapChangeListener snapChangeListener) {
        this.snapChangeListener = snapChangeListener;
    }

    private int getColorInt(
            @ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    private static class SavedState implements Parcelable {

        Parcelable superSavedState;

        ImmutableList<String> selectableListItemIds;
        ImmutableList<String> selectedListItemIds;

        SavedState() {
        }

        public SavedState(
                Parcel parcel) {
            selectableListItemIds = toStringList(parcel.createStringArray());
            selectedListItemIds = toStringList(parcel.createStringArray());
        }

        private boolean isListItemIdsAvailable() {
            return (selectableListItemIds != null && !selectableListItemIds.isEmpty())
                    || (selectedListItemIds != null && !selectedListItemIds.isEmpty());
        }

        public void update(
                SavedState savedState) {
            if (savedState != null) {
                selectableListItemIds = savedState.selectableListItemIds;
                selectedListItemIds = savedState.selectedListItemIds;
            }
        }

        @Override
        public void writeToParcel(
                Parcel out,
                int flags) {
            out.writeStringArray(toStringArray(selectableListItemIds));
            out.writeStringArray(toStringArray(selectedListItemIds));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        private String[] toStringArray(
                @Nullable List<String> list){
            return list == null
                    ? new String[0]
                    : list.toArray(new String[0]);
        }

        private ImmutableList<String> toStringList(
                @Nullable String[] array){
            return array == null
                    ? ImmutableList.of()
                    : ImmutableList.copyOf(array);
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