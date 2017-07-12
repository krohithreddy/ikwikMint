package com.example.rohithreddy.kmstockflow;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.rohithreddy.kmstockflow.playvideo.READ_BLOCK_SIZE;

public class rmain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    attendance at;
    public static int navItemIndex = 0;
    private Cursor c;
    private View navHeader;
    TextView name,fname;private ProgressDialog pDialog;
    String x="nointernet"; String responseBody,responseBody1, result1="",error="", result2="",error2="" ,sk,phonenumber;
    Response response,response1;
    String fullname = "myfile",filename1="mydata1";
    String datetime = "Hello world!",lm="location not found";
    double latitude,longitude;Integer pause =1;
    private SQLiteDatabase db;boolean bool=true;UserSessionManager session;
    Timer repeatTask;    String longi,lati,phone;  File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        name = (TextView) navHeader.findViewById(R.id.myid);
        fname = (TextView) navHeader.findViewById(R.id.myid1);
       // name =  (TextView) findViewById(R.id.myid);


        session = new UserSessionManager(rmain.this);
        HashMap<String, String> user =  session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        fullname = user.get(UserSessionManager.KEY_FULLNAME);
       // pass = user.get(UserSessionManager.KEY_PASS);
      //  name.getText().toString();
        name.setText(phone);
        fname.setText(fullname);
       // System.out.print(name);
        System.out.print("this is happenning\n");
        System.out.print(phone);
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "kmsf");
        db=openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mylocation(phonen VARCHAR,date VARCHAR,lat VARCHAR,lng VARCHAR);");
        Worker();
        displaySelectedScreen(R.id.nav_menu1);
        //name.setText(phone);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(navItemIndex!=0){
            displaySelectedScreen(R.id.nav_menu1);
        }
        else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (session.isattendancestarted()) {
                at.bool=false;
               at.tb1.performClick();
            }
            session.logoutUser();
            Intent loginIntent = new Intent(rmain.this, LOGIN.class);
            rmain.this.startActivity(loginIntent);
            //repeatTask.cancel();
            return true;
        }
       else if (id == R.id.play) {
            Intent myIntent = new Intent(rmain.this, playvideo.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //name =  (TextView) findViewById(R.id.myid);
        //name.setText(phone);
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
              navItemIndex = 0;
                fragment = new mainl();
                break;
            case R.id.nav_menu2:
                navItemIndex = 1;
                fragment = new attendance();
                break;
            case R.id.nav_menu3:
                navItemIndex = 2;
                fragment = new MainActivity();
                break;
            case R.id.nav_menu4:
                navItemIndex = 3;
                fragment = new registeroutlet();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void Worker (){
            repeatTask = new Timer();
            repeatTask.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if(count>1)
                            //  repeatTask.cancel();
                         //   System.out.print("its timeeeeeeeeeeeeeeeeee");
                            // count++;
                            GPSTracker gps = new GPSTracker(rmain.this);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                            lm = sdf.format(new Date());
                            if (gps.canGetLocation()) {
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                if (latitude == 0.0) {
                                    Toast.makeText(getApplicationContext(), "wait for location and try again",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    longi = String.valueOf(longitude);
                                    lati = String.valueOf(latitude);
                                    FileOutputStream outputStream;
                                    try {
                                        outputStream = openFileOutput(filename1, Context.MODE_APPEND);
                                        String newline = "\n";
                                        longi = String.valueOf(longitude);
                                        lati = String.valueOf(latitude);
                                        String l1 = "  " + longitude + "  ";
                                        String l2 = "  " + latitude + "  ";
                                        outputStream.write(newline.getBytes());
                                        outputStream.write(phone.getBytes());
                                        outputStream.write(lm.getBytes());
                                        outputStream.write(l1.getBytes());
                                        outputStream.write(l2.getBytes());
                                        outputStream.close();
                                        db.execSQL("INSERT INTO mylocation VALUES('" + phone + "','" + lm + "','" + lati + "','" + longi + "');");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        FileInputStream fileIn = openFileInput("mydata1");
                                        InputStreamReader InputRead = new InputStreamReader(fileIn);
                                        char[] inputBuffer = new char[READ_BLOCK_SIZE];
                                        String s = "";
                                        int charRead;
                                        while ((charRead = InputRead.read(inputBuffer)) > 0) {
                                            String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                                            s += readstring;
                                        }
                                        InputRead.close();
                                        File filepath = new File(file, "output2" + ".txt");  // file path to save
                                        FileWriter writer = new FileWriter(filepath);
                                        writer.append(s.toString());
                                        writer.flush();
                                        writer.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                            if(pause.equals(0))
                            {

                            }
                                else
                                gps.showSettingsAlert();
                            }

                        }
                    });
                }
            }, 0, 900000);
    }
    @Override
    public void onStart() {
        pause =1;
        super.onStart();
       // Toast.makeText(getContext(), "onStart called", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        pause =1;
        super.onResume();
       // Toast.makeText(getContext(), "onResumed called", Toast.LENGTH_LONG).show();

    }



    @Override
    public void onDestroy() {
        pause =0 ;
        super.onDestroy();
        System.out.print("destroyeddddddddddddd");
       // Toast.makeText(getContext(), "onDestroy called", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onStop() {
        pause =0 ;
        super.onStop();
        System.out.print("stopppppppppp");
       // Toast.makeText(getContext(), "onStop called", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onPause() {
        pause =0 ;
        super.onPause();
       // Toast.makeText(getContext(), "onPause called", Toast.LENGTH_LONG).show();

    }
}