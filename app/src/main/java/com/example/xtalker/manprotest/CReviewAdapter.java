package com.example.xtalker.manprotest;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ferry on 6/5/2017.
 */

public class CReviewAdapter extends ArrayAdapter<DetailWisata.CReview> {

    Context context;
    int layoutResourceId;
    ArrayList<DetailWisata.CReview> data;

    public CReviewAdapter(Context context, int layoutResourceId, ArrayList<DetailWisata.CReview> listData) {
        super(context, layoutResourceId, listData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = new ArrayList<DetailWisata.CReview>();
        this.data.addAll(listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView nameTv = (TextView)row.findViewById(R.id.name);
        TextView commentTv = (TextView)row.findViewById(R.id.comment);
        TextView dateTv = (TextView)row.findViewById(R.id.date);
        RatingBar ratingBar = (RatingBar) row.findViewById(R.id.ratingBar);

        Log.i("Nama",Integer.toString(position) + data.get(position).name);
        Log.i("Nama",data.get(position).name);
        nameTv.setText(data.get(position).name);
        commentTv.setText(data.get(position).comment);
        dateTv.setText(data.get(position).date);
        ratingBar.setRating((float)data.get(position).rating);

        return row;
    }
}
