package balychev.oleh.blch.movieapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.model.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMoviesByTitleFragment extends AbstractMoviePagerListFragment {

    private EditText mTitleEditText;
    private ImageButton mSearchButton;

    private TextView mNotFoundText;

    private String mCurrentQuery;
    private boolean isRequested;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentQuery = savedInstanceState.getString("edit_text");
            isRequested = savedInstanceState.getBoolean("requested");

        } else {
            mCurrentQuery = "";
            isRequested = false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edit_text", mTitleEditText.getText().toString());
        outState.putBoolean("requested", isRequested);

    }

    @Override
    protected void initCall() {
        mCall = mApiService.getMoviesByTitle(getString(R.string.API_KEY),
                mTitleEditText.getText().toString(),  mCurrentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, R.layout.fragment_search_movies_by_title);
        mScrollView = view.findViewById(R.id.fg_searchmovies_scroll_view);
        mTitleEditText = view.findViewById(R.id.fg_searchmovies_edit_text_movie);
        mSearchButton = view.findViewById(R.id.fg_searchmovies_search_btn);
        mNotFoundText = view.findViewById(R.id.fg_searchmovies_not_found_text);

        mCurrentPageText.setVisibility(View.GONE);
        mLeftButton.setVisibility(View.GONE);
        mRightButton.setVisibility(View.GONE);
        mTitleEditText.setText(mCurrentQuery);

        if (isRequested) {
            mDialog.show();  
            loadGUI();
        }

        mSearchButton.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if("".equals(mTitleEditText.getText().toString()))
                return;
            isRequested = true;
            mCurrentPage=1;
            mDialog.show();
            loadGenres();
        });
        return view;
    }

    @Override
    protected void loadGUI() {
        super.loadGUI();
        if (mMoviesLab.getMovies().size() == 0) {
            mAmountText.setVisibility(View.GONE);
            mNotFoundText.setVisibility(View.VISIBLE);
        }else {
            mNotFoundText.setVisibility(View.GONE);
        }
    }
}
