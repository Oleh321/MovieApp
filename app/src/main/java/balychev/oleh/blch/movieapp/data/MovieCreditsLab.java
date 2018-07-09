package balychev.oleh.blch.movieapp.data;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.Genre;
import balychev.oleh.blch.movieapp.model.Movie;
import balychev.oleh.blch.movieapp.model.MovieCast;
import balychev.oleh.blch.movieapp.model.MovieCrew;
import balychev.oleh.blch.movieapp.model.response.MovieCreditsResponse;

public class MovieCreditsLab  {
    private static MovieCreditsLab sMovieCreditsLab;

    private ArrayList<MovieCrew> mMovieCrews;
    private ArrayList<MovieCast> mMovieCasts;
    private ArrayList<Genre> mGenres;
    //private Context mContext;

    public static MovieCreditsLab get(){
        if (sMovieCreditsLab == null)
            sMovieCreditsLab = new MovieCreditsLab();
        return sMovieCreditsLab;
    }

    public ArrayList<MovieCrew> getMovieCrews() {
        return mMovieCrews;
    }

    public void setMovieCrews(ArrayList<MovieCrew> movieCrews) {
        mMovieCrews = movieCrews;
    }

    public ArrayList<MovieCast> getMovieCasts() {
        return mMovieCasts;
    }

    public void setMovieCasts(ArrayList<MovieCast> movieCasts) {
        mMovieCasts = movieCasts;
    }

    public ArrayList<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        mGenres = genres;
    }
}
