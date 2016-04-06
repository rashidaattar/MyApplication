package com.udacity.stage2.popularmoviess2.retrofitInterface;

import com.udacity.stage2.popularmoviess2.dto.moviedetail.MoviesDbDto;
import com.udacity.stage2.popularmoviess2.dto.review.ReviewsDto;
import com.udacity.stage2.popularmoviess2.dto.video.MovieVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rashida on 2/28/2016.
 */
public interface MovieDbService {

    @GET("/3/discover/movie")
    Call<MoviesDbDto> getMovieDb(@Query("api_key") String api,@Query("sort_by") String sort,@Query("page") String page);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsDto> getMovieReview(@Path("id") String id, @Query("api_key") String api);

    @GET("/3/movie/{id}/videos")
    Call<MovieVideo> getMovieVideo(@Path("id") String id, @Query("api_key") String api);
}
