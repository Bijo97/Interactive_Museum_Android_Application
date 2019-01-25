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
import android.widget.AdapterView;
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
public class News extends Fragment {
    public View view;
    public Context ctx;
    public ListView listViewNews;
    public CNewsAdapter adapter;
    public ArrayList<CNews> dataNews;

    public News() {
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
        view = inflater.inflate(R.layout.fragment_news, container, false);

        dataNews = new ArrayList<CNews>();
        dataNews.clear();
        listViewNews = (ListView)view.findViewById(R.id.listViewNews);
        adapter = new CNewsAdapter(ctx, R.layout.item_news, dataNews);

        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            getFragmentManager().beginTransaction().replace(R.id.content_side, new DetailNews()).addToBackStack(null).commit();
            DetailNews.setData(dataNews.get(i));
            }
        });

        LoadData();

        return view;
    }

    public void LoadData(){
        getDataTask task = new getDataTask();
        String [] params = new String[1];
        params[0] = CGlobal.urlServer + "getnews.php";
        task.execute(params);
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
                        JSONArray dataJSON = jsonData.getJSONArray("news");
                        for(int i=0; i<dataJSON.length(); i++){
                            JSONObject newsObj = dataJSON.getJSONObject(i);
                            CNews news = new CNews();
                            news.idNews = newsObj.getInt("id_news");
                            news.title = newsObj.getString("title");
                            news.content = newsObj.getString("content");
                            news.image = newsObj.getString("image");
                            dataNews.add(news);
                        }
                        listViewNews.setAdapter(adapter);
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
}
