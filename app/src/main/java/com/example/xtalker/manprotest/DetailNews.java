package com.example.xtalker.manprotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Ferry on 6/7/2017.
 */

public class DetailNews extends Fragment {
    public View view;
    public Context ctx;
    public static CNews dataNews;

    public DetailNews(){
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ctx = context;
        super.onAttach(context);
    }

    public static void setData(CNews data){
        dataNews = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detailnews, container, false);

        TextView title = (TextView)view.findViewById(R.id.title);
        TextView content = (TextView)view.findViewById(R.id.content);
        ImageView picture = (ImageView)view.findViewById(R.id.picture);
        title.setText(dataNews.title);
        content.setText(dataNews.content);
        DownloadImageTask download = new DownloadImageTask(picture);
        download.execute(CGlobal.urlServer + dataNews.image);

        return view;
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