package com.example.rohithreddy.kmstockflow;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by rohithreddy on 07/07/17.
 */

public class onsubmit {
    String longi,lati,phone,pass;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ProgressDialog pDialog;
    String x="nointernet"; String responseBody,responseBody1, result1="",error="", result2="",error2="" ,sk,phonenumber;
    Response response,response1;
    File file;Integer pause =1;
    EditText phonenum,stock;
    UserSessionManager session;
    private SQLiteDatabase db;boolean bool=true;
    private Cursor c; Timer repeatTask;
    private final Context mContext;


    public onsubmit(Context context) {
        this.mContext = context;
        submitreport();
    }
    
   void submitreport(){
       session = new UserSessionManager(mContext);
       HashMap<String, String> user =  session.getUserDetails();
       phone = user.get(UserSessionManager.KEY_NAME);
       pass = user.get(UserSessionManager.KEY_PASS);
       db=mContext.openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
       db.execSQL("CREATE TABLE IF NOT EXISTS mylocation(phonen VARCHAR,date VARCHAR,lat VARCHAR,lng VARCHAR);");
       c = db.rawQuery("SELECT * FROM mylocation WHERE phonen="+phone+" ", null);
       final JSONArray resultSet = new JSONArray();
       if (!(c.moveToFirst()) || c.getCount() == 0){

       }
           //Toast.makeText(mContext, "nothing to submit", Toast.LENGTH_LONG).show();
       else {
           c.moveToFirst();
           while (c.isAfterLast() == false) {
               int totalColumn = c.getColumnCount();
               JSONObject rowObject = new JSONObject();
               for (int i = 0; i < totalColumn; i++) {
                   if (c.getColumnName(i) != null) {
                       try {
                           if (c.getString(i) != null) {
                               rowObject.put(c.getColumnName(i), c.getString(i));
                           } else {
                               rowObject.put(c.getColumnName(i), "");
                           }
                       } catch (Exception e) {

                       }
                   }
               }
               resultSet.put(rowObject);
               c.moveToNext();
           }
           c.close();

           new AsyncTask<Void, Void, Void>() {
               @Override
               protected void onPreExecute() {
                   super.onPreExecute();
                   // Showing progress dialog
                   pDialog = new ProgressDialog(mContext);
                   pDialog.setMessage("Please wait...");
                   pDialog.setCancelable(false);
                   pDialog.show();

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
                   JSONObject studentsObj = new JSONObject();
                   try {
                        studentsObj.put("credentials", cred);
                       studentsObj.put("data", resultSet);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   String jsonStr = studentsObj.toString();
                   System.out.print(jsonStr);
                   System.out.print("mydataaaaaaaaaaaaa");
                   RequestBody formBody = new FormBody.Builder()
                           .add("request", jsonStr)
                           .build();
                   Request request = new Request.Builder()
                           .url(mContext.getResources().getString(R.string.url_text)+"/bgLocation")
                           .post(formBody)
                           .build();
                   try {

                       response = client.newCall(request).execute();
                       responseBody = response.body().string();
                       try {
                           System.out.println("output ..................." + responseBody);
                           JSONObject jsonObj = new JSONObject(responseBody);
                           result1 = jsonObj.getString("status");
                           if (result1.equals("failed"))
                               error = jsonObj.getString("errorCode");
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
                       pDialog.dismiss();
                   if (x == "nointernet")
                       Toast.makeText(mContext, "check your internet connection", Toast.LENGTH_LONG).show();
                   else if (result1.equals("success")) {
                       db.execSQL("DELETE FROM mylocation WHERE phonen=" + phone + " ");
                       Toast.makeText(mContext, "recored successful", Toast.LENGTH_LONG).show();
                   } else if (result1.equals("failed"))
                   {
                       Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
                       if(error.equals("ADAPILC")){
                           session.logoutUser();
                           Intent loginIntent = new Intent(mContext, LOGIN.class);
                           mContext.startActivity(loginIntent);
                           //repeatTask.cancel();
                       }
                   }
                   else {
                       Toast.makeText(mContext, "server down try again later", Toast.LENGTH_LONG).show();
                       x = "nointernet";
                   }

               }
           }.execute();
       }


   }
    
    
    
}
