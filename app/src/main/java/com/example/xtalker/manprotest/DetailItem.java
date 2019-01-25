package com.example.xtalker.manprotest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
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

public class DetailItem extends AppCompatActivity{
    public CItem dataItem;
    public String[] kata;

    public SliderLayout slider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailitem);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("mode").equals("map")){
            kata = extras.getString("nama").split("_");
            Log.d("kata1", kata[0]);
            Log.d("kata2", kata[1]);
            LoadData(kata[0], kata[1]);
            //Toast.makeText(getApplicationContext(), "Map", Toast.LENGTH_SHORT).show();
        } else if (extras.getString("mode").equals("scan")){
            IntentIntegrator scanIntegrator = new IntentIntegrator(DetailItem.this);
            scanIntegrator.setPrompt("Scan a barcode");
            scanIntegrator.setBeepEnabled(true);
            scanIntegrator.setOrientationLocked(true);
            scanIntegrator.setBarcodeImageEnabled(true);
            scanIntegrator.initiateScan();
            //Toast.makeText(getApplicationContext(), "Scan", Toast.LENGTH_SHORT).show();
        }

        //final Bundle extra = getIntent().getExtras();
        slider = (SliderLayout) findViewById(R.id.slider);
        dataItem = new CItem();
//        LoadData(kata[0], kata[1]);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        /*As an example in order to get the content of what is scanned you can do the following*/
        if (scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents().toString();
            kata = scanContent.split("_");
            Log.d("kata1", kata[0]);
            Log.d("kata2", kata[1]);
            LoadData(kata[0], kata[1]);
        } else {
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//        Intent backIntent = new Intent(DetailItem.this, listwisata.class);
//        startActivity(backIntent);
//    }

    public void setData(CItem data){
        dataItem = data;
    }

    public void LoadData(String id, String jenis){
        getDataTask task = new getDataTask();
        String [] params = new String[1];
        CGlobal global = new CGlobal();
        params[0] = global.urlServer + "getitem.php?id_item=" + id + "&jenis=" + jenis;
        Log.d("Konek", params[0]);
        task.execute(params);
    }

    public class getDataTask extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            Log.d("Progress", "abc");
            pDialog = new ProgressDialog(DetailItem.this);
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s!=null){
                try {
                    JSONObject jsonData = new JSONObject(s);
                    //LOAD ITEM
                    if(jsonData.getString("status").equals("item")){
                        JSONArray dataJSONWisata = jsonData.getJSONArray("item");
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            dataItem.iditem = wisataObj.getInt("item_id");
                            dataItem.namaitem = wisataObj.getString("item_name");
                            dataItem.deskripsi = wisataObj.getString("ItemDescription");
                            dataItem.video = wisataObj.getString("VideoPath");
                            dataItem.audio = wisataObj.getString("AudioPath");
                            dataItem.qrcode = wisataObj.getString("QrImage");
                            break;
                        }
                        TextView namaitem = (TextView) findViewById(R.id.namaItem);
                        TextView deskripsiitem = (TextView) findViewById(R.id.deskripsiItem);
//                        ImageView vwItem = (ImageView) findViewById(R.id.vwItem);

                        namaitem.setText(dataItem.namaitem);
                        deskripsiitem.setText(dataItem.deskripsi);


                        setMedia(dataItem.audio, dataItem.video);

                        Log.i("check_item", "begin Load");

                        LoadPhotos(String.valueOf(dataItem.iditem)); //LOAD PHOTOS AFTER ITEM INFORMATION

                    } else if (jsonData.getString("status").equals("room")){
//                        LOAD ROOM
                        JSONArray dataJSONWisata = jsonData.getJSONArray("item");
                        JSONObject wisataObj = dataJSONWisata.getJSONObject(0);

                        TextView namaitem = (TextView) findViewById(R.id.namaItem);
                        TextView deskripsiitem = (TextView) findViewById(R.id.deskripsiItem);
//                        ImageView vwItem = (ImageView) findViewById(R.id.vwItem);

                        namaitem.setText(wisataObj.getString("roomName"));
                        deskripsiitem.setText(wisataObj.getString("roomDesc"));

                        String video_name = wisataObj.getString("roomVideo");
                        String audio_name = wisataObj.getString("roomAudio");
                        setMedia(audio_name, video_name);


                        String photo_name = wisataObj.getString("roomPhotoPath");
                        String urlPhoto = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/"+ photo_name;
                        HashMap<String,String> url_maps = new HashMap<String, String>();
                        url_maps.put(urlPhoto, urlPhoto);
                        for(String name : url_maps.keySet()){
                            DefaultSliderView textSliderView = new DefaultSliderView(DetailItem.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            slider.addSlider(textSliderView);
                        }
                        slider.setPresetTransformer(SliderLayout.Transformer.Default);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.stopAutoCycle();

                        ScrollView scroll = (ScrollView) findViewById(R.id.scView);
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

    public void LoadPhotos(String id){
        getPhotoTask photo_task = new getPhotoTask();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getphoto.php?id_item=" + id;
        Log.i("check_item", params[0]);
        photo_task.execute(params);
    }
    public class getPhotoTask extends  AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(DetailItem.this);
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
                    HashMap<String,String> url_maps = new HashMap<String, String>();
                    JSONObject jsonData = new JSONObject(s);
                    if(jsonData.getString("status").equals("ok")){
                        JSONArray dataJSONSchedule = jsonData.getJSONArray("photo_path");
                        for(int i=0; i<dataJSONSchedule.length(); i++){
                            JSONObject photoObj = dataJSONSchedule.getJSONObject(i);
                            String urlPhoto = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/photos/"+ photoObj.getString("photo_path");
                            url_maps.put(urlPhoto, urlPhoto);
                            Log.i("check_item", urlPhoto);
                        }
                        //Set to Slider
                        for(String name : url_maps.keySet()){
                            DefaultSliderView textSliderView = new DefaultSliderView(DetailItem.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            slider.addSlider(textSliderView);
                        }
                        slider.setPresetTransformer(SliderLayout.Transformer.Default);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.stopAutoCycle();

                        ScrollView scroll = (ScrollView) findViewById(R.id.scView);
                        scroll.scrollTo(0,0);
                    }
                    else{
                        Toast.makeText(DetailItem.this,"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailItem.this,"Download failed 2",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(DetailItem.this,"Download failed",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }

    }
    public void setMedia(String audio_name, String video_name){
        String URLVideo = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/videos/"+video_name;
        Log.i("check_media", URLVideo);
        Uri uri = Uri.parse(URLVideo);
        final VideoView video = (VideoView) findViewById(R.id.videoItem);
        final MediaController media = new MediaController(DetailItem.this);
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

        String URLAudio = "http://opensource.petra.ac.id/~m26415147/museum/MuseumAdmin/audios/"+audio_name;
        Log.i("check_media", URLAudio);
        Uri uri_audio = Uri.parse(URLAudio);
        final VideoView audio = (VideoView) findViewById(R.id.audioItem);
        final MediaController media_audio = new MediaController(DetailItem.this);
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

        ScrollView sc_view = (ScrollView) findViewById(R.id.scView);
        sc_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                media.hide();
                media_audio.hide();
            }
        });
    }
}
