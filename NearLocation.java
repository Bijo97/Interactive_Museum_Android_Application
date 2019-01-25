package com.example.xtalker.manprotest;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NearLocation extends Fragment {
    public View view;
    public Context ctx;
    public ListView listViewWisata;
    public CWisataAdapter adapter;
    public ArrayList<CWisata> dataWisata;

    public NearLocation() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        ctx = context;
        super.onAttach(context);
    }

    public void IsiDataDummy(){
        CGlobal.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nearme, container, false);
        dataWisata = new ArrayList<CWisata>();
        dataWisata.clear();
        listViewWisata = (ListView) view.findViewById(R.id.listViewWisata);

        //IsiDataDummy();
        adapter = new CWisataAdapter(ctx,R.layout.list_wisata,dataWisata);
        listViewWisata.setAdapter(adapter);
        LoadData();
        listViewWisata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Fragment fragment = null;
            fragment = new DetailWisata();
            DetailWisata.setData(dataWisata.get(i));
            if(fragment != null){
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_side, fragment).commit();
            }
            }
        });
        return view;
    }

    public void LoadData(){
        getDataTask task = new getDataTask();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getwisata.php";
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
                        dataWisata.clear();
                        for(int i=0; i<dataJSONWisata.length(); i++){
                            JSONObject wisataObj = dataJSONWisata.getJSONObject(i);
                            CWisata wisata = new CWisata();
                            wisata.idwisata = wisataObj.getInt("idwisata");
                            wisata.namawisata = wisataObj.getString("namawisata");
                            wisata.alamat = wisataObj.getString("alamat");
                            wisata.telpon = wisataObj.getString("telpon");
                            wisata.latitude = wisataObj.getDouble("latitude");
                            wisata.longitude = wisataObj.getDouble("longitude");
                            wisata.gambar = wisataObj.getString("gambar");
                            wisata.gambar2 = wisataObj.getString("gambar2");
                            wisata.gambar3 = wisataObj.getString("gambar3");
                            wisata.kota = wisataObj.getString("kota");
                            wisata.jambuka = wisataObj.getString("jambuka");
                            wisata.jamtutup = wisataObj.getString("jamtutup");
                            wisata.keterangan = wisataObj.getString("keterangan");

                            dataWisata.add(wisata);
                        }
                        adapter = new CWisataAdapter(ctx,R.layout.item_nearme,dataWisata);
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
            //super.onPostExecute(s);
        }
    }
}