package balychev.oleh.blch.movieapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.MovieCast;
import balychev.oleh.blch.movieapp.model.MovieCrew;

public class MovieCreditsResponse {
    @SerializedName("cast")
    private ArrayList<MovieCast> cast;
    @SerializedName("crew")
    private ArrayList<MovieCrew> crew;

    public ArrayList<MovieCast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<MovieCast> cast) {
        this.cast = cast;
    }

    public ArrayList<MovieCrew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<MovieCrew> crew) {
        this.crew = crew;
    }
}
