package com.example.rohithreddy.kmstockflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rohithreddy on 26/07/17.
 */

public class mapuseradapter extends BaseAdapter {
    private Context mcontext;
    private List<mapuser> mapuserList;

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
        outletname.setText(mapuserList.get(i).getOutletname());
        username.setText(mapuserList.get(i).getUsername());
        phonenum.setText(mapuserList.get(i).getPhonenumber());
        v.setTag(mapuserList.get(i).getId());
        return v;
    }
}
