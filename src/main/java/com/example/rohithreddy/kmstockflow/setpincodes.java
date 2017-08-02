package com.example.rohithreddy.kmstockflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by rohithreddy on 25/07/17.
 */

public class setpincodes {
    private Cursor d;
    private SQLiteDatabase db;
    private ProgressDialog pDialog;
    Response response;String responseBody;
    Context mContext;String phone,pass,x,result1,error;
    JSONObject pcodes = new JSONObject();
    public setpincodes(Context context,String phone,String pass) {
        this.mContext = context;
        this.phone=phone;this.pass=pass;
        something();
    }
    public  void something() {
        db=mContext.openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS routelist(id VARCHAR," + "route VARCHAR," + "routenum VARCHAR);");
        d = db.rawQuery("SELECT * FROM routelist", null);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(mContext);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
//                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {

                OkHttpClient client = new OkHttpClient();
                JSONObject cred = new JSONObject();
                try {
                    cred.put("field1", phone);
                    cred.put("field2", pass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject data = new JSONObject();
               /*   try {
                  data.put("fullName", r1);
                    data.put("outletName", r2);
                    data.put("phone", r3);
                    data.put("dob", r4);
                    data.put("outletType", r5);
                    data.put("area", r6);
                    data.put("vicinity", r7);
                    data.put("pincodeID", r8a);
                    data.put("geoLng", longi);
                    data.put("geoLat", lati);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                JSONObject studentsObj = new JSONObject();
                try {
                    studentsObj.put("credentials", cred);
                   // studentsObj.put("data", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String jsonStr = studentsObj.toString();
                System.out.print(jsonStr);
                RequestBody formBody = new FormBody.Builder()
                        .add("request", jsonStr)
                        .build();
                Request request = new Request.Builder()
                       // .url(mContext.getResources().getString(R.string.url_text) + "/posForm")
                        .url("http://kwikmint.in/dashboard/index.php/api/getRoutes")
                        .post(formBody)
                        .build();
                try {

                    response = client.newCall(request).execute();
                    responseBody = response.body().string();
                    try {
                        System.out.println("output ..................." + responseBody);
                        JSONObject jsonObj = new JSONObject(responseBody);
                       result1 = jsonObj.getString("status");
                       if (result1.equals("success")) {
                           pcodes = jsonObj.getJSONObject("routes");
                           System.out.println("\npin------- ..................." + pcodes);
                       }
                        if (result1.equals("failed")) {
                            error = jsonObj.getString("errorCode");
                            System.out.println("\npincode..------- ..................." + pcodes);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    x = "internet";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                    pDialog.dismiss();  if (x == "nointernet")
                    Toast.makeText(mContext, "check your internet connection", Toast.LENGTH_LONG).show();
                else if (result1.equals("success")) {
                    db.execSQL("delete from routelist");
                    Iterator key = pcodes.keys();
                    while(key.hasNext())  {
                            JSONObject student =null;
                        try {
                            String keys = (String) key.next();
                            System.out.println(keys);
                            student = pcodes.getJSONObject(keys);
                            String routename = student.getString("POINT");
                            String id = student.getString("ROUTE_ID");
                             String route = student.getString("ROUTE");
                            System.out.println(routename);
                            db.execSQL("INSERT INTO routelist VALUES('" + id + "','" + route + "','" + routename + "');");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                    Toast.makeText(mContext, "sync successful", Toast.LENGTH_LONG).show();
                } else if (result1.equals("failed"))
                    Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(mContext, "server down try again later", Toast.LENGTH_LONG).show();
                    x = "nointernet";
                }
                result1="";error="";


            }


        }.execute();
    }
}
