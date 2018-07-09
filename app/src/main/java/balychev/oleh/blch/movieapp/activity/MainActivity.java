package balychev.oleh.blch.movieapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import balychev.oleh.blch.movieapp.R;
import balychev.oleh.blch.movieapp.fragment.AllMoviesFragment;
import balychev.oleh.blch.movieapp.fragment.SavedMoviesFragment;
import balychev.oleh.blch.movieapp.fragment.SearchMoviesByTitleFragment;
import balychev.oleh.blch.movieapp.fragment.SearchPeopleByNameFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    public static final String FRAGMENT_TAG = "fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (mCurrentFragment == null){
            mCurrentFragment = new AllMoviesFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, mCurrentFragment, FRAGMENT_TAG);
        ft.commit();


       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_all_movies);
        setActionBarText();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new Fragment();

        if (id == R.id.nav_all_movies) {
            fragment = new AllMoviesFragment();
        } else if (id == R.id.nav_saved_movies) {
            fragment = new SavedMoviesFragment();
        } else if (id == R.id.nav_search_by_title) {
            fragment = new SearchMoviesByTitleFragment();
        } else if (id == R.id.nav_search_by_director) {
            fragment = new SearchPeopleByNameFragment();
        }

        if (!fragment.getClass().getName().equals(mCurrentFragment.getClass().getName())) {
            mCurrentFragment = fragment;
            setActionBarText();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, mCurrentFragment, FRAGMENT_TAG);
            ft.commit();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionBarText(){
        String label = getString(R.string.app_name);
        if(mCurrentFragment instanceof AllMoviesFragment){
            label = getString(R.string.all_movies);
        } else if (mCurrentFragment instanceof SavedMoviesFragment){
            label = getString(R.string.saved_movies);
        } else if (mCurrentFragment instanceof SearchPeopleByNameFragment){
            label = getString(R.string.person_search);
        } else if (mCurrentFragment instanceof SearchMoviesByTitleFragment){
            label = getString(R.string.movie_search);
        }
        getSupportActionBar().setTitle(label);
    }
}
