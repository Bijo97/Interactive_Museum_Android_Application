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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Ferry on 6/9/2017.
 */

public class CEventAdapter extends ArrayAdapter<Event.CEvent> {

    Context context;
    int layoutResourceId;
    List<Event.CEvent> list = null;
    ArrayList<Event.CEvent> data;

    public CEventAdapter(Context context, int layoutResourceId, List<Event.CEvent> listData) {
        super(context, layoutResourceId, listData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = listData;
        this.data = new ArrayList<Event.CEvent>();
        this.data.addAll(listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView titleTv = (TextView)row.findViewById(R.id.title);
        TextView descriptionTv = (TextView)row.findViewById(R.id.description);
        TextView locationTv = (TextView)row.findViewById(R.id.location);
        TextView date = (TextView)row.findViewById(R.id.date);

        titleTv.setText(data.get(position).title);
        descriptionTv.setText(data.get(position).description);
        locationTv.setText(data.get(position).location);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String startDate = formateDateFromstring("yyyy-MM-dd","dd/MM/yyyy",data.get(position).startDate);
        String endDate = formateDateFromstring("yyyy-MM-dd","dd/MM/yyyy",data.get(position).endDate);
        date.setText(startDate + " - " + endDate);

        return row;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        java.util.Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }
}
