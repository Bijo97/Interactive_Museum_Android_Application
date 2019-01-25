package com.example.xtalker.manprotest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by macbook on 5/2/17.
 */

public class CWisataAdapter extends ArrayAdapter<CWisata> {

    Context context;
    int layoutResourceId;
    List<CWisata> list = null;
    ArrayList<CWisata> data;

    public CWisataAdapter(Context context, int layoutResourceId, List<CWisata> listData) {
        super(context, layoutResourceId, listData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = listData;
        this.data = new ArrayList<CWisata>();
        this.data.addAll(listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CWisataHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CWisataHolder();
            holder.textViewNama = (TextView)row.findViewById(R.id.textViewNama);
            holder.city = (TextView)row.findViewById(R.id.city);
            //holder.distance = (TextView)row.findViewById(R.id.distance);
            //holder.point = (TextView)row.findViewById(R.id.point);
            holder.picture = (ImageView)row.findViewById(R.id.picture);
            holder.picture.getLayoutParams().height = 300;
            holder.picture.getLayoutParams().width = 300;

            row.setTag(holder);
        }
        else
        {
            holder = (CWisataHolder)row.getTag();
        }

        String URLImages = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/"+list.get(position).gambar;

        holder.textViewNama.setText(list.get(position).namawisata);
        holder.city.setText(list.get(position).kota);
        Picasso.with(getContext()).load(URLImages).into(holder.picture);
//        DownloadImageTask download = new DownloadImageTask(holder.picture);
//        download.execute(CGlobal.urlServer + list.get(position).gambar);
        //holder.distance.setText(new DecimalFormat("#.##").format(list.get(position).distance) + " Km from your location");
        //holder.point.setText("Point: " + Double.toString(list.get(position).total));

        return row;
    }

    static class CWisataHolder
    {
        TextView textViewNama;
        TextView city;
        ImageView picture;
        //TextView distance;
        TextView point;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(data);
        }
        else
        {
            for (CWisata wp : data)
            {
                if (wp.namawisata.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
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