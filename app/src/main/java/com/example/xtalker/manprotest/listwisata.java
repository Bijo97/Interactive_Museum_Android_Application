package com.example.xtalker.manprotest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class listwisata extends Fragment {
    public View view;
    public Context ctx;
    public ListView listViewWisata;
    public CWisataAdapter adapter;
    public ArrayList<CWisata> dataWisata;
    public SharedPreferences shared;
    public SharedPreferences.Editor spEdit;

    public listwisata() {
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
        view = inflater.inflate(R.layout.fragment_listwisata, container, false);

        dataWisata = new ArrayList<CWisata>();
        dataWisata.clear();
//        shared = ctx.getSharedPreferences("TEXT", Context.MODE_PRIVATE);
//        spEdit.clear();
//        spEdit.commit();
        listViewWisata = (ListView) view.findViewById(R.id.listViewWisata);


        listViewWisata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getFragmentManager().beginTransaction().replace(R.id.content_side, new DetailWisata()).addToBackStack(null).commit();
                shared = ctx.getSharedPreferences("TEXT", Context.MODE_PRIVATE);
                spEdit = shared.edit();
                spEdit.putString("id_museum", Integer.toString(dataWisata.get(i).idwisata));
                spEdit.commit();
                DetailWisata.setData(dataWisata.get(i));
                updateRecommend task = new updateRecommend();
                String [] params = new String[1];
                params[0] = CGlobal.urlServer + "updaterecommend.php?id_user=" + CGlobal.user.pref.getString(CGlobal.user.KEY_ID, "") + "&id_wisata=" + dataWisata.get(i).idwisata;
                task.execute(params);
            }
        });

        LoadData();

        SearchView search = (SearchView)view.findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return false;
            }
        });
        return view;
    }

    public void LoadData(){
        getDataTask task = new getDataTask();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getwisata.php?id_user=" + CGlobal.user.pref.getString(CGlobal.user.KEY_ID, "");
        task.execute(params);
    }

    public class getDataTask extends AsyncTask<String,Void,String>{
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
                        String wisataValue = "";
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            CWisata wisata = new CWisata();
                            wisata.idwisata = wisataObj.getInt("museum_id");
                            wisata.namawisata = wisataObj.getString("museum_name");
                            wisata.alamat = wisataObj.getString("museum_address");
                            wisata.fax = wisataObj.getString("museum_fax");
                            wisata.latitude = wisataObj.getDouble("museum_latitude");
                            wisata.longitude = wisataObj.getDouble("museum_langitude");
                            wisata.gambar = wisataObj.getString("museum_photoPath");
                            wisata.video = wisataObj.getString("museum_video");
                            wisata.audio = wisataObj.getString("museum_audio");
                            wisata.kota = wisataObj.getString("city_name");
                            //wisata.jambuka = wisataObj.getString("jambuka");
                            //wisata.jamtutup = wisataObj.getString("jamtutup");
                            //wisata.viewer = wisataObj.getDouble("viewer");
                            wisata.deskripsi = wisataObj.getString("museum_description");
                            //wisata.rating = wisataObj.getDouble("rate");
                            //wisata.distance = getDistance(wisata);

//                            wisata.total = wisata.total + (wisata.viewer / 10);
//                            wisata.total = wisata.total + (wisata.rating * 10);
//                            wisata.total = wisata.total - (wisata.distance / 2);
//                            if(CGlobal.user.isUserLoggedIn()) {
//                                String[] kategori = wisataObj.getString("kategori").split(",");
//                                JSONArray dataJSONRecommendation = jsonData.getJSONArray("recommendation");
//                                JSONObject recommendationObj = dataJSONRecommendation.getJSONObject(0);
//                                for (int j = 0; j < kategori.length; j++) {
//                                    wisata.total = wisata.total + recommendationObj.getInt(kategori[j]);
//                                }
//                                JSONArray dataJSONSetting = jsonData.getJSONArray("setting");
//                                JSONObject settingObj = dataJSONSetting.getJSONObject(0);
//                                for (int j = 0; j < kategori.length; j++) {
//                                    if(settingObj.getBoolean(kategori[j])) wisata.total = wisata.total + 100;
//                                }
//                            }
                            //wisataValue = wisataValue + wisata.namawisata + " Value : " + new DecimalFormat("#.##").format(wisata.total) + "\n";

                            dataWisata.add(wisata);
                        }
                        Collections.sort(dataWisata, new Comparator<CWisata>() {
                            @Override
                            public int compare(CWisata cWisata, CWisata t1) {
                                return Double.valueOf(t1.total).compareTo(cWisata.total);
                            }
                        });
                        //Toast.makeText(ctx, wisataValue, Toast.LENGTH_LONG).show();
                        adapter = new CWisataAdapter(ctx,R.layout.list_wisata,dataWisata);
                        listViewWisata.setAdapter(adapter);
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

    public class updateRecommend extends AsyncTask<String,Void,String> {
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

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public float getDistance(CWisata wisata){
        double longitude=0, latitude=0;
        try{
            LocationManager lm;
            android.location.Location location;
            lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        catch (SecurityException e){e.printStackTrace();}

        android.location.Location me = new android.location.Location("");
        me.setLatitude(latitude);
        me.setLongitude(longitude);

        android.location.Location des = new android.location.Location("");
        des.setLatitude(wisata.latitude);
        des.setLongitude(wisata.longitude);

        return me.distanceTo(des)/1000;
    }
}
