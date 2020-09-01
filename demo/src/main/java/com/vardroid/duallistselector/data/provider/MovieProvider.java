package com.vardroid.duallistselector.data.provider;

import android.content.Context;

import com.vardroid.duallistselector.data.Movie;

import java.util.List;

public class MovieProvider {

    private static final MovieProvider INSTANCE = new MovieProvider();

    public static MovieProvider getInstance() {
        return INSTANCE;
    }

    private final DataProvider<Movie> dataProvider;

    private MovieProvider() {
        this.dataProvider = new DataProvider<>("movies.json");
    }

    public List<Movie> getMovies(
            Context context) {
        return dataProvider.getList(context, Movie.class);
    }
}
