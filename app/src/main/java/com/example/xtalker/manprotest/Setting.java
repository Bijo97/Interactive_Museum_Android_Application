package com.example.xtalker.manprotest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

/**
 * Created by Ferry on 6/10/2017.
 */

public class Setting extends Fragment {
    public View view;
    public Context ctx;

    public Setting() {
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
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        if(CGlobal.user.isUserLoggedIn()) {
            getSetting task = new getSetting();
            String[] params = new String[1];
            params[0] = CGlobal.urlServer + "getsetting.php?id_user=" + CGlobal.user.pref.getString(CGlobal.user.KEY_ID, "");
            task.execute(params);

            final Switch sejarah = (Switch)view.findViewById(R.id.sejarah);
            final Switch adventure = (Switch)view.findViewById(R.id.adventure);
            final Switch alam = (Switch)view.findViewById(R.id.alam);
            final Switch bahari = (Switch)view.findViewById(R.id.bahari);
            final Switch seni = (Switch)view.findViewById(R.id.seni);
            final Switch budaya = (Switch)view.findViewById(R.id.budaya);
            final Switch religi = (Switch)view.findViewById(R.id.religi);
            final Switch kuliner = (Switch)view.findViewById(R.id.kuliner);
            final Switch flora_fauna = (Switch)view.findViewById(R.id.flora_fauna);
            final Switch wahana = (Switch)view.findViewById(R.id.wahana);

            Button save = (Button)view.findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSetting task = new setSetting();

                    String boolSejarah = "false";
                    String boolAdventure = "false";
                    String boolAlam = "false";
                    String boolBahari = "false";
                    String boolSeni = "false";
                    String boolBudaya = "false";
                    String boolReligi = "false";
                    String boolKuliner = "false";
                    String boolFlora_fauna = "false";
                    String boolWahana = "false";

                    if(sejarah.isChecked()) boolSejarah = "true";
                    if(adventure.isChecked()) boolAdventure = "true";
                    if(alam.isChecked()) boolAlam = "true";
                    if(bahari.isChecked()) boolBahari = "true";
                    if(seni.isChecked()) boolSeni = "true";
                    if(budaya.isChecked()) boolBudaya = "true";
                    if(religi.isChecked()) boolReligi = "true";
                    if(kuliner.isChecked()) boolKuliner = "true";
                    if(flora_fauna.isChecked()) boolFlora_fauna = "true";
                    if(wahana.isChecked()) boolWahana = "true";

                    String[] params = new String[1];
                    params[0] = CGlobal.urlServer + "setsetting.php?id_user=" + CGlobal.user.pref.getString(CGlobal.user.KEY_ID, "")
                    + "&sejarah=" + boolSejarah
                    + "&adventure=" + boolAdventure
                    + "&alam=" + boolAlam
                    + "&bahari=" + boolBahari
                    + "&seni=" + boolSeni
                    + "&budaya=" + boolBudaya
                    + "&religi=" + boolReligi
                    + "&kuliner=" + boolKuliner
                    + "&flora_fauna=" + boolFlora_fauna
                    + "&wahana=" + boolWahana
                    ;

                    task.execute(params);
                }
            });
        }

        return view;
    }

    public class getSetting extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
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
                    if (jsonData.getString("status").equals("ok")) {
                        JSONArray dataJSONSetting = jsonData.getJSONArray("setting");
                        JSONObject settingObj = dataJSONSetting.getJSONObject(0);
                        Switch sejarah = (Switch)view.findViewById(R.id.sejarah);
                        Switch adventure = (Switch)view.findViewById(R.id.adventure);
                        Switch alam = (Switch)view.findViewById(R.id.alam);
                        Switch bahari = (Switch)view.findViewById(R.id.bahari);
                        Switch seni = (Switch)view.findViewById(R.id.seni);
                        Switch budaya = (Switch)view.findViewById(R.id.budaya);
                        Switch religi = (Switch)view.findViewById(R.id.religi);
                        Switch kuliner = (Switch)view.findViewById(R.id.kuliner);
                        Switch flora_fauna = (Switch)view.findViewById(R.id.flora_fauna);
                        Switch wahana = (Switch)view.findViewById(R.id.wahana);
                        sejarah.setChecked(settingObj.getBoolean("sejarah"));
                        adventure.setChecked(settingObj.getBoolean("adventure"));
                        alam.setChecked(settingObj.getBoolean("alam"));
                        bahari.setChecked(settingObj.getBoolean("bahari"));
                        seni.setChecked(settingObj.getBoolean("seni"));
                        budaya.setChecked(settingObj.getBoolean("budaya"));
                        religi.setChecked(settingObj.getBoolean("religi"));
                        kuliner.setChecked(settingObj.getBoolean("kuliner"));
                        flora_fauna.setChecked(settingObj.getBoolean("flora_fauna"));
                        wahana.setChecked(settingObj.getBoolean("wahana"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download Failed",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ctx,"Result is NULL",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class setSetting extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
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
                    if (jsonData.getString("status").equals("ok")) {
                        Toast.makeText(ctx, "Preference Saved", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download Failed",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ctx,"Result is NULL",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
