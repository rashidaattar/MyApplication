package com.udacity.stage2.popularmoviess2.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.adapter.MovieListAdapter;
import com.udacity.stage2.popularmoviess2.data.MovieColumns;
import com.udacity.stage2.popularmoviess2.data.MovieProvider;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.MoviesDbDto;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.Results;
import com.udacity.stage2.popularmoviess2.retrofitInterface.MovieDbService;
import com.udacity.stage2.popularmoviess2.utility.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment {

    private static MoviesDbDto moviesDbDto = new MoviesDbDto();
    private static MovieListAdapter movieListAdapter;
    ArrayList<Results> resultsList = new ArrayList<>();
    public static boolean isFav=false;
    @Bind(R.id.my_recycler_view) RecyclerView recyclerView;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor  editor=null;
    SharedPreferences.OnSharedPreferenceChangeListener myPrefListner;

    public interface Callback1 {

         void callMovieDetail(Results results);
    }

    public MovieActivityFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
         myPrefListner = new SharedPreferences.OnSharedPreferenceChangeListener(){
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                if(key.equals(getString(R.string.sort_by_key_param)))
                {
                    //Toast.makeText(getActivity(),"preference changed",Toast.LENGTH_SHORT).show();
                    if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.fav_url)))
                    {
                        callTMDB(getString(R.string.fav_url));
                    }
                    else if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.rating_url)))
                    {
                        callTMDB(getString(R.string.rating_url));
                    }
                    else
                    {
                        callTMDB(getString(R.string.popularity_url));
                    }
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_movie, null, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if(savedInstanceState==null)
        {
            if(sharedPreferences.getString(getString(R.string.sort_by_key_param),null)!=null)
            {
                if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.fav_url)))
                {
                    callTMDB(getString(R.string.fav_url));
                }
                else if(sharedPreferences.getString(getString(R.string.sort_by_key_param),"").equalsIgnoreCase(getString(R.string.rating_url)))
                {
                    callTMDB(getString(R.string.rating_url));
                }
            }
            else
            {
                callTMDB(getString(R.string.popularity_url));
            }
        }
        else
        {
            resultsList=savedInstanceState.getParcelableArrayList("resultslist");
            movieListAdapter = new MovieListAdapter(getActivity(),resultsList );
            recyclerView.invalidate();
            recyclerView.setAdapter(movieListAdapter);
        }

        recyclerView.smoothScrollToPosition(sharedPreferences.getInt("POSITION", 0));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sharedPreferences.edit().putInt("POSITION",position).commit();
                isFav=false;
                Cursor c=getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                        new String[]{MovieColumns.MOVIE_ID},null,null,null);
                if(c!=null && c.moveToFirst())
                {
                    do {
                        if(c.getString(c.getColumnIndex(MovieColumns.MOVIE_ID))
                                .equalsIgnoreCase(resultsList.get(position).getId())){
                            isFav=true;
                            break;
                        }

                    }while(c.moveToNext());
                }
                ((Callback1)getActivity()).callMovieDetail(resultsList.get(position));
            }
        }
        ));
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    public  void callTMDB(String sort_by)
    {
        if(resultsList!=null)
        {
            resultsList.clear();
            recyclerView.invalidate();
        }

        if(!sort_by.equalsIgnoreCase(getString(R.string.fav_url))){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url_tmdb))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieDbService movieDbService=retrofit.create(MovieDbService.class);
            Call<MoviesDbDto> moviesDbDtoCall=movieDbService.getMovieDb(getString(R.string.movie_api_key), sort_by, "1");
            moviesDbDtoCall.enqueue(new Callback<MoviesDbDto>() {
                @Override
                public void onResponse(Call<MoviesDbDto> call, Response<MoviesDbDto> response) {
                    moviesDbDto = response.body();
                    resultsList.addAll(Arrays.asList(moviesDbDto.getResults()));
                    movieListAdapter = new MovieListAdapter(getActivity(), Arrays.asList(moviesDbDto.getResults()));
                    recyclerView.invalidate();
                    recyclerView.setAdapter(movieListAdapter);
                }

                @Override
                public void onFailure(Call<MoviesDbDto> call, Throwable t) {
                }
            });

        }
        else{
            Cursor c=getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    MovieColumns.PROJECTION_ALL,null,null,null);

            if(c!=null && c.moveToFirst())
            {
                do {
                    Results results=new Results();
                    results.setId(c.getString(c.getColumnIndex(MovieColumns.MOVIE_ID)));
                    results.setBackdrop_path(c.getString(c.getColumnIndex(MovieColumns.BACKDROP_PATH)));
                    results.setTitle(c.getString(c.getColumnIndex(MovieColumns.TITLE)));
                    results.setOverview(c.getString(c.getColumnIndex(MovieColumns.OVERVIEW)));
                    results.setPoster_path(c.getString(c.getColumnIndex(MovieColumns.POSTER_PATH)));
                    results.setRelease_date(c.getString(c.getColumnIndex(MovieColumns.RELEASE_DATE)));
                    results.setVote_average(c.getString(c.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
                    results.setVote_count(c.getString(c.getColumnIndex(MovieColumns.VOTE_COUNT)));
                    resultsList.add(results);

                }while (c.moveToNext());

            }
            movieListAdapter = new MovieListAdapter(getActivity(), resultsList);
            recyclerView.invalidate();
            recyclerView.setAdapter(movieListAdapter);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedPreferences.getString(getString(R.string.sort_by_key_param),null)!=null)
        if(sharedPreferences.getString(getString(R.string.sort_by_key_param),null).equalsIgnoreCase(getString(R.string.fav_url)))
        {
            callTMDB(sharedPreferences.getString(getString(R.string.sort_by_key_param),null));
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(myPrefListner);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(myPrefListner);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(resultsList!=null)
        {
            outState.putParcelableArrayList("resultslist",resultsList);
        }
        super.onSaveInstanceState(outState);
    }
}


