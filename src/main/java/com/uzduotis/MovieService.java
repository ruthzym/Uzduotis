package com.uzduotis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    public static final String API_URL = "http://www.omdbapi.com/";
    public static final String API_KEY = "?apikey=3711542b";

    //Method return list of sorted movie list of all 3 years
    public List<Movie> allMovies(String year1, String year2, String year3) throws InterruptedException, JSONException, IOException {
        var movies = getMovies(year1);
        movies.addAll(getMovies(year2));
        movies.addAll(getMovies(year3));

        var average = Average(movies);
        average.sort(Comparator.comparing(Movie::getRank).thenComparing(Movie::getTitle));

        return average;
    }

    //Method checks if the list of movies by year has more than 50 movies, sets pagination and returns list of movies.
    private List<Movie> getMovies(String year) throws IOException, InterruptedException, JSONException {
        List<Movie> movies = new ArrayList<Movie>();
        var jobj = getMovieByYear(year, "&page=1");
        movies.addAll(get(jobj));

        var totalResults =  jobj.getInt("totalResults");
        int page = (totalResults > 50) ? 5 : (int)Math.ceil(totalResults/10);

        for (int i = 2; i <= page; i++){
            String pagination = "&page=" + String.valueOf(i);
            var obj = getMovieByYear(year, pagination);
            movies.addAll(get(obj));
        }

        return movies;
    }

    //Method parses JSON object to get array of movies. Then it calls method to fetch specific movie data by id. Method returns list of movies.
    private List<Movie> get(JSONObject jobj) throws JSONException, IOException, InterruptedException {
        List<Movie> movies = new ArrayList<Movie>();
        var array = jobj.getJSONArray("Search");

        for (int i = 0; i < array.length(); i++){
            JSONObject jsonMovie = array.getJSONObject(i);
            String id = jsonMovie.getString("imdbID");
            var movie = getMovieById(id);
            movies.add(movie);
        }
        return  movies;
    }

    //Method calculates average of movies ranks and returns list of movies that have higher ranks than average
    private List<Movie> Average(List<Movie> movies){
        List<Movie> averageMovies = new ArrayList<Movie>();
        var average = movies.stream()
                .mapToDouble(a -> a.getRank())
                .average();
        System.out.println(average);
        for (Movie movie : movies){
            if (movie.getRank() > average.getAsDouble()){
                averageMovies.add(movie);
            }
        }
        return averageMovies;
    }

    //Method fetches movie data of selected year from api and returns JSON object
    private JSONObject getMovieByYear(String year, String page) throws IOException, InterruptedException, JSONException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + API_KEY + "&s=one&type=movie&y=" + year + page))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject jobj = new JSONObject(response.body());

        return  jobj;
    }

    //Method fetches movie by id from api and returns Movie object
    private Movie getMovieById(String Id) throws IOException, InterruptedException, JSONException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .uri(URI.create(API_URL + API_KEY + "&i=" + Id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject jobj = new JSONObject(response.body());
        String title = jobj.getString("Title");
        String year = jobj.getString("Year");
        double rank;
        if (jobj.getString("imdbRating").equals("N/A")){
            rank = 0;
        }else{
             rank = jobj.getDouble("imdbRating");
        }
        String genre = jobj.getString("Genre");
        String plot = jobj.getString("Plot");
        String actors = jobj.getString("Actors");

        Movie movie = new Movie(title,
                year,
                rank,
                genre,
                plot,
                actors
                );

        return movie;
    }

}
