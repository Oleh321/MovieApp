package balychev.oleh.blch.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<>();

    private String director;

    public Movie() {
    }

    public Movie(Integer id, String title, String posterPath, String releaseDate, Double voteAverage, String overview, List<Integer> genreIds, String director) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.genreIds = genreIds;
        this.director = director;
    }

    public Movie(Integer id, String title, String posterPath, String releaseDate, Double voteAverage, String overview, List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.voteAverage = voteAverage;

        this.overview = overview;
        director = "";
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return  id + "\n" +
                title + '\n' +
                "https://image.tmdb.org/t/p/w500/" + posterPath +
                "\n" + releaseDate +
                "\n" + genreIds +
                "\n" + voteAverage + "\n"
                + overview;
    }
}
