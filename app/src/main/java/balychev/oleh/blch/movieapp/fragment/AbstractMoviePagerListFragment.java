package balychev.oleh.blch.movieapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.model.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AbstractMoviePagerListFragment extends AbstractMovieListFragment {

    protected ImageButton mLeftButton;
    protected ImageButton mRightButton;
    protected TextView mCurrentPageText;
    protected ScrollView mScrollView;

    protected int mCurrentPage;

    protected Call<MoviesResponse> mCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mCurrentPage = savedInstanceState.getInt("current_page");
            mAmount = savedInstanceState.getInt("pages_amount");
            needToReload = false;

        } else  {
            needToReload = true;
            mCurrentPage = 1;
            mMoviesLab.setMovies(new ArrayList<>());

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_page", mCurrentPage);
        outState.putInt("pages_amount", mAmount);
    }

    protected View initView(LayoutInflater inflater, ViewGroup container, int layout){
        View view = inflater.inflate(layout, container, false);
        mLeftButton = view.findViewById(R.id.layout_pager_list_left_btn);
        mRightButton = view.findViewById(R.id.layout_pager_list_right_btn);
        mAmountText = view.findViewById(R.id.layout_pager_list_pages_amount);
        mCurrentPageText = view.findViewById(R.id.layout_pager_list_current_page);
        mRecyclerView = view.findViewById(R.id.layout_pager_list_recycler_view);

        configureRecyclerViewLayoutManager();

        mLeftButton.setOnClickListener(v->{
            if(mCurrentPage==1)
                return;
            mCurrentPage--;
            loadMovies();
        });
        mRightButton.setOnClickListener(v->{
            if(mCurrentPage==mAmount)
                return;
            mCurrentPage++;
            loadMovies();
        });
        return view;
    }


    @Override
    protected void loadMovies() {
        try {
            initCall();
            mCall.enqueue(new Callback<MoviesResponse>() {

                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    mMoviesLab.setMovies(response.body().getResults());
                    mAmount = response.body().getTotalPages();
                  //  adapter.loadData();
                    loadGUI();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract void initCall();

    @Override
    protected void loadGUI() {
        super.loadGUI();
        mScrollView.scrollTo(0,0);
        if(mAmount == 1){
            mCurrentPageText.setVisibility(View.GONE);
            mAmountText.setText(getString(R.string.movie_amount) + " " + mMoviesLab.getMovies().size());
            mLeftButton.setVisibility(View.GONE);
            mRightButton.setVisibility(View.GONE);
            return;
        }
        if (mCurrentPage==1){
            mLeftButton.setVisibility(View.INVISIBLE);
        }else {
            mLeftButton.setVisibility(View.VISIBLE);
        }
        if (mCurrentPage == mAmount){
            mRightButton.setVisibility(View.INVISIBLE);
        } else {
            mRightButton.setVisibility(View.VISIBLE);
        }
        mCurrentPageText.setVisibility(View.VISIBLE);
        mCurrentPageText.setText(String.valueOf(mCurrentPage));
        mAmountText.setVisibility(View.VISIBLE);
        mAmountText.setText(getString(R.string.pages_amount) + " " + mAmount);
        mCurrentPageText.setVisibility(View.VISIBLE);
    }




}
