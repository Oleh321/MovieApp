package balychev.oleh.blch.movieapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import balychev.oleh.blch.movieapp.R;

import balychev.oleh.blch.movieapp.model.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMoviesFragment extends AbstractMoviePagerListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initCall() {
        mCall = mApiService.getPopularMovies(getString(R.string.API_KEY), mCurrentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, R.layout.fragment_all_movies);
        mScrollView = view.findViewById(R.id.fg_allmovies_scroll_view);
        mDialog.show();
        configureRecyclerViewLayoutManager();
        if (needToReload){
            loadGenres();
        } else {
            loadGUI();
        }
        return view;
    }
}
