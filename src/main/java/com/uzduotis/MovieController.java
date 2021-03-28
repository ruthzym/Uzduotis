package com.uzduotis;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping(value="/getmovies", method = RequestMethod.GET)
    public List<Movie> getMovies() throws IOException, InterruptedException, JSONException {

        var movies = movieService.allMovies("2019", "2008", "2005");
        return movies;

    }
}
