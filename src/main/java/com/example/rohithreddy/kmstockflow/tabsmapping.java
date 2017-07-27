package com.example.rohithreddy.kmstockflow;


import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;




public class tabsmapping extends Fragment// implements AdapterView.OnItemSelectedListener
 {

    private static final String TAG = "tabs";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    UserSessionManager session;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.activity_tabsmapping, container, false);
    }
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);
            getActivity().setTitle("      Tabs Mapping");
       // getActivity().setTitle("     IKWIKMINT");

        //   FirebaseMessaging.getInstance().subscribeToTopic("news");
        // String msg = getString(R.string.msg_subscribed);
        //Log.d(TAG, msg);
        // Toast.makeText(tabs.this, msg, Toast.LENGTH_SHORT).show();

        // FirebaseInstanceId.getInstance().getToken();
        //String msg1 = getString(R.string.msg_token_fmt, token);
        //Log.d(TAG, msg1);
        //Toast.makeText(tabs.this, msg1, Toast.LENGTH_SHORT).show();

        // b.putString("n1value", "hello");
        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            System.out.println("ffffffffffffffffffffffffffffffff");
        tabLayout.setupWithViewPager(mViewPager);
            System.out.println("uuuuuuuuuuuuuuuuuuuu");
            SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
            System.out.println("kkkkkkkkkkkkkkkkkkkkkk");
            adapter.addFrament(new Mapping(), "MAP");
            System.out.println("cccccccccccc");
            adapter.addFrament(new history(), "HISTORY");
            System.out.println(adapter+"<-----p-------->");
            mViewPager.setAdapter(adapter);
            System.out.println(adapter);
        //setupviewpage(mViewPager);
    }

    private void setupviewpage(ViewPager ViewPager) {


    }

  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String x = String.valueOf(parent.getItemAtPosition(position));
        //  ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
        // ((TextView) parent.getItemAtPosition(1)).setTextColor(Color.BLUE);

        if(x=="logout"){
            session.logoutUser();
            // Intent loginIntent = new Intent(tabs.this, LoginActivity.class);
            // tabs.this.startActivity(loginIntent);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }*/


}