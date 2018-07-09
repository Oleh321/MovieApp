package balychev.oleh.blch.movieapp.api;


import balychev.oleh.blch.movieapp.model.response.CrewResponse;
import balychev.oleh.blch.movieapp.model.response.GenresResponse;
import balychev.oleh.blch.movieapp.model.response.MovieCreditsResponse;
import balychev.oleh.blch.movieapp.model.response.MoviesResponse;
import balychev.oleh.blch.movieapp.model.response.PersonsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular?language=ru")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("genre/movie/list?language=ru")
    Call<GenresResponse> getGenresList(@Query("api_key") String apiKey);



    @GET("movie/{id}/credits?language=ru")
    Call<CrewResponse> getCrew(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("search/movie?language=ru")
    Call<MoviesResponse> getMoviesByTitle(@Query("api_key") String apiKey, @Query("query") String query, @Query("page") int page);

    @GET("search/person?language=ru")
    Call<PersonsResponse> getPeopleByName(@Query("api_key") String apiKey, @Query("query") String query, @Query("page") int page);

    @GET("person/{id}/movie_credits?language=ru")
    Call<MovieCreditsResponse> getMovieCreditsByPerson(@Path("id") int id, @Query("api_key") String apiKey);

}
