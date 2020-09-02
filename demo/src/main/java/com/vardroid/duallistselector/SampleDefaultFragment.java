package com.vardroid.duallistselector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vardroid.duallistselector.data.Movie;
import com.vardroid.duallistselector.data.provider.MovieProvider;
import com.vardroid.duallistselector.databinding.FragmentSampleDefaultBinding;
import com.vardroid.duallistselector.list.SelectorListItem;
import com.vardroid.duallistselector.util.ConverterUtils;

import java.util.List;

public class SampleDefaultFragment extends Fragment {

    private FragmentSampleDefaultBinding binding;

    public SampleDefaultFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSampleDefaultBinding.inflate(getLayoutInflater());
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
        List<Movie> movies = MovieProvider.getInstance().getMovies(getContext());
        List<SelectorListItem> items = ConverterUtils.convertList(
                movies,
                movie -> SelectorListItem.builder()
                        .id(String.valueOf(movie.getId()))                              // Mandatory
                        .primaryText(movie.getTitle())                                  // Mandatory
                        .build()
        );

        binding.dualListSelectorView.setItems(items);
    }
}
