package com.vardroid.duallistselector;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vardroid.duallistselector.data.Movie;
import com.vardroid.duallistselector.data.provider.MovieProvider;
import com.vardroid.duallistselector.duallist.DualListSelectorView;
import com.vardroid.duallistselector.list.SelectorListItem;
import com.vardroid.duallistselector.util.ConverterUtils;

import java.util.List;

public class SampleSingleActivity extends AppCompatActivity {

    private DualListSelectorView dualListSelectorView;

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_single);
        dualListSelectorView = findViewById(R.id.dual_list_selector_view);
        initViews();
    }

    private void initViews() {
        List<Movie> movies = MovieProvider.getInstance().getMovies(this);
        List<SelectorListItem> items = ConverterUtils.convertList(
                movies,
                movie -> SelectorListItem.builder()
                        .id(String.valueOf(movie.getId()))                              // Mandatory
                        .primaryText(movie.getTitle())                                  // Mandatory
                        .secondaryText(movie.getYear() + " - " + movie.getDirector())   // Optional
                        .imageLoader((imageView, defaultImageHolder) ->                 // Optional, default (letter) image can be used as placeholder
                                Glide
                                        .with(SampleSingleActivity.this)
                                        .load(movie.getPosterImageUrl())
                                        .placeholder(defaultImageHolder.get())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView))
                        .build()
        );

        dualListSelectorView.setItems(items);
    }
}