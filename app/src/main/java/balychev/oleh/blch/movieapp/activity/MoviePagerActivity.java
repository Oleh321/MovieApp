package balychev.oleh.blch.movieapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.data.MovieCreditsLab;
import balychev.oleh.blch.movieapp.data.MoviesLab;
import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.fragment.MovieFragment;
import balychev.oleh.blch.movieapp.model.Movie;

public class MoviePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;


    public static final String MOVIE_POSITION = "movie_position";
    public static final String MOVIE_LAB = "movie_lab";

    public static final String MOVIE = "movie";
    public static final String MOVIE_CAST = "movie_cast";
    public static final String MOVIE_CREW = "movie_crew";

    private String mMovieClass;
    int mCurrentMoviePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_movie);

        if(savedInstanceState!=null) {
            mMovieClass = savedInstanceState.getString(MOVIE_LAB);
            mCurrentMoviePosition = savedInstanceState.getInt(MOVIE_POSITION);
        } else{
            mMovieClass = getIntent().getExtras().getString(MOVIE_LAB);
            mCurrentMoviePosition = getIntent().getExtras().getInt(MOVIE_POSITION, -1);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mViewPager = findViewById(R.id.activity_movie_viewpager);

        mPagerAdapter = new MovieFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentMoviePosition);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                getSupportActionBar().setTitle(getMovie(mViewPager.getCurrentItem()).getTitle());
            }
        });

        getSupportActionBar().setTitle(getMovie(mViewPager.getCurrentItem()).getTitle());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOVIE_LAB, mMovieClass);
        outState.putInt(MOVIE_POSITION, mViewPager.getCurrentItem());
    }

    public Movie getMovie(int position){
        if(mMovieClass.equals(MOVIE)){
            return MoviesLab.get().getMovies().get(position);
        }else if(mMovieClass.equals(MOVIE_CAST)){
            return MovieCreditsLab.get().getMovieCasts().get(position).convertToMovie();
        }else  if(mMovieClass.equals(MOVIE_CREW)){
            return MovieCreditsLab.get().getMovieCrews().get(position).convertToMovie();
        }
        return null;
    }


    public int getMovieArraySize(){
        if(mMovieClass.equals(MOVIE)){
            return MoviesLab.get().getMovies().size();
        }else if(mMovieClass.equals(MOVIE_CAST)){
            return MovieCreditsLab.get().getMovieCasts().size();
        }else  if(mMovieClass.equals(MOVIE_CREW)){
            return MovieCreditsLab.get().getMovieCrews().size();
        }
        return 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private class MovieFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MovieFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MovieFragment.newInstance(position);
        }

        @Override
        public int getCount() {
           return getMovieArraySize();
     //       return MoviesLab.get().getMovies().size();
        }
    }
}
