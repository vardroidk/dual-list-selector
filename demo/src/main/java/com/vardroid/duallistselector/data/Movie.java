package com.vardroid.duallistselector.data;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @SerializedName("director")
    private String director;

    @SerializedName("year")
    private int year;

    @SerializedName("poster_image_url")
    private String posterImageUrl;

    public Long getId() {
        return id;
    }

    public void setId(
            Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(
            String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(
            int year) {
        this.year = year;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(
            String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }
}
