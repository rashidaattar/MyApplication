package com.udacity.stage2.popularmoviess2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.adapter.ReviewListAdapter;
import com.udacity.stage2.popularmoviess2.data.MovieColumns;
import com.udacity.stage2.popularmoviess2.data.MovieProvider;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.MoviesDbDto;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.Results;
import com.udacity.stage2.popularmoviess2.dto.review.ReviewsDto;
import com.udacity.stage2.popularmoviess2.dto.video.MovieVideo;
import com.udacity.stage2.popularmoviess2.retrofitInterface.MovieDbService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private static final String ARG_PARAM1 = "result";
    private Results results;
    private  static Context mContext;
    public static String video_url_result;
    @Bind(R.id.movie_title) TextView movie_title;
    @Bind(R.id.overview_text) TextView overview_text;
    @Bind(R.id.ratingBar) RatingBar voter_rating;
    @Bind(R.id.poster_img) ImageView poster_img;
    @Bind(R.id.votes_text) TextView votes_text;
    @Bind(R.id.movie_date) TextView movie_date;
    //@Bind(R.id.toolbar_bottom) Toolbar toolbar;
    @Bind(R.id.fav_button) Button fav_button;

    @OnClick(R.id.review_button)
    public void getReview()
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieDbService movieDbService=retrofit.create(MovieDbService.class);
        Call<ReviewsDto> reviewsDtoCall=movieDbService.getMovieReview(results.getId(), getString(R.string.movie_api_key));
        reviewsDtoCall.enqueue(new Callback<ReviewsDto>() {
            @Override
            public void onResponse(Call<ReviewsDto> call, Response<ReviewsDto> response) {
                ReviewsDto reviewsDto=response.body();
                if(reviewsDto!=null)
                {
                    com.udacity.stage2.popularmoviess2.dto.review.Results[] results=reviewsDto.getResults();
                    if(results.length>0)
                    {
                        ReviewListAdapter reviewListAdapter=new ReviewListAdapter(mContext, Arrays.asList(results));
                        reviewListAdapter.notifyDataSetChanged();
                        new AlertDialog.Builder(mContext,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth).setTitle("Reviews")
                                .setAdapter(reviewListAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setInverseBackgroundForced(true).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No reviews", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsDto> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.trailer_button)
    public void trailerButton()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_tmdb))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieDbService movieDbService = retrofit.create(MovieDbService.class);
        Call<MovieVideo> reviewsDtoCall = movieDbService.getMovieVideo(results.getId(), getString(R.string.movie_api_key));
        reviewsDtoCall.enqueue(new Callback<MovieVideo>() {
            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                MovieVideo movieDto = response.body();
                if(movieDto!=null)
                {
                    final com.udacity.stage2.popularmoviess2.dto.video.Results[] results = movieDto.getResults();
                    if(results!=null && results.length!=0)
                    {
                        video_url_result=results[0].getKey();
                        if(results.length==1)
                        {

                            if(video_url_result!=null)
                            {
                                try
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_url_result));// Checking if youtube app is present
                                    startActivity(intent);

                                }
                                catch (ActivityNotFoundException ex)
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://" + "www.youtube.com" + "/watch?v=" + video_url_result));
                                    startActivity(intent);

                                }

                            }
                        }
                        else
                        {
                            String[] strings=new String[results.length];
                           for(int i=0;i<results.length;i++)
                           {
                               strings[i]="Trailer : "+(i+1);
                           }
                            //create arrayadapter here
                            ArrayAdapter<String> adapterTrailer = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, strings);
                            new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle("Trailers")
                                    .setAdapter(adapterTrailer, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try
                                            {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + results[which].getKey()));// Checking if youtube app is present
                                                startActivity(intent);

                                            }
                                            catch (ActivityNotFoundException ex)
                                            {
                                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://" + "www.youtube.com" + "/watch?v=" + results[which].getKey()));
                                                startActivity(intent);

                                            }



                                        }
                                    }).setInverseBackgroundForced(true).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No results",Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {

            }


        });
    }

    @OnClick(R.id.share_button)
    public void shareTrailer()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_tmdb))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieDbService movieDbService = retrofit.create(MovieDbService.class);
        Call<MovieVideo> reviewsDtoCall = movieDbService.getMovieVideo(results.getId(), getString(R.string.movie_api_key));
        reviewsDtoCall.enqueue(new Callback<MovieVideo>() {
            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                MovieVideo movieDto = response.body();
                if (movieDto != null) {
                    com.udacity.stage2.popularmoviess2.dto.video.Results[] results = movieDto.getResults();
                    if (results != null && results.length > 0)
                        video_url_result = results[0].getKey();
                    if (video_url_result != null && results.length != 0) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + video_url_result);
                        startActivity(Intent.createChooser(intent, "Share URL"));
                    } else {
                        Toast.makeText(getActivity(), "No Results found", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {

            }


        });
    }

    @OnClick(R.id.fav_button)
    public void addToFav()
    {
        if(!MovieActivityFragment.isFav)
        {
            MovieActivityFragment.isFav=true;
            if(results!=null)
            {
                ContentValues cv = new ContentValues();
                cv.put(MovieColumns.MOVIE_ID, results.getId());
                cv.put(MovieColumns.TITLE,results.getTitle());
                cv.put(MovieColumns.BACKDROP_PATH, results.getBackdrop_path());
                cv.put(MovieColumns.OVERVIEW,results.getOverview());
                cv.put(MovieColumns.POSTER_PATH,results.getPoster_path());
                cv.put(MovieColumns.RELEASE_DATE,results.getRelease_date());
                cv.put(MovieColumns.VOTE_AVERAGE,results.getVote_average());
                cv.put(MovieColumns.VOTE_COUNT,results.getVote_count());
                mContext.getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, cv);
            }
            fav_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_white_24dp),null,null);
            Cursor c=mContext.getContentResolver().query(MovieProvider.Movies.CONTENT_URI,new String[]{"id"},null,null,null);
            Log.d("here", "checking size: " + c.getCount());

        }
        else
        {
            MovieActivityFragment.isFav=false;
            int d=mContext.getContentResolver().delete(MovieProvider.Movies.CONTENT_URI,MovieColumns.MOVIE_ID+" LIKE ?",new String[]{results.getId()});
            Log.d("here", "row deleted: " + d);
            fav_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp), null, null);
        }

    }

    public static MovieDetailActivityFragment newInstance(Results results,Context mContext) {
        MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, results);
        MovieDetailActivityFragment.mContext=mContext;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = getArguments().getParcelable(ARG_PARAM1);
        }
        setRetainInstance(true);
        video_url_result=null;
    }

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(results!=null)
        {
            View v= inflater.inflate(R.layout.fragment_movie_detail, null, false);
            ButterKnife.bind(this,v);
            String base_url=getString(R.string.base_url_img)+ getPixelDensity();
            String final_url=base_url+results.getBackdrop_path();
            Glide.with(getActivity()).load(final_url).into(poster_img);
            if(MovieActivityFragment.isFav)
            {
                fav_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_white_24dp), null, null);
            }
            else
            {
                fav_button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp), null, null);
            }
            //to handle the delay while loading the image
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    movie_title.setText(results.getTitle());
                    overview_text.setText(results.getOverview());
                    if (Integer.parseInt(results.getVote_count()) == 1) {
                        votes_text.setText("( " + results.getVote_count() + "  " + "vote" + " ) ");
                    } else {
                        votes_text.setText("( " + results.getVote_count() + "  " + "votes" + " ) ");
                    }
                    voter_rating.setRating(Float.parseFloat(results.getVote_average()));
                    movie_date.setText((CharSequence) results.getRelease_date());
                }
            }, 2000);
            return v;
        }

        return null;
    }

    public void callTrailerService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieDbService movieDbService = retrofit.create(MovieDbService.class);
        Call<MovieVideo> reviewsDtoCall = movieDbService.getMovieVideo(results.getId(), getString(R.string.movie_api_key));
        reviewsDtoCall.enqueue(new Callback<MovieVideo>() {
            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                MovieVideo movieDto = response.body();
                com.udacity.stage2.popularmoviess2.dto.video.Results[] results = movieDto.getResults();
                video_url_result=results[0].getKey();
            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {

            }


        });
    }
    public String getPixelDensity() {
        float density = getResources().getDisplayMetrics().density;

        if (density == 0.75f)
        {
            return getString(R.string.pixel_img_url_w92);
        }
        else if (density >= 1.0f && density < 1.5f)
        {
            return getString(R.string.pixel_img_url_w92);
        }
        else if (density == 1.5f)
        {
            // HDPI
            return getString(R.string.pixel_img_url_w92);
        }
        else if (density > 1.5f && density <= 2.0f)
        {
            // XHDPI
            return getString(R.string.pixel_img_url_w185);
        }
        else if (density > 2.0f && density <= 3.0f)
        {
            // XXHDPI
            return getString(R.string.pixel_img_url_w185);
        }
        else
        {
            // XXXHDPI
            return getString(R.string.pixel_img_url_w500);
        }
        //return null;
    }

}
