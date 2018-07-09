package balychev.oleh.blch.movieapp.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.adapter.MovieCastAdapter;
import balychev.oleh.blch.movieapp.adapter.MovieCrewAdapter;
import balychev.oleh.blch.movieapp.adapter.MoviesAdapter;
import balychev.oleh.blch.movieapp.api.Client;
import balychev.oleh.blch.movieapp.api.Service;
import balychev.oleh.blch.movieapp.data.MovieCreditsLab;
import balychev.oleh.blch.movieapp.data.PeopleLab;
import balychev.oleh.blch.movieapp.model.MovieCrew;
import balychev.oleh.blch.movieapp.model.Person;
import balychev.oleh.blch.movieapp.model.response.GenresResponse;
import balychev.oleh.blch.movieapp.model.response.MovieCreditsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mCastTextView;
    private TextView mCrewTextView;
    private TextView mNotFoundTextView;
    private RecyclerView mRecyclerCastView;
    private RecyclerView mRecyclerCrewView;

    private Service mApiService;
    private ProgressDialog mDialog;
    public static final String PERSON_POSITION = "person_position";

    private MovieCrewAdapter movieCrewAdapter;
    private MovieCastAdapter movieCastAdapter;

    private Person mPerson;
    private MovieCreditsLab mMovieCreditsLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mImageView = findViewById(R.id.act_person_photo_image);
        mCastTextView = findViewById(R.id.act_person_cast_text);
        mCrewTextView = findViewById(R.id.act_person_crew_text);
        mNotFoundTextView = findViewById(R.id.act_person_not_found_text);

        mRecyclerCastView = findViewById(R.id.act_person_list_recycler_view_cast);
        mRecyclerCrewView = findViewById(R.id.act_person_list_recycler_view_crew);

        movieCrewAdapter = new MovieCrewAdapter(this);
        movieCastAdapter = new MovieCastAdapter(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieCreditsLab = MovieCreditsLab.get();
        mPerson = PeopleLab.get().getPeople()
                .get(getIntent().getIntExtra(PERSON_POSITION, 0));

        if (mPerson.getProfilePath()!=null){
            mImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(getString(R.string.IMAGE_PATH) + mPerson.getProfilePath())
                    .networkPolicy(NetworkPolicy.OFFLINE).into(mImageView);
        } else {
            mImageView.setVisibility(View.GONE);
        }
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Загрузка...");
        mDialog.show();
        mApiService = Client.getClient().create(Service.class);
        getSupportActionBar().setTitle(mPerson.getName());
        configureRecyclerViewLayoutManager();
        loadGenres();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureRecyclerViewLayoutManager() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerCrewView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerCastView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerCrewView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerCastView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void loadGenres(){
        try {
            Call<GenresResponse> call = mApiService.getGenresList(getString(R.string.API_KEY));
            call.enqueue(new Callback<GenresResponse>() {
                @Override
                public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                    mMovieCreditsLab.setGenres(response.body().getGenres());
                    loadMovieCredits();
                }
                @Override
                public void onFailure(Call<GenresResponse> call, Throwable t) {
                    Toast.makeText(PersonActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("myLogs", t.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(PersonActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("myLogs", e.getMessage());
        }
    }

    private void loadMovieCredits() {
        try {
            Call<MovieCreditsResponse> call = mApiService.getMovieCreditsByPerson(mPerson.getId(), getString(R.string.API_KEY));
            call.enqueue(new Callback<MovieCreditsResponse>() {
                @Override
                public void onResponse(Call<MovieCreditsResponse> call, Response<MovieCreditsResponse> response) {
                    mMovieCreditsLab.setMovieCrews(response.body().getCrew());
                    deleteDuplicateData();
                    mMovieCreditsLab.setMovieCasts(response.body().getCast());
                    loadGUI();
                }
                @Override
                public void onFailure(Call<MovieCreditsResponse> call, Throwable t) {
                    Toast.makeText(PersonActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("myLogs", t.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(PersonActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("myLogs", e.getMessage());
        }
    }

    private void deleteDuplicateData() {
        StringBuilder job;
        ArrayList<MovieCrew> list = new ArrayList();
        for(int i = 0; i < mMovieCreditsLab.getMovieCrews().size(); i++){
            job = new StringBuilder(mMovieCreditsLab.getMovieCrews().get(i).getJob());
            for(int j = i+1; j <  mMovieCreditsLab.getMovieCrews().size()-1; j++){
                if ( mMovieCreditsLab.getMovieCrews().get(i).equals(mMovieCreditsLab.getMovieCrews().get(j))){
                    job.append("\n" + mMovieCreditsLab.getMovieCrews().get(j).getJob());
                    mMovieCreditsLab.getMovieCrews().get(i).setJob(job.toString());
                    if (!list.contains(mMovieCreditsLab.getMovieCrews().get(i)))
                        list.add(mMovieCreditsLab.getMovieCrews().get(i));
                }
            }
        }
        mMovieCreditsLab.setMovieCrews(list);
    }

    protected void loadGUI(){

        movieCastAdapter.loadData();
        movieCrewAdapter.loadData();

        mRecyclerCastView.setAdapter(movieCastAdapter);
        mRecyclerCrewView.setAdapter(movieCrewAdapter);

        int sizeCast = mMovieCreditsLab.getMovieCasts().size();
        int sizeCrew = mMovieCreditsLab.getMovieCrews().size();


        if (sizeCast == 0){
            mRecyclerCastView.setVisibility(View.GONE);
            mCastTextView.setVisibility(View.GONE);
        } else {
            mRecyclerCastView.setVisibility(View.VISIBLE);
            mCastTextView.setVisibility(View.VISIBLE);
        }

        if (sizeCrew == 0){
            mRecyclerCrewView.setVisibility(View.GONE);
            mCrewTextView.setVisibility(View.GONE);
        } else {
            mRecyclerCrewView.setVisibility(View.VISIBLE);
            mCrewTextView.setVisibility(View.VISIBLE);
        }

        if(sizeCast==0 && sizeCrew==0){
            mNotFoundTextView.setVisibility(View.VISIBLE);
        }else {
            mNotFoundTextView.setVisibility(View.GONE);
        }

        mDialog.dismiss();
    }



}
