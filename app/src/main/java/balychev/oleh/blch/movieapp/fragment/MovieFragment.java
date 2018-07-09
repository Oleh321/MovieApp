package balychev.oleh.blch.movieapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import balychev.oleh.blch.movieapp.activity.MoviePagerActivity;
import balychev.oleh.blch.movieapp.data.MovieCreditsLab;
import balychev.oleh.blch.movieapp.data.MoviesLab;
import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.api.Client;
import balychev.oleh.blch.movieapp.api.Service;
import balychev.oleh.blch.movieapp.database.MovieDbHelper;
import balychev.oleh.blch.movieapp.model.Crew;
import balychev.oleh.blch.movieapp.model.Movie;
import balychev.oleh.blch.movieapp.model.response.CrewResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieFragment extends Fragment {

    private Movie mMovie;

    private ImageView mPosterImage;
    private TextView mReleaseYearText;
    private TextView mDirectorText;
    private TextView mRatingText;
    private TextView mSummaryText;

    private MovieDbHelper mDbHelper;

    private MenuItem mSaveMenuItem;
    private int position;
    public static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            position = getArguments().getInt(ARGUMENT_PAGE_NUMBER, -1);
         //   mMovie = ((MoviePagerActivity) getActivity()).getMovie(position);
        } else {
            position = savedInstanceState.getInt("pos", -1);
          //  mMovie = new Movie();
        }

       // mMovie = ((MoviePagerActivity) getActivity()).getMovie(position);
       // mMovie = MoviesLab.get().getMovies().get(position);
        mDbHelper = new MovieDbHelper(getContext());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovie = ((MoviePagerActivity) getActivity()).getMovie(position);
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        mPosterImage = view.findViewById(R.id.fg_movie_image_poster);
        mReleaseYearText = view.findViewById(R.id.fg_movie_text_release_year);
        mDirectorText = view.findViewById(R.id.fg_movie_text_director);
        mRatingText = view.findViewById(R.id.fg_movie_text_rating);
        mSummaryText = view.findViewById(R.id.fg_movie_text_summary);

        loadCrewByConcreteMovieJSON(mMovie.getId());

        if (mMovie.getPosterPath()!=null) {
            Picasso.get().load(getString(R.string.IMAGE_PATH) + mMovie.getPosterPath()).networkPolicy(NetworkPolicy.OFFLINE).into(mPosterImage);
        } else {
            Picasso.get().load(R.drawable.film_reel).into(mPosterImage);
        }

        if (mMovie.getReleaseDate()!=null && mMovie.getReleaseDate().toCharArray().length > 4)
            mReleaseYearText.setText(mMovie.getReleaseDate().substring(0, 4));

        mRatingText.setText(String.valueOf(mMovie.getVoteAverage()));
        if("0.0".equals(mRatingText.getText().toString())){
            mRatingText.setVisibility(View.GONE);
        }
        mSummaryText.setText(mMovie.getOverview());

        return view;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mSaveMenuItem = menu.add("save");
        mSaveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (null==mDbHelper.getMovieById(mMovie.getId())){
            mSaveMenuItem.setIcon(R.drawable.baseline_star_border_white_36dp);
        } else {
            mSaveMenuItem.setIcon(R.drawable.baseline_star_white_36dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(mSaveMenuItem)){
            if (null==mDbHelper.getMovieById(mMovie.getId())){
                mDbHelper.addMovie(mMovie);
                mSaveMenuItem.setIcon(R.drawable.baseline_star_white_36dp);
            } else {
                mDbHelper.deleteMovie(mMovie.getId());
                mSaveMenuItem.setIcon(R.drawable.baseline_star_border_white_36dp);
            }
        }
        return true;
    }

    public static Fragment newInstance(int position) {
        MovieFragment fragment = new MovieFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARGUMENT_PAGE_NUMBER, position);
        fragment.setArguments(arg);
        return fragment;
    }

    private boolean loadCrewByConcreteMovieJSON(int id){
        try {
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<CrewResponse> call = apiService.getCrew(id, getString(R.string.API_KEY));
            call.enqueue(new Callback<CrewResponse>() {

                @Override
                public void onResponse(Call<CrewResponse> call, Response<CrewResponse> response) {
                    List<Crew> crews = response.body().getCrew();
                    for(Crew crew : crews){
                        if (crew.getJob().equals("Director")) {
                            mDirectorText.setText(crew.getName());
                            mMovie.setDirector(crew.getName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CrewResponse> call, Throwable t) {

                }
            });
            return true;

        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
