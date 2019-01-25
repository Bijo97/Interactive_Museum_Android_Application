package com.example.xtalker.manprotest;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    public Context ctx;
    public View view;
    public UserSessionManager user;

    public Login() {
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
        view = inflater.inflate(R.layout.fragment_login, container, false);

        Button login = (Button)view.findViewById(R.id.signin);
        final EditText edEmail = (EditText)view.findViewById(R.id.edEmail);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email = edEmail.getText().toString();
            getDataTask JSONTask = new getDataTask();
            JSONTask.execute(CGlobal.urlServer + "getuser.php?username="+email);
            }
        });
        TextView sign_up = (TextView)view.findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content_side, new Signup()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    public class getDataTask extends AsyncTask<String,Void,String>{
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
                    EditText edPw = (EditText)view.findViewById(R.id.edPw);
                    if(!jsonData.getJSONArray("user").isNull(0)){
                        JSONArray dataUser = jsonData.getJSONArray("user");
                        JSONObject userObj = dataUser.getJSONObject(0);
                        if(userObj.getString("visitorPassword").equals(edPw.getText().toString())){
                            Toast.makeText(ctx,"Login Successful",Toast.LENGTH_SHORT).show();
                            CGlobal.user.createUserLoginSession(userObj.getString("visitor_id"), userObj.getString("visitorName"), userObj.getString("visitorUsername"));
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            SideActivity.setMenuItem();
                        }else{
                            Toast.makeText(ctx,"Password Salah",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ctx,"Username tidak terdaftar",Toast.LENGTH_SHORT).show();
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
