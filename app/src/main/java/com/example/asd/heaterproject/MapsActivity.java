package com.example.asd.heaterproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //name for shared preference storing lat long data
    private static final String LOCATIONID = "LatLong";

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in the last recorded device position and move the camera
        // if shared preferences are empty, default coordinates are in Inari, Lapland
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double latitude = Double.valueOf(prefGet.getString("latitude","68.9105231"));
        double longitude = Double.valueOf(prefGet.getString("longitude","27.0174193"));
        latitude = (double)Math.round(latitude * 100000000d) / 100000000d;
        longitude = (double)Math.round(longitude * 100000000d) / 100000000d;
        LatLng position = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(position).title("Marker at " + latitude + ", " + longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}
