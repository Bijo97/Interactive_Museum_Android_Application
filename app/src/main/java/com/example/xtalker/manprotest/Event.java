package com.example.xtalker.manprotest;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
public class Event extends Fragment {
    public View view;
    public Context ctx;
    public ListView listViewEvent;
    public ArrayAdapter<CEvent> adapter;
    public ArrayList<CEvent> dataEvent;

    public Event() {
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
        view =  inflater.inflate(R.layout.fragment_event, container, false);

        dataEvent = new ArrayList<CEvent>();
        dataEvent.clear();
        listViewEvent = (ListView)view.findViewById(R.id.listViewEvent);

        getReview task = new getReview();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getevent.php";
        task.execute(params);

        return view;
    }

    public class CEvent{
        public String title;
        public String description;
        public String location;
        public String startDate;
        public String endDate;
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
                        JSONArray dataJSON = jsonData.getJSONArray("event");
                        for(int i=0; i<dataJSON.length(); i++){
                            JSONObject eventObj = dataJSON.getJSONObject(i);
                            CEvent event = new CEvent();
                            event.title = eventObj.getString("title");
                            event.description = eventObj.getString("description");
                            event.location = eventObj.getString("location");
                            event.startDate = eventObj.getString("start_period");
                            event.endDate = eventObj.getString("end_period");
                            dataEvent.add(event);
                        }
                        adapter = new CEventAdapter(ctx, R.layout.item_event, dataEvent);
                        listViewEvent.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(ctx,"Download failed 3",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ctx,"Download failed",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}
