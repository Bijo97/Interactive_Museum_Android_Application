package com.example.xtalker.manprotest;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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


public class Signup extends Fragment {
    public View view;
    public Context ctx;

    public Signup() {
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
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        Button signup = (Button)view.findViewById(R.id.sign_up);
        final EditText email = (EditText)view.findViewById(R.id.edEmail);
        final EditText pw = (EditText)view.findViewById(R.id.edPw);
        final EditText rePw = (EditText)view.findViewById(R.id.edRePw);
        final EditText nama = (EditText)view.findViewById(R.id.edNama);
        final EditText umur = (EditText)view.findViewById(R.id.edUmur);
        final RadioButton laki = (RadioButton)view.findViewById(R.id.radioButton);
        final RadioButton perempuan = (RadioButton)view.findViewById(R.id.radioButton2);

        laki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perempuan.setChecked(false);
            }
        });

        perempuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laki.setChecked(false);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataTask JSONTask = new getDataTask();
                if (!email.getText().toString().trim().equals("")) {
                        if (!pw.getText().toString().trim().equals("")) {
                            if (!nama.getText().toString().trim().equals("")) {
                                if (pw.getText().toString().equals(rePw.getText().toString())) {
                                    String genders = "";
                                    if (laki.isChecked()){
                                        genders = "Laki-laki";
                                    } else if (perempuan.isChecked()){
                                        genders = "Perempuan";
                                    }
                                    String name = nama.getText().toString();
                                    name = name.replaceAll(" ", "%20");
                                    JSONTask.execute(CGlobal.urlServer + "setuser.php?username=" + email.getText().toString() + "&password=" + pw.getText().toString() + "&nama=" + name + "&umur=" + umur.getText().toString() + "&gender=" + genders);
                                    Log.d("Biljo", CGlobal.urlServer + "setuser.php?username=" + email.getText().toString() + "&password=" + pw.getText().toString() + "&nama=" + name + "&umur=" + umur.getText().toString() + "&gender=" + genders);
                                } else {
                                    Toast.makeText(ctx, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                nama.setError("Nama Harus Diisi");
                            }
                        } else {
                            pw.setError("Password Harus Diisi");
                        }
                } else {
                    email.setError("Username Harus Diisi");
                }
            }
        });

        return view;
    }

    public class getDataTask extends AsyncTask<String,Void,String> {
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
            super.onPostExecute(s);
            if(s!=null) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonData = new JSONObject(s);
                    if(jsonData.getString("status").equals("ok")){
                        Toast.makeText(ctx,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new Login()).addToBackStack(null).commit();
                    }else{
                        Toast.makeText(ctx,"Username sudah terdaftar",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(ctx,"Download Failed 2",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ctx,"Download Failed 3",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

