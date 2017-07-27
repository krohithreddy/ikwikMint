package com.example.rohithreddy.kmstockflow;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Mapping extends Fragment {
    EditText phonenum,outletname,username;
    String user ,phonenumber,outlet,datetime,longi=null,lati=null;
    double longitude,latitude;
    private SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mapping, container, false);

        db=getActivity().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mapusers(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR," +
                "outletname VARCHAR," +
                "phonen VARCHAR," +
                "date VARCHAR," +
                "lat VARCHAR,lng VARCHAR,systemid VARCHAR);");

        final Button submit = (Button) view.findViewById(R.id.map);
        phonenum = (EditText) view.findViewById(R.id.phonenumber);
        username = (EditText) view.findViewById(R.id.outletname);
        outletname = (EditText) view.findViewById(R.id.fullname);
        phonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = phonenum.getText().toString();
                user = username.getText().toString();
                outlet = outletname.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    gps.showSettingsAlert();
                }
                if (latitude == 0.0) {
                        Toast.makeText(getContext(), "mapping wihtout location",
                                Toast.LENGTH_SHORT).show();
                    }

                    if (username.getText().toString().trim().length() == 0) {
                        username.setError("username cant be empty ");
                    }
                    else if (outletname.getText().toString().trim().length() == 0) {
                        outletname.setError("outletname cant be empty ");
                    }
                    else if (phonenum.getText().toString().trim().length() < 10) {
                        phonenum.setError("phone number should be 10 digits");
                    }
                    else {
                        longi = String.valueOf(longitude);
                        lati = String.valueOf(latitude);
                        db.execSQL("INSERT INTO mapusers( username ,outletname ,phonen ,date ,lat ,lng ,systemid )"+" VALUES('" + user + "','" + outlet + "','" + phonenumber + "','" + datetime + "','" + lati + "','" + longi + "','" + null + "');");
                        phonenum.setText("");
                        outletname.setText("");
                        username.setText("");
                    }





            }
        });
        return view;

    }
}
