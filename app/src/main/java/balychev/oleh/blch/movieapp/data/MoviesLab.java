package balychev.oleh.blch.movieapp.data;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.Genre;
import balychev.oleh.blch.movieapp.model.Movie;

public class MoviesLab {
    private static MoviesLab sMoviesLab = new MoviesLab();

    private ArrayList<Movie> mMovies;
    private ArrayList<Genre> mGenres;
    //private Context mContext;

    public static MoviesLab get(){
        return sMoviesLab;
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    public ArrayList<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        mGenres = genres;
    }
}
