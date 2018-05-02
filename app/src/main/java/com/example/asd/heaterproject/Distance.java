package com.example.asd.heaterproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Distance extends AppCompatActivity implements View.OnClickListener {
    public Double latitude, longitude;
    public String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        // find text view
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView currentLong = (TextView) findViewById(R.id.currentLong);
        TextView currentLat = (TextView) findViewById(R.id.currentLat);
        TextView currentLoc = (TextView) findViewById(R.id.currentLoc);

        // find buttons
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        SharedPreferences latitudeValues = getSharedPreferences("LOCATIONID", Activity.MODE_PRIVATE);
        latitude = Double.valueOf(latitudeValues.getString("latitude", "0"));
        SharedPreferences longitudeValues = getSharedPreferences("LOCATIONID", Activity.MODE_PRIVATE);
        longitude = Double.valueOf(longitudeValues.getString("longitude", "0"));

        currentLong.setText(String.valueOf(longitude));
        currentLat.setText(String.valueOf(latitude));

        Geocoder gc=new Geocoder(getApplicationContext());
        StringBuilder builder=new StringBuilder();
        List<Address>addresses;

        try {
            /*addresses= gc.getFromLocation(latitude,longitude,1);//here we pass lat long and set the max return value 1
            if (addresses.size()>0){
                Address address=addresses.get(0);//i fetched the result from the list
                for(int i=0;i<address.getMaxAddressLineIndex();i++){//looping into the max address line contain the result
                    builder.append(address.getAddressLine(i)+"\n"); //appending the addressline of the given latitude longitude
                }
            }

            Toast.makeText(Distance.this,builder.toString(),Toast.LENGTH_LONG).show();//showing the address into the toast
            //to get the most accurate one
            */
            addresses = gc.getFromLocation(latitude,longitude,1);

            String addressStr = addresses.get(0).getAddressLine(0);
            //String areaStr = addresses.get(0).getLocality();
            //String cityStr = addresses.get(0).getAdminArea();
            String countryStr = addresses.get(0).getCountryName();
            String postalcodeStr = addresses.get(0).getPostalCode();

            String fullAddress = addressStr+", "+postalcodeStr+", "+countryStr;
            currentLoc.setText(fullAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                Intent gps = new Intent(this, Hiking.class);
                startActivity(gps);
                break;
        }
    }
}