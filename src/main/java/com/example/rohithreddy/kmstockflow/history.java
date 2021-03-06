package com.example.rohithreddy.kmstockflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;


public class history extends Fragment {
    rmain mainscreen;
    registeroutlet userdata;

   private ListView lv;
    private mapuseradapter adapter;
    private List<mapuser> mapuserList;
    private Cursor c;

    private SQLiteDatabase db;
    public  void GetList(String searchword){
        mapuserList.clear();
        if(searchword=="all"||searchword.length()==0||searchword=="all1")
        c = db.rawQuery("SELECT * FROM mapusers  ", null);
        else{
            System.out.println(searchword);
            c = db.rawQuery("SELECT * FROM mapusers where username like '"+searchword+"%'", null);
            //Toast.makeText(getContext(),searchword, Toast.LENGTH_LONG).show();
        }
        if (!(c.moveToFirst()) || c.getCount() == 0){
            System.out.println("no results found");
        }
        else {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                System.out.println(c.getColumnNames());

                int totalColumn = c.getColumnCount();
                for (int i = 0; i < totalColumn; i++) {
                    if (c.getColumnName(i) != null) {
                        try {
                            if (c.getString(i) != null) {
                                System.out.println("here i is"+i);
                                System.out.println(c.getColumnName(i));
                                System.out.println(c.getString(i));
                            } else {
                                System.out.println("its else" + c.getColumnName(i));
                            }
                        } catch (Exception e) {

                        }
                    }
                    System.out.println("one step over");
                }
                mapuserList.add(0,new mapuser(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(5),c.getString(6)));
                c.moveToNext();
            }
            if(searchword=="all")
                System.out.println("its all");
            else
                adapter.notifyDataSetChanged();
            c.close();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_history, container, false);

    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        lv=(ListView) view.findViewById(R.id.listv);
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mapuserList = new ArrayList<>();
        mapuserList.clear();
        final EditText search = (EditText) view.findViewById(R.id.searchid);
        db=getActivity().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mapuserList.clear();
                GetList("all1");
                swipeLayout.setRefreshing(false);
            }
        });
        GetList("all");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String x =search.getText().toString();
                System.out.println(x);
                GetList(x);
                System.out.println(x);
                // /search.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      //  mapuserList.add(new mapuser(1,"rohith shop","rohith","9502177727"));
      //  mapuserList.add(new mapuser(2,"rahul shop","rahil","9090909090"));

        adapter=new mapuseradapter(getActivity(),mapuserList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.print("-------->its clicked");
                System.out.println(adapterView.getItemAtPosition(i));
                System.out.print("....");
                System.out.println(view.getTag());
                Fragment fragment = null;
                fragment = new registeroutlet();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    mainscreen.navItemIndex=3;
                    mainscreen.mapvalue=view.getTag().toString();
                  //  userdata.ro3.setText(view.getTag().toString());
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

}
