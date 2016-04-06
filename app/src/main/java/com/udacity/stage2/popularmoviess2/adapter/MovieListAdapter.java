package com.udacity.stage2.popularmoviess2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.dto.moviedetail.Results;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rashida on 2/28/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context mContext;
    private List<Results> mResultsArrayList;


    public MovieListAdapter(Context context,List<Results> resultsArrayList)
    {
        mContext=context;
        mResultsArrayList=resultsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_adapter_view, null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String base_url=mContext.getString(R.string.base_url_img)+ getPixelDensity();
        String final_url=base_url+mResultsArrayList.get(position).getPoster_path();
        Glide.with(mContext).load(final_url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(mResultsArrayList.size()>0 && mResultsArrayList!=null)
            return mResultsArrayList.size();
        else
            return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view) ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public String getPixelDensity()
    {
        float density = mContext.getResources().getDisplayMetrics().density;

        if (density == 0.75f)
        {
            return mContext.getString(R.string.pixel_img_url_w92);
        }
        else if (density >= 1.0f && density < 1.5f)
        {
            return mContext.getString(R.string.pixel_img_url_w154);
        }
        else if (density == 1.5f)
        {
            // HDPI
            return mContext.getString(R.string.pixel_img_url_w185);
        }
        else if (density > 1.5f && density <= 2.0f)
        {
            // XHDPI
            return mContext.getString(R.string.pixel_img_url_w342);
        }
        else if (density > 2.0f && density <= 3.0f)
        {
            // XXHDPI
            return mContext.getString(R.string.pixel_img_url_w500);
        }
        else
        {
            // XXXHDPI
            return mContext.getString(R.string.pixel_img_url_w780);
        }
        //return null;
    }
}
