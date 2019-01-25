package com.example.xtalker.manprotest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ferry on 5/21/2017.
 */

public class DetailWisata extends Fragment{
    public View view, views;
    public Context ctx;
    public static CWisata dataWisata;
    public Timer timer = new Timer();
    ViewPager vp;
    ViewPagerAdapter vpAdapter;
    ArrayList<CSchedule> listSchedule = new ArrayList<CSchedule>();

    //ZXingScannerView scanner;
    Boolean status = false;

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            Log.e("src",src);
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("Exception",e.getMessage());
//            return null;
//        }
//    }

    public DetailWisata() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ctx = context;
        super.onAttach(context);
    }

    public static void setData(CWisata data){
        dataWisata = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_detailwisata, container, false);
        views =  inflater.inflate(R.layout.fragment_detailwisata, container, false);
        loadSchedule();

        TextView namaWisata = (TextView)view.findViewById(R.id.namaWisata);
        namaWisata.setText(dataWisata.namawisata);
        TextView deskripsi = (TextView)view.findViewById(R.id.deskripsi);
        deskripsi.setText(dataWisata.deskripsi);


        //TextView jamBuka = (TextView)view.findViewById(R.id.jamBuka);
        //jamBuka.setText(dataWisata.jambuka + " - " + dataWisata.jamtutup);
        TextView telepon = (TextView)view.findViewById(R.id.telepon);
        telepon.setText(dataWisata.fax);
        TextView alamat = (TextView)view.findViewById(R.id.alamat);
        alamat.setText(dataWisata.alamat);
        TextView kota = (TextView)view.findViewById(R.id.kota);
        kota.setText(dataWisata.kota);
        //TextView rating = (TextView)view.findViewById(R.id.rating);
        //rating.setText(new DecimalFormat("#.#").format(dataWisata.rating));

        String URLImages = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/"+dataWisata.gambar;

        ImageButton location = (ImageButton)view.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new Location();
                Location.setLatLng(dataWisata.namawisata, dataWisata.latitude, dataWisata.longitude);
                if(fragment != null){
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_side, fragment).commit();
                }
            }
        });

        ImageButton qr = (ImageButton)view.findViewById(R.id.scan);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrIntent = new Intent(DetailWisata.this.getActivity(), DetailItem.class);
                qrIntent.putExtra("mode", "scan");
                startActivity(qrIntent);
            }
        });

        ImageButton denah = (ImageButton)view.findViewById(R.id.denah);
        denah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent denahIntent = new Intent(DetailWisata.this.getActivity(), Denah.class);
                startActivity(denahIntent);
            }
        });


        ImageView vw = (ImageView)view.findViewById(R.id.vwMuseum);
        //vw.setImageBitmap(getBitmapFromURL(URLImages[0]));
        Picasso.with(getContext()).load(URLImages).into(vw);


        String URLVideo = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/videos/"+dataWisata.video;
        Uri uri = Uri.parse(URLVideo);
        final VideoView video = (VideoView) view.findViewById(R.id.videoMuseum);
        final MediaController media = new MediaController(ctx);
        video.setVideoURI(uri);
        video.setMediaController(media);
        media.setAnchorView(video);
        video.pause();
        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });


        String URLAudio = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/audios/"+dataWisata.audio;
        Uri uri_audio = Uri.parse(URLAudio);
        final VideoView audio = (VideoView) view.findViewById(R.id.audioMuseum);
        final MediaController media_audio = new MediaController(ctx);
        audio.setVideoURI(uri_audio);
        audio.setMediaController(media_audio);
        media_audio.setAnchorView(audio);
        audio.start();
        audio.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        ScrollView sc_view = (ScrollView) view.findViewById(R.id.scView);
        sc_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                media.hide();
                media_audio.hide();
            }
        });

//        vp = (ViewPager)view.findViewById(R.id.viewPager);
//        vpAdapter = new ViewPagerAdapter(ctx, URLImages);
//        vp.setAdapter(vpAdapter);

        //timer.scheduleAtFixedRate(new MyTimer(), 2000, 4000);

//        LinearLayout commentSection = (LinearLayout)view.findViewById(R.id.commentSection);
//        if(CGlobal.user.isUserLoggedIn()){
//            commentSection.setVisibility(View.VISIBLE);
//        }
//        else{
//            commentSection.setVisibility(View.GONE);
//        }
//
//        getReview getComment = new getReview();
//        String [] params = new String[1];
//        params[0] = CGlobal.urlServer + "getreview.php?id_wisata=" + dataWisata.idwisata;
//        getComment.execute(params);
//
//        Button postReview = (Button)view.findViewById(R.id.postReview);
//        final EditText comment = (EditText)view.findViewById(R.id.comment);
//        final RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
//        postReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setReview task = new setReview();
//                String [] params = new String[1];
//                params[0] = CGlobal.urlServer + "setreview.php?id_wisata=" + dataWisata.idwisata + "&id_user=" + CGlobal.user.pref.getString(CGlobal.user.KEY_ID, "") + "&comment=" + comment.getText().toString() + "&rating=" + ratingBar.getRating();
//                task.execute(params);
//            }
//        });
        return view;
    }


//    @Override
//    public void handleResult(Result result) {
//        Toast.makeText(ctx, result.getText(), Toast.LENGTH_LONG).show();
//        scanner.resumeCameraPreview(this);
//    }
//
//    public void clicked (View view){
//        scanner = new ZXingScannerView(ctx);
//        //LinearLayout content = (LinearLayout) views.findViewById(R.id.camera);
//        //content.addView(scanner);
//        DetailWisata.this.getActivity().setContentView(scanner);
//        scanner.setResultHandler(this);
//        status = true;
//        scanner.startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (status == true){
//            scanner.stopCamera();
//        }
//    }

    public class MyTimer extends TimerTask{
        @Override
        public void run(){
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vp.getCurrentItem() < vpAdapter.getCount() - 1) {
                            vp.setCurrentItem(vp.getCurrentItem() + 1);
                        } else {
                            vp.setCurrentItem(0);
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                timer.cancel();
            }
        }
    }

    public class setReview extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(true);
            pDialog.show();
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
                    if(jsonData.getString("status").equals("ok")){
                        Toast.makeText(ctx,"Review Posted",Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.content_side, new DetailWisata()).addToBackStack(null).commit();
                        DetailWisata.setData(dataWisata);
                    }
                    else{
                        Toast.makeText(ctx,"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download failed 2",Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(s);
        }
    }

    public void loadSchedule(){
        getSchedule schedule_task = new getSchedule();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getschedule.php?id_museum=" + dataWisata.idwisata;
        schedule_task.execute(params);
    }
    public class getSchedule extends  AsyncTask<String,Void,String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(true);
            pDialog.show();
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
                    if(jsonData.getString("status").equals("ok")){
                        JSONArray dataJSONSchedule = jsonData.getJSONArray("schedule_wisata");
                        for(int i=0; i<dataJSONSchedule.length(); i++){
                            JSONObject scheduleObj = dataJSONSchedule.getJSONObject(i);
                            CSchedule schedule = new CSchedule();
                            schedule.Day = scheduleObj.getString("schedule_day");
                            schedule.startTime = scheduleObj.getString("ScheduleStartTime");
                            schedule.endTime = scheduleObj.getString("ScheduleEndTime");


                            TableLayout layoutSchedule = (TableLayout)view.findViewById(R.id.layout_schedule);

                            TableRow tr = new TableRow(ctx);
                            //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            //tr.setLayoutParams(lp);

                            final float scale = getContext().getResources().getDisplayMetrics().density;
                            int pixel;
                            TextView day = new TextView(ctx);
                            pixel = (int) (15 * scale + 0.5f);
                            day.setPadding(pixel, 0, 0, 0);
                            pixel = (int) (95 * scale + 0.5f);
                            day.setWidth(pixel);
                            day.setText("  "+schedule.Day);
                            TextView startTime = new TextView(ctx);
                            pixel = (int) (15 * scale + 0.5f);
                            startTime.setPadding(pixel, 0, 0, 0);
                            startTime.setText(schedule.startTime);
                            TextView dash = new TextView(ctx);
                            pixel = (int) (10 * scale + 0.5f);
                            dash.setPadding(pixel, 0, 0, 0);
                            dash.setText("-");
                            TextView endTime = new TextView(ctx);
                            pixel = (int) (10 * scale + 0.5f);
                            endTime.setPadding(pixel, 0, 0, 0);
                            endTime.setText(schedule.endTime);

                            tr.addView(day);
                            tr.addView(startTime);
                            tr.addView(dash);
                            tr.addView(endTime);
                            layoutSchedule.addView(tr, i);

                        }
                        ScrollView scroll = (ScrollView) view.findViewById(R.id.scView);
                        scroll.scrollTo(0,0);

                        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        //IBinder binder = view.getWindowToken();
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    else{
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
    }
    public class getReview extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(true);
            pDialog.show();
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
                    if(jsonData.getString("status").equals("ok")){
                        JSONArray dataJSONReview = jsonData.getJSONArray("review");
                        ArrayList<CReview> reviewsList = new ArrayList<>();
                        reviewsList.clear();
                        int h=0;
                        for(int i=0; i<dataJSONReview.length(); i++){
                            h++;
                            JSONObject reviewObj = dataJSONReview.getJSONObject(i);
                            CReview review = new CReview();
                            review.name = reviewObj.getString("nama");
                            review.comment = reviewObj.getString("comment");
                            review.date = reviewObj.getString("date");
                            review.rating = reviewObj.getDouble("rating");
                            reviewsList.add(review);
                        }

                        CReviewAdapter adapter = new CReviewAdapter(ctx, R.layout.item_review, reviewsList);
                        //ListView listViewReview = (ListView)view.findViewById(R.id.listViewReview);
                        //listViewReview.getLayoutParams().height = h * 120;
                        //listViewReview.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(ctx,"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download failed 2",Toast.LENGTH_SHORT).show();
                }
            }
            ScrollView scrollView = (ScrollView)view.findViewById(R.id.scView);
            scrollView.smoothScrollTo(0,0);
            super.onPostExecute(s);
        }
    }

    public class CReview{
        public String name;
        public String comment;
        public String date;
        public double rating;
    }
}
