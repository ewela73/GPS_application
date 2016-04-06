package com.example.domi.gps_application;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;

    public static LatLng point;
    public static LatLng point1;
    static PolylineOptions option = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    double latitude;
    double longitude;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        point1 = new LatLng(GPS_Point.array_GPS_Point.get(0).getLatitude(), GPS_Point.array_GPS_Point.get(0).getLongitude());
        GPS_Point.array_GPS_Point.get(0).getLatitude();

        for(GPS_Point g : GPS_Point.array_GPS_Point)
        {
            latitude = g.getLatitude();
            longitude = g.getLongitude();

            point = new LatLng(latitude, longitude);
            //array_points.add(point);
            option.add(point);
        }

        mMap.addPolyline(option);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point1, 50));

    }
}
