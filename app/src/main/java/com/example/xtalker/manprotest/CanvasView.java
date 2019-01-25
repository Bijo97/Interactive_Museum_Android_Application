package com.example.xtalker.manprotest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Bijo97 on 28/11/2017.
 */

public class CanvasView extends View {
    float scaling = 1.5f;
    Bitmap gambar, newGambar;
    int x, y;
    Context ctx;
    public SharedPreferences shared;
    int[] intermap_id = new int[10];
    int[] intermap_floor = new int[10];
    String[] intermap_image = new String[10];
    public ArrayList<CDenah> denahs = new ArrayList<CDenah>();
    // public CDenah[] denah = new CDenah[10];

    public CanvasView(Context context) {
        super(context);
        ctx = context;
        final ImageView iv = (ImageView) findViewById(R.id.iv);
//        Picasso.with(ctx)
//                .load("http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/denah.png")
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        // loaded bitmap is here (bitmap)
//                        iv.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
        //gambar = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        //gambar = BitmapFactory.decodeResource(getResources(), R.drawable.denah);
        x = 0;
        y = 0;
        LoadData();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

//        Rect ourRect = new Rect();
//        ourRect.set(0, 0, canvas.getWidth(), canvas.getHeight() / 2);
//
//        Paint blue = new Paint();
//        blue.setColor(Color.BLUE);
//        blue.setStyle(Paint.Style.FILL);
//
//        canvas.drawRect(ourRect, blue);
        if (gambar != null){
//            int width = gambar.getWidth();
//            int height = gambar.getHeight();
//            float ratioBitmap = (float) width / (float) height;
//            float ratioMax = (float) canvas.getWidth() / (float) canvas.getHeight();
//
//            int finalWidth = canvas.getWidth();
//            int finalHeight = canvas.getHeight();
//            if (ratioMax > ratioBitmap) {
//                finalWidth = (int) ((float)canvas.getWidth() * ratioBitmap);
//            } else {
//                finalHeight = (int) ((float)canvas.getHeight() / ratioBitmap);
//            }

//            newGambar = Bitmap.createScaledBitmap(gambar, gambar.getWidth(), gambar.getHeight(), true);
            //newGambar = Bitmap.createScaledBitmap(gambar, (int) finalWidth, (int) finalHeight, true);
            Paint p = new Paint();

            canvas.save();
            canvas.scale(scaling, scaling);
            canvas.drawBitmap(gambar, x, y, p);

            final float scale = getContext().getResources().getDisplayMetrics().density;
            int m = 0, n = 0;
            Log.d("Ukuran", Integer.toString(denahs.size()));
            for (int i = 0; i < denahs.size(); i++){
                Rect kotak = new Rect();
                Log.d("jumlah", Integer.toString(m)+" "+Integer.toString(n));
//                m = (int) (denahs.get(i).koorX.intValue() * scale + 0.5f);
//                n = (int) (denahs.get(i).koorY.intValue() * scale + 0.5f);
//                int panjang = (int) (denahs.get(i).panjang.intValue() * scale + 0.5f);
//                int lebar = (int) (denahs.get(i).lebar.intValue() * scale + 0.5f);

                m = denahs.get(i).koorX.intValue();
                n = denahs.get(i).koorY.intValue();
                int panjang = denahs.get(i).panjang.intValue();
                int lebar = denahs.get(i).lebar.intValue();
                kotak.set(m, n, m + panjang, n + lebar);

                Paint blue = new Paint();
                blue.setColor(Color.BLUE);
                blue.setStyle(Paint.Style.FILL);
                canvas.drawRect(kotak, blue);
                Paint ptemp = new Paint();
                ptemp.setColor(Color.WHITE);
                ptemp.setStyle(Paint.Style.FILL);
                ptemp.setTextSize(20);
                canvas.drawText(denahs.get(i).text, m, n, ptemp);
            }
            canvas.restore();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float a = event.getX();
        float b = event.getY();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                int c = 0, d = 0;
                for (int i = 0; i < denahs.size(); i++){
                    c = denahs.get(i).koorX.intValue();
                    d = denahs.get(i).koorY.intValue();
//                    int m = (int) ((denahs.get(i).koorX.intValue() * scale + 0.5f) * scaling);
//                    int n = (int) ((denahs.get(i).koorY.intValue() * scale + 0.5f) * scaling);
//                    int panjang = (int) ((denahs.get(i).panjang.intValue() * scale + scaling) * scaling);
//                    int lebar = (int) ((denahs.get(i).lebar.intValue() * scale + 0.5f) * scaling);


                    int m = (int) (denahs.get(i).koorX.intValue() * scaling);
                    int n = (int) (denahs.get(i).koorY.intValue() * scaling);
                    int panjang = (int) (denahs.get(i).panjang.intValue() * scaling);
                    int lebar = (int) (denahs.get(i).lebar.intValue() * scaling);
                    //Check if the x and y position of the touch is inside the bitmap
                    if( a >= m && a <= m + panjang && b >= n && b <= n + lebar){
                        //Log.e("TOUCHED", "X: " + x + " Y: " + y);
                        Intent inRuangan = new Intent(getContext(), DetailItem.class);
                        inRuangan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String kata = denahs.get(i).room_id+"_room";
                        inRuangan.putExtra("mode", "map");
                        inRuangan.putExtra("nama", kata);
                        //inRuangan.putExtra("id", 5);
                        getContext().startActivity(inRuangan);
                        //Bitmap touched
                        break;
                    }
                }

                return true;
        }
        return false;
    }

    public void LoadData(){
        CanvasView.getDataTask task = new CanvasView.getDataTask();
        String [] params = new String[1];
        CGlobal global = new CGlobal();
        shared = ctx.getSharedPreferences("TEXT", Context.MODE_PRIVATE);
        String id = shared.getString("id_museum", "");
        params[0] = global.urlServer + "getmap.php?id_museum=" + id;
        Log.d("Konek", params[0]);
        task.execute(params);
    }

    public class getDataTask extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            Log.d("Progress", "abc");
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(true);
            pDialog.show();
            Log.d("Progress Lagi", "def");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            }catch( Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s!=null){
                try {
                    JSONObject jsonData = new JSONObject(s);
                    //LOAD ITEM
                    if(jsonData.getString("status").equals("ok")){
                        JSONArray dataJSONWisata = jsonData.getJSONArray("denah");
                        Log.d("panjang", Integer.toString(dataJSONWisata.length()));
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            intermap_id[i] = wisataObj.getInt("intermap_id");
                            intermap_floor[i] = wisataObj.getInt("floor");
                            intermap_image[i] = wisataObj.getString("intermap_photoPath");
                        }

                        JSONArray dataJSON2 = jsonData.getJSONArray("detail");
                        //Log.d("length", Integer.toString(dataJSON2.length()));
                        for (int i = 0; i < dataJSON2.length(); i++){
                            CDenah denah = new CDenah();
                            JSONObject obj2 = dataJSON2.getJSONObject(i);
                            Log.d("detail_id", Integer.toString(obj2.getInt("interdetail_id")));
                            denah.detail_id = obj2.getInt("interdetail_id");
                            denah.intermap_id = obj2.getInt("intermap_id");
                            denah.image = obj2.getString("image");
                            denah.text = obj2.getString("text");
                            denah.panjang = obj2.getDouble("panjang");
                            denah.lebar = obj2.getDouble("lebar");
                            denah.koorX = obj2.getDouble("x");
                            denah.koorY = obj2.getDouble("y");
                            denah.description = obj2.getString("description");
                            denah.room_id = obj2.getString("css");
                            denahs.add(denah);
                            Log.d("Size", Integer.toString(denahs.size()));
                        }
                        Log.d("intermap", "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/" + intermap_image[0]);
                        new GetBitmap().execute("http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/" + intermap_image[0]);
//                        gambar = getBitmapFromURL("http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/" + intermap_image[0]);
                    } else {
                        Toast.makeText(ctx,"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download failed 2",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ctx,"Download failed",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }

        public class GetBitmap extends AsyncTask<String,Void,Bitmap> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("Progress", "abc");
                pDialog = new ProgressDialog(ctx);
                pDialog.setMessage("Loading data dari server");
                pDialog.setCancelable(true);
                pDialog.show();
                Log.d("Progress Lagi", "def");
            }

            @Override

            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                pDialog.dismiss();
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    Log.d("ampas", params[0]);
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    gambar = BitmapFactory.decodeStream(input);
                    return gambar;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

//    public Bitmap getBitmapFromURL(String src) {
//
//    }
}
