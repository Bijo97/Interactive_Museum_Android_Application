package com.example.xtalker.manprotest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ferry on 6/4/2017.
 */

public class CNewsAdapter extends ArrayAdapter<CNews> {

    Context context;
    int layoutResourceId;
    List<CNews> list = null;
    ArrayList<CNews> data;

    public CNewsAdapter(Context context, int layoutResourceId, List<CNews> listData) {
        super(context, layoutResourceId, listData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = listData;
        this.data = new ArrayList<CNews>();
        this.data.addAll(listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CNewsHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CNewsHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.picture = (ImageView)row.findViewById(R.id.picture);
            holder.picture.getLayoutParams().height = 300;
            holder.picture.getLayoutParams().width = 300;

            row.setTag(holder);
        }
        else{
            holder = (CNewsHolder)row.getTag();
        }

        holder.title.setText(list.get(position).title);
        DownloadImageTask download = new DownloadImageTask(holder.picture);
        download.execute(CGlobal.urlServer + list.get(position).image);
        Log.i("Task","Image Downloaded");

        return row;
    }

    static class CNewsHolder{
        TextView title;
        ImageView picture;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
