package com.example.xtalker.manprotest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

//import android.media.session.MediaController;

/**
 * Created by Bijo97 on 13/11/2017.
 */

public class DetailRuangan extends AppCompatActivity{
    public int idruangan;
    public CRoom detailRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailruangan);

        Bundle extras = getIntent().getExtras();
        idruangan = extras.getInt("id");
        detailRoom = new CRoom();
        LoadData(idruangan);
    }

//    @Override
//    public void onBackPressed() {
//        Intent backIntent = new Intent(DetailItem.this, listwisata.class);
//        startActivity(backIntent);
//    }

    public void LoadData(int id){
        getDataTask task = new getDataTask();
        String [] params = new String[1];
        CGlobal global = new CGlobal();
        params[0] = global.urlServer + "getitem.php?id_item=" + id + "&jenis=room";
        Log.d("Konek", params[0]);
        task.execute(params);
    }

    public class getDataTask extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            Log.d("Progress", "abc");
            pDialog = new ProgressDialog(getApplicationContext());
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

//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s!=null){
                try {
                    JSONObject jsonData = new JSONObject(s);
                    //LOAD ITEM
                    if(jsonData.getString("status").equals("room")){
                        JSONArray dataJSONWisata = jsonData.getJSONArray("item");
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            detailRoom.idruangan = wisataObj.getInt("room_id");
                            detailRoom.idmuseum = wisataObj.getInt("museum_id");
                            detailRoom.namaruangan = wisataObj.getString("roomName");
                            detailRoom.description = wisataObj.getString("roomDesc");
                            //detailRoom.video = wisataObj.getString("roomVideo");
                            //detailRoom.audio = wisataObj.getString("roomAudio");
                            //detailRoom.photo = wisataObj.getString("roomPhotoPath");
                            detailRoom.floor = wisataObj.getInt("floor");
                            detailRoom.qr = wisataObj.getString("qrimage");
                            break;
                        }
                        TextView namaRuangan = (TextView) findViewById(R.id.namaRuangan);
                        TextView deskripsiRuangan = (TextView) findViewById(R.id.deskripsiRuangan);
                        //ImageView vwItem = (ImageView) findViewById(R.id.vwRuangan);

                        namaRuangan.setText(detailRoom.namaruangan);
                        deskripsiRuangan.setText(detailRoom.description);


                        //setMedia(detailRoom.audio, detailRoom.video);

                        Log.i("check_item", "begin Load");

                        ScrollView scroll = (ScrollView) findViewById(R.id.scViews);
                        scroll.scrollTo(0,0);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Download failed 2",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Download failed",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    public void setMedia(String audio_name, String video_name){
        String URLVideo = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/videos/"+video_name;
        Log.i("check_media", URLVideo);
        Uri uri = Uri.parse(URLVideo);
        final VideoView video = (VideoView) findViewById(R.id.videoItem);
        final MediaController media = new MediaController(getApplicationContext());
        video.setVideoURI(uri);
        video.setMediaController(media);
        media.setAnchorView(video);
        video.seekTo(1000);
        video.pause();

        String URLAudio = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/audios/"+audio_name;
        Log.i("check_media", URLAudio);
        Uri uri_audio = Uri.parse(URLAudio);
        final VideoView audio = (VideoView) findViewById(R.id.audioItem);
        final MediaController media_audio = new MediaController(getApplicationContext());
        audio.setVideoURI(uri_audio);
        audio.setMediaController(media_audio);
        media_audio.setAnchorView(audio);
        audio.start();

        ScrollView sc_view = (ScrollView) findViewById(R.id.scViews);
        sc_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                media.hide();
                media_audio.hide();
            }
        });
    }
}
