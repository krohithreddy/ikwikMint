package com.example.rohithreddy.kmstockflow;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class mainl extends Fragment {

    rmain mainscreen;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mainl, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("     IKWIKMINT");
        Button su,ro,at,sr;
        ro = view.findViewById(R.id.ro);
        at = view.findViewById(R.id.at);
        sr = view.findViewById(R.id.sr);
        su = view.findViewById(R.id.su);
        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new MainActivity();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    mainscreen.navItemIndex=2;
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
    });
        ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new registeroutlet();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    mainscreen.navItemIndex=3;
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });
        at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new attendance();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    mainscreen.navItemIndex=1;
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });
       sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new onsubmit(getActivity());
            }
        });
}
}