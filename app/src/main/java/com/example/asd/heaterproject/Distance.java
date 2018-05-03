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
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

public class Distance extends AppCompatActivity implements View.OnClickListener {
    public Double latitude, longitude, inputLat, inputLong;
    public Double calculationLat, calculationLong, calculation;
    public String location, result, inputVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        // find text view
        TextView currentLong = (TextView) findViewById(R.id.currentLong);
        TextView currentLat = (TextView) findViewById(R.id.currentLat);
        TextView currentLoc = (TextView) findViewById(R.id.currentLoc);


        // find buttons
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        Button enterTextButton = (Button)findViewById(R.id.enterTextButton);
        enterTextButton.setOnClickListener(this);

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
            addresses = gc.getFromLocation(latitude,longitude,1);

            String addressStr = addresses.get(0).getAddressLine(0);
            String areaStr = addresses.get(0).getLocality();
            //String cityStr = addresses.get(0).getAdminArea();
            String countryStr = addresses.get(0).getCountryName();
            String postalcodeStr = addresses.get(0).getPostalCode();

            String fullAddress = addressStr +", "+ postalcodeStr +", "+ countryStr;
            currentLoc.setText(fullAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        TextView distance = (TextView) findViewById(R.id.distance);

        switch (v.getId()){
            case R.id.enterTextButton:
                EditText inputText = (EditText) findViewById(R.id.inputText);
                inputVal = inputText.getText().toString();

                Geocoder geoc = new Geocoder(getApplicationContext());
                int i = 0;

                try {
                    List<Address> geoResults = geoc.getFromLocationName(inputVal, 1);
                    while (geoResults.size()==0 && i<5) {
                        geoResults = geoc.getFromLocationName(inputVal, 1);
                        i++;
                    }
                    if (geoResults.size()>0) {
                        Address addr = geoResults.get(0);
                        inputLat = addr.getLatitude();
                        inputLong = addr.getLongitude();

                        //jäähalli as static point:
                        //lat: 60.26366729999999
                        //long: 24.840160400000065408
                        //Myyrmäen jäähalli as static point:
                        calculationLat = ((latitude - inputLat)*(40000/360));
                        calculationLong = ((longitude - inputLong)*((40000*cos(60))/360));
                        calculation = (sqrt((Math.pow(calculationLat, 2.0))+(Math.pow(calculationLong, 2.0))));

                        result = String.format("%.3f", calculation);
                        distance.setText(result + " km");
                    }
                    if (i >= 5){
                        distance.setText("try again");
                    }
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }

                break;

            case R.id.backButton:
                Intent gps = new Intent(this, Hiking.class);
                startActivity(gps);
                break;
        }
    }
}