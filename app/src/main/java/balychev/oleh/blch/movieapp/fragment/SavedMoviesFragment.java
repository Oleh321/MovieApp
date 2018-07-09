package balychev.oleh.blch.movieapp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.database.MovieDbHelper;

public class SavedMoviesFragment extends AbstractMovieListFragment {

    private MovieDbHelper mDbHelper;

    private ScrollView mScrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new MovieDbHelper(getContext());
        if (savedInstanceState != null){
            needToReload = false;
        } else  {
            needToReload = true;
        }

    }

    @Override
    protected void loadMovies() {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                mMoviesLab.setMovies(mDbHelper.getAllMovies());
                return null;
            }

            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                loadGUI();
            }
        }.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDialog.show();
        if (needToReload){
            loadGenres();
        } else {
            loadMovies();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_movies, container, false);

        mAmountText = view.findViewById(R.id.fg_savedmovies_movies_amount);
        mRecyclerView = view.findViewById(R.id.fg_savedmovies_recycler_view);
        mScrollView = view.findViewById(R.id.fg_savedmovies_scroll_view);
        configureRecyclerViewLayoutManager();


        return view;
    }

    protected void loadGUI() {
        mScrollView.scrollTo(0,0);
        mAmount = mMoviesLab.getMovies().size();
        mAmountText.setText(getString(R.string.movie_amount) + " " + mAmount);
        super.loadGUI();
    }

}
