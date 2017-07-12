package com.example.rohithreddy.kmstockflow;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.rohithreddy.kmstockflow.playvideo.READ_BLOCK_SIZE;
import android.app.Dialog;

public class registeroutlet extends Fragment {
    String filename = "myfile", filename1 = "mydata1";
    Integer count = 0;
    String datetime = "Hello world!", lm = "location not found";
    double latitude, longitude;
    String longi, lati, phone, pass;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ProgressDialog pDialog;
    String x = "nointernet";
    String responseBody, responseBody1, result1 = "", error = "", result2 = "", error2 = "", sk, phonenumber;
    Response response, response1;
    File file;
    Integer pause = 1;
    EditText phonenum, stock;
    UserSessionManager session;
    private SQLiteDatabase db;
    boolean bool = true;
    private Cursor c;
    Timer repeatTask;
    EditText ro1, ro2, ro3, ro6;AppCompatAutoCompleteTextView ro8;
    TextView ro4a, ro4b, ro4c;
    String r1, r2, r3, r4,r4a,r4b,r4c, r5, r6, r7, r8,r8a,pinvalue;
    Spinner sp1, sp2,sp3;
    private int year, month, day;
    private Calendar calendar;
    int resID,erase=0;
    ArrayList<String>  formList,pin ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_registeroutlet, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("      Register Outlet");
        getpincodes pinco = new getpincodes(getActivity());
       //= new ArrayList<HashMap<String, String>>();
        formList=pinco.getformlist();

       // String pincodes = loadJSON();
      //  System.out.print(formList);
        pin = pinco.getPinlist();
        pin.add(0,"Get Pincode");
        formList.add(0,"0");
        session = new UserSessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);
        sp1 = (Spinner) view.findViewById(R.id.ro5);
        sp2 = (Spinner) view.findViewById(R.id.ro7);
        sp3 = (Spinner) view.findViewById(R.id.ro8a);
        ro8 =  view.findViewById(R.id.ro8);

        // Spinner click listener
        //  spinner.((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("outlet Type");
        categories.add("Tea Stall");
        categories.add("Pan Shop");
        categories.add("Retail Store");

        List<String> types = new ArrayList<String>();
        types.add("Main road Vicinity");
        types.add("Beside");
        types.add("Near");
        types.add("Far");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> pinadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pin);
        ArrayAdapter<String> stypeda = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stypeda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp3.setAdapter(pinadapter);
        ro8.setAdapter(pinadapter);
        // attaching data adapter to spinner
        sp1.setAdapter(dataAdapter);
        sp2.setAdapter(stypeda);
        final Button submit = (Button) view.findViewById(R.id.submit);
        final Button setdate = (Button) view.findViewById(R.id.setdate);
        ro1 = (EditText) view.findViewById(R.id.ro1);
        ro2 = (EditText) view.findViewById(R.id.ro2);
        ro3 = (EditText) view.findViewById(R.id.ro3);
        ro6 = (EditText) view.findViewById(R.id.ro6);
        ro4a = (TextView) view.findViewById(R.id.ro4a);
        ro4b = view.findViewById(R.id.ro4b);
        ro4c = view.findViewById(R.id.ro4c);
        ro3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                r5 = String.valueOf(parent.getItemAtPosition(position));
                System.out.print("\nrrrrrrr"+r5);
                if(r5=="Tea Stall")
                    r5="5";
                if(r5=="Pan Shop")
                    r5="6";
                if(r5=="Retail Store")
                    r5="7";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        ro8.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence p, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence p, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable p) {
                r8a=null;
            }
        });
        ro8.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                r8 = String.valueOf(parent.getItemAtPosition(position));
                System.out.print("\nselect");
                //sp3.setTop(position);
                sp3.setSelection(position);
                System.out.print("\nset");
                int x = pin.indexOf(r8);
                if (x>0)
                r8a = formList.get(x);
                erase=1;

            }


        });
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                pinvalue = String.valueOf(parent.getItemAtPosition(position));
                System.out.print("\npinvalue select");
                //if(!pinvalue.equals("Get Pincode"))
               // ro8.setText(pinvalue);
                int x = pin.indexOf(pinvalue);
                if(x>0)
                r8a = formList.get(x);
                if(erase==0)
                    ro8.setText("");
                erase=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                r7 = String.valueOf(parent.getItemAtPosition(position));
                if(r7=="Near")
                    r7="2";
                if(r7=="Far")
                    r7="3";
                if(r7=="Beside")
                    r7="1";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(999).show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r8a==null){
                    int x = pin.indexOf(ro8.getText().toString());
                    if(x>0)
                        r8a = formList.get(x);
                }
            /*(    if(pinvalue == "Get Pincode"){
                    int x = pin.indexOf(ro8.getText().toString());
                    if(x>0)
                        r8a = formList.get(x);
                }*/
                r1 = ro1.getText().toString();
                r2 = ro2.getText().toString();
                r3 = ro3.getText().toString();
                r4 = r4c + "-" + r4a + "-" + r4b;
                r6 = ro6.getText().toString();
                r8 = ro8.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    if (ro1.getText().toString().trim().length() == 0) {
                        ro1.setError("full name cant be empty ");
                    } else if (ro2.getText().toString().trim().length() == 0) {
                        ro2.setError("outlet name cant be empty ");
                    } else if (ro3.getText().toString().trim().length() < 10) {
                        ro3.setError("phone number should be 10 digits");
                    } else if (ro4a.getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), "set date of birth", Toast.LENGTH_LONG).show();
                    } else if (r5 == "outlet Type") {
                        Toast.makeText(getContext(), "select a outlet type", Toast.LENGTH_LONG).show();
                    } else if (ro6.getText().toString().trim().length() == 0) {
                        ro6.setError("Area cant be empty ");
                    } else if (r7 == "Main road Vicinity") {
                        Toast.makeText(getContext(), "select a Main Vicinity type", Toast.LENGTH_LONG).show();
                    } else if (pinvalue == "Get Pincode") {
                        Toast.makeText(getContext(), "select a pincode", Toast.LENGTH_LONG).show();
                    }  else if (r8a == null) {
                        Toast.makeText(getContext(), "select a pincode from the list", Toast.LENGTH_LONG).show();
                        ro8.setError("select a pincode from the list");
                    } else if (latitude == 0.0) {
                        Toast.makeText(getContext(), "wait for location and try again",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        longi = String.valueOf(longitude);
                        lati = String.valueOf(latitude);
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
                                }
                                JSONObject studentsObj = new JSONObject();
                                try {
                                    studentsObj.put("credentials", cred);
                                    studentsObj.put("data", data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String jsonStr = studentsObj.toString();
                                System.out.print(jsonStr);
                                RequestBody formBody = new FormBody.Builder()
                                        .add("request", jsonStr)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(getResources().getString(R.string.url_text)+"/posForm")
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
                                    Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                else if (result1.equals("success")) {
                                    // db.execSQL("DELETE FROM videodata WHERE phonen="+phone+" ");
                                    Fragment fragment = null;
                                    fragment = new registeroutlet();
                                    if (fragment != null) {
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_frame, fragment);
                                        ft.commit();
                                    }

                                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                    drawer.closeDrawer(GravityCompat.START);

                                    Toast.makeText(getContext(), "recored successful", Toast.LENGTH_LONG).show();
                                } else if (result1.equals("failed"))
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getContext(), "server down try again later", Toast.LENGTH_LONG).show();
                                    x = "nointernet";
                                }
                                result1="";error="";
                            }


                        }.execute();

                    }
                } else {
                    gps.showSettingsAlert();
                }
                 new onsubmit(getActivity());

            }
        });

    }
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(),
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    r4a="";r4b="";r4c="";
                    ro4a.setText(Integer.toString(arg2 + 1));r4a=Integer.toString(arg2 + 1);
                    ro4b.setText(Integer.toString(arg3));r4b=Integer.toString(arg3);
                    ro4c.setText(Integer.toString(arg1));r4c=Integer.toString(arg1);

                }
            };
}
