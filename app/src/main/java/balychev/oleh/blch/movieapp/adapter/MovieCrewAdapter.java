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

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.activity.MoviePagerActivity;
import balychev.oleh.blch.movieapp.data.MovieCreditsLab;
import balychev.oleh.blch.movieapp.model.Genre;
import balychev.oleh.blch.movieapp.model.MovieCast;
import balychev.oleh.blch.movieapp.model.MovieCrew;

public class MovieCrewAdapter extends RecyclerView.Adapter<MovieCrewAdapter.MovieCrewViewHolder>{

    private Context mContext;
    private MovieCreditsLab mMovieCreditsLab;
    private ArrayList<MovieCrew> mMovieCrew;
    private ArrayList<Genre> mGenres;

    public MovieCrewAdapter(Context context) {
        mContext = context;
        mMovieCreditsLab = MovieCreditsLab.get();
    }

    @NonNull
    @Override
    public MovieCrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_person_in_movie_layout, parent, false);
        return new MovieCrewViewHolder(view);
    }

    private void checkView(TextView view){
        if ("".equals(view.getText())) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCrewViewHolder holder, int position) {
        MovieCrew movieCrew = mMovieCrew.get(position);
        holder.mTitle.setText(movieCrew.getTitle());
        holder.mJob.setText(movieCrew.getJob());

        holder.mRating.setText(String.valueOf(movieCrew.getVoteAverage()));
        if ("0.0".equals( holder.mRating.getText())){
            holder.mRating.setVisibility(View.GONE);
        } else {
            holder.mRating.setVisibility(View.VISIBLE);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movieCrew.getGenreIds().size(); i++) {
            sb.append(mGenres.get(mGenres.indexOf(new Genre(movieCrew.getGenreIds().get(i)))).getName());
            if (i + 1 < movieCrew.getGenreIds().size()) {
                sb.append(", ");
            }
        }
        holder.mCategory.setText(sb.toString());

        if (movieCrew.getReleaseDate()!=null && movieCrew.getReleaseDate().toCharArray().length > 4)
            holder.mReleaseYear.setText(movieCrew.getReleaseDate().substring(0, 4));


        if (movieCrew.getPosterPath()!=null) {
            Picasso.get().load(mContext.getString(R.string.IMAGE_PATH) + movieCrew.getPosterPath()).into(holder.mPosterImage);
        } else {
            Picasso.get().load(R.drawable.film_reel).into(holder.mPosterImage);
        }

        View.OnClickListener listener = view->{
            Intent intent = new Intent(mContext, MoviePagerActivity.class);
            intent.putExtra(MoviePagerActivity.MOVIE_POSITION, position);
            intent.putExtra(MoviePagerActivity.MOVIE_LAB, MoviePagerActivity.MOVIE_CREW);
            mContext.startActivity(intent);
        };

        checkView(holder.mJob);
        checkView(holder.mCategory);
        checkView(holder.mTitle);
        checkView(holder.mReleaseYear);

        holder.mPosterImage.setOnClickListener(listener);
        holder.mView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mMovieCrew.size();
    }

    public void loadData() {
        mMovieCrew = mMovieCreditsLab.getMovieCrews();
        mGenres = mMovieCreditsLab.getGenres();
    }

    public class MovieCrewViewHolder extends RecyclerView.ViewHolder {
        private TextView mJob;
        private ImageView mPosterImage;
        private TextView mCategory;
        private TextView mTitle;
        private TextView mRating;
        private TextView mReleaseYear;

        private View mView;

        public MovieCrewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mPosterImage = itemView.findViewById(R.id.single_person_in_movie_layout_image_poster);
            mCategory = itemView.findViewById(R.id.single_person_in_movie_layout_category);
            mTitle = itemView.findViewById(R.id.single_person_in_movie_layout_title);
            mRating = itemView.findViewById(R.id.single_person_in_movie_layout_rating);
            mReleaseYear = itemView.findViewById(R.id.single_person_in_movie_layout_release_year);
            mJob = itemView.findViewById(R.id.single_person_in_movie_layout_person);
        }
    }
}
