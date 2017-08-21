package com.example.rohithreddy.kmstockflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rohithreddy on 26/07/17.
 */

public class mapuseradapter extends BaseAdapter {
    private Context mcontext;
    private List<mapuser> mapuserList;
    private Double Lat=0.0,Lng=0.0;

    public mapuseradapter(Context mcontext, List<mapuser> mapuserList) {
        this.mcontext = mcontext;
        this.mapuserList = mapuserList;
    }


    @Override
    public int getCount() {
        return mapuserList.size();
    }

    @Override
    public Object getItem(int i) {
        return mapuserList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(mcontext,R.layout.listview,null);
        TextView outletname=(TextView) v.findViewById(R.id.outletname);
        TextView username=(TextView) v.findViewById(R.id.username);
        TextView phonenum=(TextView) v.findViewById(R.id.phonenum);
        Lat = mapuserList.get(i).getLat();
        Lng = mapuserList.get(i).getLan();
        Button mapview=(Button) v.findViewById(R.id.mapview);
        mapview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", Lat);
                bundle.putDouble("lng", Lng);
                Intent mapactivity = new Intent(mcontext, MapsLocation.class);
                mapactivity.putExtras(bundle);
                mcontext.startActivity(mapactivity);
            }
        });
        outletname.setText(mapuserList.get(i).getOutletname());
        username.setText(mapuserList.get(i).getUsername());
        phonenum.setText(mapuserList.get(i).getPhonenumber());
        v.setTag(mapuserList.get(i).getId());
        return v;
    }
}
