package com.vardroid.duallistselector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vardroid.duallistselector.data.Movie;
import com.vardroid.duallistselector.data.provider.MovieProvider;
import com.vardroid.duallistselector.databinding.FragmentSampleCoolBinding;
import com.vardroid.duallistselector.list.SelectorListItem;
import com.vardroid.duallistselector.util.ConverterUtils;

import java.util.List;

public class SampleCoolFragment extends Fragment {

    private FragmentSampleCoolBinding binding;

    public SampleCoolFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSampleCoolBinding.inflate(getLayoutInflater());
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
                        .id(String.valueOf(movie.getId()))                              // Mandatory
                        .primaryText(movie.getTitle())                                  // Mandatory
                        .secondaryText(movie.getYear() + " - " + movie.getDirector())   // Optional
                        .imageLoader((imageView, defaultImageHolder) ->                 // Optional, default (letter) image can be used as placeholder
                                Glide
                                        .with(getContext())
                                        .load(movie.getPosterImageUrl())
                                        .placeholder(defaultImageHolder.get())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView))
                        .build()
        );

        binding.dualListSelectorView.setItems(items);
    }
}
