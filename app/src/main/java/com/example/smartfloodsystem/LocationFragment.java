package com.example.smartfloodsystem;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class LocationFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap map;
    Button btnConfirm;
    TextView lblMsg;
    SearchView searchlocation;
    String selectedLocation;


    public LocationFragment() {
        // Required empty public constructor
        selectedLocation = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_location, container, false);

       searchlocation = (SearchView) v.findViewById(R.id.location_search);
       btnConfirm = (Button) v.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedLocation = searchlocation.getQuery().toString();
                WaterLevelFragment wf = new WaterLevelFragment ();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, wf)
                        .addToBackStack(null)
                        .commit();

            }
        });

       mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       if(mapFragment==null)
       {
           FragmentManager fm = getFragmentManager();
           FragmentTransaction ft = fm.beginTransaction();
           mapFragment = SupportMapFragment.newInstance();
           ft.replace(R.id.map, mapFragment).commit();
       }


        searchlocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchlocation.getQuery().toString();
                List<Address> addressList = null;

                if(location != null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder (getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latlng = new LatLng (address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latlng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));


                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sunway = new LatLng(3.071880, 101.604113);
        map.addMarker(new MarkerOptions().position(sunway).title("Marker in Sunway University"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sunway));
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
