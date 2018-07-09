package balychev.oleh.blch.movieapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import balychev.oleh.blch.movieapp.model.Genre;


public class GenresResponse {
    @SerializedName("genres")
    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
