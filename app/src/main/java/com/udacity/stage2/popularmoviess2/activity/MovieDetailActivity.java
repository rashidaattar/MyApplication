package com.udacity.stage2.popularmoviess2.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.Results;
import com.udacity.stage2.popularmoviess2.fragment.MovieDetailActivityFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra("Result"))
        {
            Results results=getIntent().getParcelableExtra("Result");
            Fragment movieFragment= MovieDetailActivityFragment.newInstance(results,this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_movie_detail,movieFragment).commit();
        }
    }


}
