package com.udacity.stage2.popularmoviess2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.Results;
import com.udacity.stage2.popularmoviess2.fragment.MovieActivityFragment;
import com.udacity.stage2.popularmoviess2.fragment.MovieDetailActivityFragment;

import butterknife.Bind;

public class MovieActivity extends AppCompatActivity implements MovieActivityFragment.Callback1 {
    public static boolean mTwoPane;
    public static final String DETAIL_TAG="DFT";
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor  editor=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        sharedPreferences=getPreferences(Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(findViewById(R.id.fragment_movie_detail) != null)
        {
            mTwoPane=true;
            if(savedInstanceState==null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movie_detail, new MovieDetailActivityFragment())
                        .commit();
            }
        }
        else
        {
            mTwoPane=false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.fav_url)))
        {
            menu.findItem(R.id.favorite).setChecked(true);
        }
        else if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.rating_url)))
        {
            menu.findItem(R.id.sort_rating).setChecked(true);
        }
        else
        {
            menu.findItem(R.id.sort_popularity).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sort_popularity:
                editor.putString(getString(R.string.sort_by_key_param),getString(R.string.popularity_url)).commit();
                if(!item.isChecked()) {
                    item.setChecked(true);

                }
                return true;
            case R.id.sort_rating:
                editor.putString(getString(R.string.sort_by_key_param),getString(R.string.rating_url)).commit();
                if(!item.isChecked()) {
                    item.setChecked(true);

                }
                return true;
            case R.id.favorite:
                editor.putString(getString(R.string.sort_by_key_param),getString(R.string.fav_url)).commit();
                if(!item.isChecked()) {
                    item.setChecked(true);

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void callMovieDetail(Results results) {
        if(mTwoPane)
        {
            Fragment movieFragment= MovieDetailActivityFragment.newInstance(results,this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_movie_detail,movieFragment,MovieActivity.DETAIL_TAG).commit();
        }
        else
        {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("Result", results);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mTwoPane)
        {
            MovieDetailActivityFragment movieDetailActivityFragment = (MovieDetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAIL_TAG);
            if (movieDetailActivityFragment != null && movieDetailActivityFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(movieDetailActivityFragment).commit();
            }
        }
    }
}
