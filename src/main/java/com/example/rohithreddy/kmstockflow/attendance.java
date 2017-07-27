package com.example.rohithreddy.kmstockflow;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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


public class attendance extends Fragment {
    public static Button tb1;
    String Status;
    String datetime = "Hello world!";
    double latitude, longitude;
    String longi, lati, phone, pass;
    private ProgressDialog pDialog;
    String x = "nointernet";
    String responseBody, result1 = "", error = "", result2 = "";
    Response response;
    File file;
    public static int verify = 0;
    UserSessionManager session;
    public static boolean bool = true;
    TextView duty;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance, container, false);
        getActivity().setTitle("     Attendance");

       // System.out.print(verify+"\n\n\n");
        session = new UserSessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);
        tb1 = view.findViewById(R.id.tb1);
        duty = view.findViewById(R.id.duty);
        if (session.isattendancestarted()) {
            //System.out.print("\nsession + check");
            tb1.setText("OFF");
            duty.setText("ON DUTY");
            bool = false;
        } else {

            //System.out.print("\nsession - check");
           // System.out.print(session.isattendancestarted());
            tb1.setText("ON");
            duty.setText("OFF DUTY");
            bool = true;
        }
        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if (latitude == 0.0) {
                        Toast.makeText(getContext(), "wait for location and try again",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        longi = String.valueOf(longitude);
                        lati = String.valueOf(latitude);
                        if (bool) {
                          //  System.out.print("\nhrerrrcbfvffc");
                            Status = "IN";
                            session.startattendance();
                            verify=1;
                           // System.out.print("i am verifying "+verify+"  kkk");

                        } else {
                           // System.out.print("\nfghjgfdszdfghjkhgcfxdcfgvh");
                            Status = "OUT";
                            verify=0;
                        }
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                // Showing progress dialog\
                                pDialog = new ProgressDialog(getActivity());
                                pDialog.setMessage("Please wait...");
                                pDialog.setCancelable(false);
                                pDialog.show();

                            }

                            @Override
                            protected Void doInBackground(Void... arg0) {
                                result1 = "";
                                result2 = "";
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
                                    data.put("phone", phone);
                                    data.put("status", Status);
                                    data.put("time", datetime);
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
                             //   System.out.print(jsonStr);
                                RequestBody formBody = new FormBody.Builder()
                                        .add("request", jsonStr)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(getResources().getString(R.string.url_text)+"/attendance")
                                        .post(formBody)
                                        .build();

                                try {
                                   // System.out.print("2222222222");
                                    response = client.newCall(request).execute();
                                    responseBody = response.body().string();
                                    try {
                                        // System.out.println("output ..................." + responseBody);
                                        JSONObject jsonObj = new JSONObject(responseBody);
                                        if (Status.equals("IN")) {
                                            result1 = jsonObj.getString("status");
                                        } else {
                                            result2 = jsonObj.getString("status");
                                        }
                                        if (result1.equals("failed"))
                                            error = jsonObj.getString("errorCode");
                                        if (result2.equals("failed"))
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

                                if (x == "nointernet") {
                                    Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                } else if (result2.equals("success")) {
                                    // db.execSQL("DELETE FROM lasttime1 WHERE phone=" + phone + " ");
                                    //  repeatTask.cancel();
                                    //System.out.print("\n hereeeeeeee");
                                    tb1.setText("ON");
                                    duty.setText("OFF DUTY");
                                    session.stopattendance();
                                    verify=0;
                                    bool = true;
                                    Toast.makeText(getContext(), "recored successfull", Toast.LENGTH_LONG).show();
                                } else if (result1.equals("success")) {

                                    // db.execSQL("DELETE FROM videodata WHERE phonen="+phone+" ");

                                    tb1.setText("OFF");
                                    duty.setText("ON DUTY");
                                    //System.out.print("starting session");
                                    bool = false;
                                    Toast.makeText(getContext(), "recored successfull", Toast.LENGTH_LONG).show();
                                    // new startrecording().execute();
                                } else if (result1.equals("failed")) {
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                    session.stopattendance();
                                    verify=0;
                                }
                                else {
                                    session.stopattendance();
                                    verify=0;
                                    Toast.makeText(getContext(), "server down try again later", Toast.LENGTH_LONG).show();
                                    x = "nointernet";
                                }
                                result1="";error="";result2="";

                            }


                        }.execute();
                    }
                } else {
                    gps.showSettingsAlert();

                }
                new onsubmit(getActivity());

            }
        });
        return view;
    }

    }
