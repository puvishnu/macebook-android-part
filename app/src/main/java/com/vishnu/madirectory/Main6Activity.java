package com.vishnu.madirectory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import android.os.AsyncTask;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Main6Activity extends AppCompatActivity {
    private static String url_all = "http://172.18.114.203:5678/mobreq";
    private static final String TAG_NAME = "name";
    private static final String TAG_PH1 = "phonenumber";
    private static final String TAG_PH2 = "landnumber";
    private static final String TAG_MAIL = "email";
    private static final String TAG_DEP = "dep";
    private static final String TAG_DES = "stafftype";
    private static final String TAG_STATUS = "is_activate";
    private static final String TAG_FILENAME = "profilefield";
    public static SqlDatabase sql;
    SharedPreferences sh;
    int load=0;
    SimpleDateFormat df;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        sql = new SqlDatabase(this);
        sh = getSharedPreferences("macebook", 0);
        date=Calendar.getInstance().getTime();
        df=new SimpleDateFormat("dd-MM-yyyy");
        try {
            new GetOrFetchData().execute();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private class GetOrFetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            if (isNetworkAvailable()&& (!(df.format(date).equalsIgnoreCase(sh.getString("date", "1"))))) {
                load = 1;
                sql.dropTab();
                sql.createTab();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url_all,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String jsonResposnce) {
                                try {
                                    Log.d("Ko","uhuu");
                                    JSONArray jarray = new JSONArray(jsonResposnce);
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject c = jarray.getJSONObject(i);
                                        int id=(c.getInt("user_id"));
                                        String name = c.getString(TAG_NAME);
                                        String ph1 = c.getString(TAG_PH1);
                                        String ph2 = c.getString(TAG_PH2);
                                        String mail = c.getString(TAG_MAIL);
                                        String dep = c.getString(TAG_DEP);
                                        String des=c.getString(TAG_DES);
                                        String status = String.valueOf(c.getInt(TAG_STATUS));
                                        String filename = c.getString(TAG_FILENAME);
                                        sql.insertData(id, name.trim(), ph1.trim(), ph2.trim(), mail.trim(), dep.trim(),des.trim(),status,filename.trim());
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                                String UPDATE="        Last Data Refreshed On "+date.toString()+"";
                                SharedPreferences.Editor edit = sh.edit();
                                edit.putInt("check", 5);
                                edit.putString("date",df.format(date));
                                edit.putString("notice",UPDATE);
                                edit.commit();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                );
                int socketTimeOut = 50000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                stringRequest.setRetryPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(stringRequest);

            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("load", load);
            startActivity(intent);
            finish();
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
