package balychev.oleh.blch.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.data.MoviesLab;
import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.activity.MoviePagerActivity;
import balychev.oleh.blch.movieapp.model.Genre;
import balychev.oleh.blch.movieapp.model.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context mContext;
    private MoviesLab mMoviesLab;
    private ArrayList<Movie> mMovies;
    private ArrayList<Genre> mGenres;

    public MoviesAdapter(Context context) {
        mContext = context;
        mMoviesLab = MoviesLab.get();
        mMovies = mMoviesLab.getMovies();
        mGenres = mMoviesLab.getGenres();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    private void checkView(TextView view){
        if ("".equals(view.getText())) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mTitle.setText(movie.getTitle());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movie.getGenreIds().size(); i++) {
            sb.append(mGenres.get(mGenres.indexOf(new Genre(movie.getGenreIds().get(i)))).getName());
            if (i + 1 < movie.getGenreIds().size()) {
                sb.append(", ");
            }
        }

        holder.mRating.setText(String.valueOf(movie.getVoteAverage()));
        if ("0.0".equals( holder.mRating.getText())){
            holder.mRating.setVisibility(View.GONE);
        } else {
            holder.mRating.setVisibility(View.VISIBLE);
        }

        holder.mCategory.setText(sb.toString());


        if (movie.getReleaseDate()!=null && movie.getReleaseDate().toCharArray().length > 4)
            holder.mReleaseYear.setText(movie.getReleaseDate().substring(0, 4));


        if (movie.getPosterPath()!=null) {
            Picasso.get().load(mContext.getString(R.string.IMAGE_PATH) + movie.getPosterPath()).into(holder.mPosterImage);
        } else {
            Picasso.get().load(R.drawable.film_reel).into(holder.mPosterImage);
        }

        checkView(holder.mCategory);
        checkView(holder.mTitle);
        checkView(holder.mReleaseYear);

        View.OnClickListener listener = v->{
            Intent intent = new Intent(mContext, MoviePagerActivity.class);
            intent.putExtra(MoviePagerActivity.MOVIE_POSITION, position);
            intent.putExtra(MoviePagerActivity.MOVIE_LAB, MoviePagerActivity.MOVIE);
            mContext.startActivity(intent);
        };
        holder.mPosterImage.setOnClickListener(listener);
        holder.mView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return  MoviesLab.get().getMovies().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPosterImage;
        private TextView mCategory;
        private TextView mTitle;
        private TextView mRating;
        private TextView mReleaseYear;

        private View mView;

        public MovieViewHolder(View view) {
            super(view);
            mView = view;
            mPosterImage = view.findViewById(R.id.single_movie_layout_image_poster);
            mCategory = view.findViewById(R.id.single_movie_layout_category);
            mTitle = view.findViewById(R.id.single_movie_layout_title);
            mRating = view.findViewById(R.id.single_movie_layout_rating);
            mReleaseYear = view.findViewById(R.id.single_movie_layout_release_year);
        }


    }
}
