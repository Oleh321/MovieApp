package balychev.oleh.blch.movieapp.fragment;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import balychev.oleh.blch.movieapp.data.MoviesLab;
import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.adapter.MoviesAdapter;
import balychev.oleh.blch.movieapp.api.Client;
import balychev.oleh.blch.movieapp.api.Service;
import balychev.oleh.blch.movieapp.model.response.GenresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AbstractMovieListFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected TextView mAmountText;

    protected MoviesLab mMoviesLab;
    protected ProgressDialog mDialog;
    protected Service mApiService;

    protected int mAmount;
    protected boolean needToReload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviesLab = MoviesLab.get();
        mApiService = Client.getClient().create(Service.class);
        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("Загрузка...");
    }

    protected void loadGenres(){
        try {
            Call<GenresResponse> call = mApiService.getGenresList(getString(R.string.API_KEY));
            call.enqueue(new Callback<GenresResponse>() {
                @Override
                public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                    mMoviesLab.setGenres(response.body().getGenres());
                    loadMovies();
                }
                @Override
                public void onFailure(Call<GenresResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void configureRecyclerViewLayoutManager() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    protected abstract void loadMovies();

    protected void loadGUI(){
        MoviesAdapter adapter = new MoviesAdapter(getContext());
        //adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        mDialog.dismiss();
    };



}
