package com.example.xtalker.manprotest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearLocation extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    public View view;
    public Context ctx;
    public ArrayList<CWisata> dataWisata;
    LocationManager lm;
    android.location.LocationListener listener;
    public static boolean NearLocation;

    public NearLocation() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ctx = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);
        NearLocation = true;

        dataWisata = new ArrayList<CWisata>();
        dataWisata.clear();

        android.location.Location loc = new android.location.Location("");

        getDataTask task = new getDataTask();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getwisata.php";
        task.execute(params);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(CWisata w : dataWisata){
            Log.i("Location", w.namawisata);
            LatLng marker = new LatLng(w.latitude, w.longitude);
            mMap.addMarker(new MarkerOptions().position(marker).title(w.namawisata));
        }

        try {
            mMap.setMyLocationEnabled(true);
        }

        catch (SecurityException e){e.printStackTrace();}
        LatLng posisiAwal = new LatLng(-7.338919, 112.736570);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posisiAwal));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
    }

    public class getDataTask extends AsyncTask<String,Void,String> {
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
                        JSONArray dataJSONWisata = jsonData.getJSONArray("wisata");
                        dataWisata.clear();
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            CWisata wisata = new CWisata();
                            wisata.idwisata = wisataObj.getInt("idwisata");
                            wisata.namawisata = wisataObj.getString("namawisata");
                            wisata.alamat = wisataObj.getString("alamat");
                            wisata.fax = wisataObj.getString("telpon");
                            wisata.latitude = wisataObj.getDouble("latitude");
                            wisata.longitude = wisataObj.getDouble("longitude");
                            wisata.gambar = wisataObj.getString("gambar");
                            wisata.video = wisataObj.getString("gambar2");
                            wisata.audio = wisataObj.getString("gambar3");
                            wisata.kota = wisataObj.getString("kota");
                            wisata.jambuka = wisataObj.getString("jambuka");
                            wisata.jamtutup = wisataObj.getString("jamtutup");
                            wisata.deskripsi = wisataObj.getString("keterangan");

                            dataWisata.add(wisata);
                        }

                        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                                        ,10);
                            }
                        }
                        SupportMapFragment mapFragment = (SupportMapFragment)getOuterClass().getChildFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(getOuterClass());

                        /*listener = new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(android.location.Location location) {
                                curLat = location.getLatitude();
                                curLng = location.getLongitude();
                                Log.i("Latitude Noob",Double.toString(location.getLatitude()));
                                Log.i("Longitude Noob",Double.toString(location.getLongitude()));
                            }
                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {}
                            @Override
                            public void onProviderEnabled(String s) {}
                            @Override
                            public void onProviderDisabled(String s) {}
                        };
                        lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                        */
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
    public NearLocation getOuterClass(){
        return this;
    }
}
