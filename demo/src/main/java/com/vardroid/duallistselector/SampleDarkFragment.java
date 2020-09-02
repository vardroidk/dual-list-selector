package com.vardroid.duallistselector;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.vardroid.duallistselector.data.Movie;
import com.vardroid.duallistselector.data.provider.MovieProvider;
import com.vardroid.duallistselector.databinding.FragmentSampleDarkBinding;
import com.vardroid.duallistselector.duallist.DualListSelectorView;
import com.vardroid.duallistselector.list.ListItemIInsertionOrder;
import com.vardroid.duallistselector.list.SelectorListItem;
import com.vardroid.duallistselector.util.ConverterUtils;

import java.util.List;

public class SampleDarkFragment extends Fragment {

    private FragmentSampleDarkBinding binding;

    public SampleDarkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSampleDarkBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        List<Movie> movies = MovieProvider.getInstance().getMovies(getActivity());
        List<SelectorListItem> items = ConverterUtils.convertList(
                movies,
                movie -> SelectorListItem.builder()
                        .id(String.valueOf(movie.getId()))                              // Mandatory, must be unique
                        .primaryText(movie.getTitle())                                  // Mandatory, non-null
                        .secondaryText(movie.getYear() + " - " + movie.getDirector())   // Optional, nullable
                        .imageLoader((imageView, defaultImageHolder) ->                 // Optional, nullable, default (letter) image can be used as placeholder
                                Glide                                                   // Glide is just an example, any image loading implementation can be used
                                        .with(getContext())
                                        .load(movie.getPosterImageUrl())
                                        .placeholder(defaultImageHolder.get())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView))
                        .build()
        );

        binding.dualListSelectorView.setItems(items);

        binding.dualListSelectorView.setListBackgroundColorInt(Color.parseColor("#36454F"));
        binding.dualListSelectorView.setShowTitle(true);
        binding.dualListSelectorView.setTitleSize(convertDpToPixel(20));
        binding.dualListSelectorView.setTitleColorInt(Color.parseColor("#ADCBE3"));
        binding.dualListSelectorView.setSelectableListTitleText("Best movies of all times");
        binding.dualListSelectorView.setSelectedListTitleText("My bucket list");
        binding.dualListSelectorView.setShowEmptyListText(true);
        binding.dualListSelectorView.setSelectableEmptyListText("No more movies to select...");
        binding.dualListSelectorView.setSelectedEmptyListText("No movie selected yet...");
        binding.dualListSelectorView.setShowListItemAnimation(true);
        binding.dualListSelectorView.setScrollToInsertedListItem(false);
        binding.dualListSelectorView.setSelectedListItemInsertionOrder(ListItemIInsertionOrder.BEGINNING);
        binding.dualListSelectorView.setShowListItemDivider(true);
        binding.dualListSelectorView.setListItemDividerHeight(convertDpToPixel(2));
        binding.dualListSelectorView.setListItemDividerColorInt(Color.parseColor("#ADCBE3"));
        binding.dualListSelectorView.setListItemImageSize(convertDpToPixel(80));
        binding.dualListSelectorView.setListItemPrimaryTextSize(convertDpToPixel(18));
        binding.dualListSelectorView.setListItemPrimaryTextColorInt(Color.parseColor("#FAD9C1"));
        binding.dualListSelectorView.setListItemSecondaryTextSize(convertDpToPixel(16));
        binding.dualListSelectorView.setListItemSecondaryTextColorInt(Color.parseColor("#CCCCCC"));
        binding.dualListSelectorView.setDividerHandlebarColorInt(Color.parseColor("#ADCBE3"));

        binding.dualListSelectorView.setSnapChangeListener(new DualListSelectorView.SnapChangeListener() {
            @Override
            public void onSelectableListSnapped() {
                showMessage("Selectable (left) list is snapped");
            }

            @Override
            public void onSelectedListSnapped() {
                showMessage("Selected (right) list is snapped");
            }
        });

        binding.dualListSelectorView.setSelectionChangeListener(new DualListSelectorView.SelectionChangeListener() {
            @Override
            public void onItemSelected(SelectorListItem item) {
                showMessage("'" + item.getPrimaryText() + "' is selected");
            }

            @Override
            public void onItemDeselected(SelectorListItem item) {
                showMessage("'" + item.getPrimaryText() + "' is removed from selection");
            }
        });
    }

    private void showMessage(
            @NonNull String message) {
        Snackbar.make(binding.dualListSelectorView, message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private int convertDpToPixel(
            int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
}
