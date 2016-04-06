package com.udacity.stage2.popularmoviess2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.stage2.popularmoviess2.R;
import com.udacity.stage2.popularmoviess2.dto.review.Results;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rashida on 3/4/2016.
 */
public class ReviewListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Results> mResultsArrayList;

    public ReviewListAdapter(Context mContext,List<Results> mResultsArrayList){

        this.mContext=mContext;
        this.mResultsArrayList=mResultsArrayList;
    }
    @Override
    public int getCount() {
        return mResultsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResultsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reviewlist_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.author_txt.setText(mResultsArrayList.get(position).getAuthor());
            holder.content_txt.setText(mResultsArrayList.get(position).getContent());
            holder.url_txt.setText(mResultsArrayList.get(position).getUrl());
        }
        return convertView;
    }
    static class ViewHolder
    {
        @Bind(R.id.author_txt) TextView author_txt;
        @Bind(R.id.content_txt) TextView content_txt;
        @Bind(R.id.url_txt) TextView url_txt;
        public ViewHolder(View view)
        {
            ButterKnife.bind(this,view);
        }
    }
}

