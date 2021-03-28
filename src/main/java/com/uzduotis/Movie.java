package com.uzduotis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"title", "year", "rank", "genre", "plot", "actors"})
public class Movie {

    private String Title;
    private String Year;
    private double Rank;
    private String Genre;
    private String Plot;
    private String Actors;

    public Movie(String title,
                 String year,
                 double rank,
                 String genre,
                 String plot,
                 String actors) {
        Title = title;
        Year = year;
        Rank = rank;
        Genre = genre;
        Plot = plot;
        Actors = actors;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getRank() {
        return Rank;
    }

    public void setRank(double rank) {
        Rank = rank;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }
}
