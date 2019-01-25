package com.example.xtalker.manprotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Ferry on 5/21/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    public Context context;
    public LayoutInflater inflater;
    public String images[] = new String[3];

    public ViewPagerAdapter(Context ctx, String[] picture) {
        this.context = ctx;
        this.images = picture;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_item, null);
        ImageView picture = (ImageView)view.findViewById(R.id.imageView);
        picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        picture.setAdjustViewBounds(true);
        DownloadImageTask download = new DownloadImageTask(picture);
        download.execute("http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/" + images[position]);
        ViewPager vp = (ViewPager)container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager)container;
        View view = (View)object;
        vp.removeView(view);
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
