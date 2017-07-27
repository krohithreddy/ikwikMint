package com.example.rohithreddy.kmstockflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.app.Activity.RESULT_OK;
public class MainActivity extends Fragment {
    public static final int REQUEST_CODE = 100;
    String qrvalue = "";
    String datetime = "Hello world!";
    double latitude, longitude;
    String longi, lati, phone, pass;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ProgressDialog pDialog;
    String x = "nointernet";
    String responseBody,  result1 = "", error = "", sk, phonenumber;
    Response response;
    File file;Integer errorvalue=0;
    EditText phonenum, stock,qrcode;
    UserSessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        getActivity().setTitle("     Stock  Update ");
        session = new UserSessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);
        verifyStoragePermissions(getActivity());
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "kmsf");
        isExternalStorageWritable();
        isExternalStorageReadable();
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

        if (file.mkdir()) {

        }//directory is created;


        final Button submit = (Button) view.findViewById(R.id.submit);
       // final Button scan = (Button) view.findViewById(R.id.scanbtn);
        phonenum = (EditText) view.findViewById(R.id.phone);
       // qrcode = (EditText) view.findViewById(R.id.qrcode);
        stock = (EditText) view.findViewById(R.id.stock);
        stock.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
      //  qrcode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

      /*  scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // qrcode.setText("");
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }

        });*/


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = phonenum.getText().toString();
                sk = stock.getText().toString();
            /*    qrvalue=qrcode.getText().toString();
                    if(!phonenumber.isEmpty()){
                        if (phonenum.getText().toString().trim().length() < 10) {
                            phonenum.setError("phone number should be 10 digits");
                            errorvalue=1;
                        }

                    }
                    if(!qrvalue.isEmpty()){
                        if(qrvalue.length()>8 || qrvalue.length()<8){
                            qrcode.setError("qrcode should be 8 digits");
                            errorvalue=2;
                        }

                    }*/


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if (latitude == 0.0) {
                        Toast.makeText(getContext(), "wait for location and try again",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (phonenum.getText().toString().trim().length() < 10) {
                        phonenum.setError("phone number should be 10 digits");
                    }
                   /* else if(qrvalue.isEmpty()&&phonenumber.isEmpty()){
                        //enter either phone or qrcode
                        Toast.makeText(getContext(), "enter either phone number or qrcode",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(errorvalue.equals(1) || errorvalue.equals(2)){
                    //do nothing
                    }
                    */else if (stock.getText().toString().trim().length() == 0) {
                        stock.setError("stock cant be empty ");
                    } else {
                        longi = String.valueOf(longitude);
                        lati = String.valueOf(latitude);
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
                                  //  data.put("qrcode",qrvalue);
                                    data.put("stock", sk);
                                    data.put("date", datetime);
                                    data.put("lng", longi);
                                    data.put("lat", lati);

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
                                        .url(getResources().getString(R.string.url_text) + "/stockInfo")
                                        .post(formBody)
                                        .build();
                                try {

                                    response = client.newCall(request).execute();
                                    responseBody = response.body().string();
                                    try {
                                        //  System.out.println("output ..................." + responseBody);
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
                                    Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                else if (result1.equals("success")) {
                                    phonenum.setText("");
                                    stock.setText("");
                                 //   qrcode.setText("");
                                    Toast.makeText(getContext(), "recored successful", Toast.LENGTH_LONG).show();
                                } else if (result1.equals("failed")) {
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                    if (error.equals("ADAPILC")) {
                                        session.logoutUser();
                                        Intent loginIntent = new Intent(getActivity(), LOGIN.class);
                                        getActivity().startActivity(loginIntent);
                                        //repeatTask.cancel();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "server down try again later", Toast.LENGTH_LONG).show();
                                    x = "nointernet";
                                }
                                result1 = "";
                                error = "";
                                qrvalue="";

                            }


                        }.execute();

                    }
                } else {
                    gps.showSettingsAlert();
                }
                new onsubmit(getActivity());
                errorvalue=0;

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                phonenum.post(new Runnable() {
                    @Override
                    public void run() {
                        qrcode.setText(barcode.displayValue);
                        qrvalue=qrcode.getText().toString();
                    }

                });

            }
        }
    }
}




