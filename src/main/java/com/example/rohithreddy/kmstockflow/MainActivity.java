package com.example.rohithreddy.kmstockflow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.rohithreddy.kmstockflow.playvideo.READ_BLOCK_SIZE;

public class MainActivity extends Fragment {
//    AssetManager assetManager = getContext().getResources().getAssets();
    String filename = "myfile",filename1="mydata1";
    Integer count=0;
    String datetime = "Hello world!",lm="location not found";
    double latitude,longitude;
    String longi,lati,phone,pass;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
         View view =inflater.inflate(R.layout.activity_main, container, false);
        getActivity().setTitle("     Stock  Update ");
        session = new UserSessionManager(getActivity());
        HashMap<String, String> user =  session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);
        verifyStoragePermissions(getActivity());
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "kmsf");
        isExternalStorageWritable();
        isExternalStorageReadable();
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

         if(file.mkdir()){

         }//directory is created;




        final Button submit = (Button)view.findViewById(R.id.submit);
        phonenum = (EditText) view.findViewById(R.id.phone);
        stock = (EditText) view.findViewById(R.id.stock);
        stock.setFilters( new InputFilter[] { new InputFilter.LengthFilter(10)});
        phonenum.setFilters( new InputFilter[] { new InputFilter.LengthFilter(10)});
        db= getActivity().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mylocation(phonen VARCHAR,date VARCHAR,lat VARCHAR,lng VARCHAR);");
        //phonenum.setText(phone);

        db.execSQL("DELETE FROM mylocation WHERE phonen=" + phone + " ");

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                phonenumber = phonenum.getText().toString();
                sk = stock.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if(gps.canGetLocation()){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if(latitude == 0.0 ){
                        Toast.makeText(getContext(), "wait for location and try again",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(phonenum.getText().toString().trim().length() < 10){
                        phonenum.setError("phone number should be 10 digits");
                    }
                    else if(stock.getText().toString().trim().length()==0){
                        stock.setError("stock cant be empty ");
                    }

                    else {
                        longi= String.valueOf(longitude) ;
                        lati =String.valueOf(latitude);
                        db.execSQL("CREATE TABLE IF NOT EXISTS mylocation(phonen VARCHAR,date VARCHAR,lat VARCHAR,lng VARCHAR);");
                        c = db.rawQuery("SELECT * FROM mylocation WHERE phonen="+phone+" ", null);
                        final JSONArray resultSet = new JSONArray();
                       new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                // Showing progress dialog
                                pDialog = new ProgressDialog(getActivity());
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
                                JSONObject data = new JSONObject();
                                try {
                                    data.put("phone", phonenumber);
                                    data.put("stock", sk);
                                    data.put("date", datetime);
                                    data.put("lat", longi);
                                    data.put("lng", lati);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JSONObject studentsObj = new JSONObject();
                                try {
                                    studentsObj.put("credentials", cred);
                                    studentsObj.put("data", data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String jsonStr = studentsObj.toString();
                                RequestBody formBody = new FormBody.Builder()
                                        .add("request", jsonStr)
                                        .build();
                                Request request = new Request.Builder()
                                        .url("http://172.16.1.12:8888/kwikmint/index.php/api/stock_flow/stockInfo")
                                        .post(formBody)
                                        .build();
                                OkHttpClient client1 = new OkHttpClient();
                                JSONObject studentsObj1 = new JSONObject();
                                try {
                                    // studentsObj.put("credentials", cred);
                                    studentsObj1.put("data", resultSet);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String jsonStr1 = studentsObj1.toString();
                                System.out.print(jsonStr1);
                                System.out.print("mydataaaaaaaaaaaaa");
                                RequestBody formBody1 = new FormBody.Builder()
                                        .add("request", jsonStr1)
                                        .build();
                                Request request1 = new Request.Builder()
                                        .url("http://172.16.1.12:8888/kwikmint/index.php/api/stock_flow/bgLocation")
                                        .post(formBody1)
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
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    x = "internet";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                               if(count.equals(1)){
                                   result2="empty";
                               }
                               else {
                                   try {

                                       response1 = client1.newCall(request1).execute();
                                       responseBody1 = response1.body().string();
                                       try {
                                           System.out.println("output ..................." + responseBody1);
                                           JSONObject jsonObj = new JSONObject(responseBody1);
                                           result2 = jsonObj.getString("status");
                                           if (result2.equals("failed"))
                                               error2 = jsonObj.getString("errorCode");
                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }
                                       //x = "internet";
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                if (x == "nointernet")
                                    Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                else if (result1.equals("success")) {
                                    // db.execSQL("DELETE FROM videodata WHERE phonen="+phone+" ");
                                    FileOutputStream outputStream;
                                    try {
                                        outputStream = getActivity().openFileOutput(filename, Context.MODE_APPEND);
                                        String newline = "\n";
                                        longi= String.valueOf(longitude) ;
                                        lati =String.valueOf(latitude);
                                        String stockv = "  "+sk + "  ";
                                        String l1 = "  "+longitude + "  ";
                                        String l2 = "  "+latitude + "  ";
                                        outputStream.write(newline.getBytes());
                                        outputStream.write(phonenumber.getBytes());
                                        outputStream.write(stockv.getBytes());
                                        outputStream.write(datetime.getBytes());
                                        outputStream.write(l1.getBytes());
                                        outputStream.write(l2.getBytes());
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        FileInputStream fileIn=getActivity().openFileInput("myfile");
                                        InputStreamReader InputRead= new InputStreamReader(fileIn);
                                        char[] inputBuffer= new char[READ_BLOCK_SIZE];
                                        String s="";
                                        int charRead;
                                        while ((charRead=InputRead.read(inputBuffer))>0) {
                                            String readstring=String.copyValueOf(inputBuffer,0,charRead);
                                            s +=readstring;
                                        }
                                        InputRead.close();
                                        File filepath = new File(file, "output" + ".csv");  // file path to save
                                        FileWriter writer = new FileWriter(filepath);
                                        writer.append(s.toString());
                                        writer.flush();
                                        writer.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    phonenum.setText("");
                                    stock.setText("");
                                    Toast.makeText(getContext(), "recored successful", Toast.LENGTH_LONG).show();
                                }
                                else if (result1.equals("failed"))
                                    Toast.makeText(getContext(),error , Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getContext(), "server down try again later", Toast.LENGTH_LONG).show();
                                    x = "nointernet";
                                }
                                if (result2.equals("empty"))
                                    Toast.makeText(getContext(), "its empty", Toast.LENGTH_LONG).show();
                                else if (result2.equals("success")) {
                                    db.execSQL("DELETE FROM mylocation WHERE phonen=" + phone + " ");
                                    Toast.makeText(getContext(), "recored successful1", Toast.LENGTH_LONG).show();
                                } else if (result2.equals("failed"))
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getContext(), "server down try again later", Toast.LENGTH_LONG).show();
                                    x = "nointernet";
                                }

                            }


                       }.execute();

                    }
                }else{
                    gps.showSettingsAlert();
                }
                new onsubmit(getActivity());

            }
        });
        return view;
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        System.out.println("verifing");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            System.out.println("verified");
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }*/




    }




