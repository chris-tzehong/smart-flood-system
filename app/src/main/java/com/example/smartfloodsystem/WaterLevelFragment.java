package com.example.smartfloodsystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;


public class WaterLevelFragment extends Fragment {
    private Button btnLocation;
    private View v;

    public WaterLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_water_level, container, false);

        btnLocation = v.findViewById(R.id.btnSelectLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 LocationFragment lf = new LocationFragment ();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, lf)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return v;
    }

}
