package com.example.xtalker.manprotest;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Location extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    public View view;
    public Context ctx;
    public static String namawisata;
    public static double latitude, longitude;

    public Location() {
        // Required empty public constructor
    }

    public static void setLatLng(String nama, double lat, double lng){
        namawisata = nama;
        latitude = lat;
        longitude = lng;
    }
    @Override
    public void onAttach(Context context) {
        ctx = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*try{
            Log.i("Location", "Requested");
            lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }
        catch (SecurityException e){e.printStackTrace();}*/
        try{
        mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e){e.printStackTrace();}
        LatLng posisiAwal = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(posisiAwal).title(namawisata));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posisiAwal));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posisiAwal, 14));
    }
}
