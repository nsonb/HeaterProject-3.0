package com.example.asd.heaterproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SontrasTestActivity extends AppCompatActivity implements View.OnClickListener {

    static TextView location;
    static TextView weatherDescription;
    static TextView outdoorTemperature;
    static TextView outdoorHumidity;
    //name for shared preference storing lat long data
    private static final String LOCATIONID = "LatLong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sontras);

        // find text views
        TextView intro = (TextView) findViewById(R.id.textView1);
        TextView weatherIntro = (TextView) findViewById(R.id.textView2);
        location = (TextView) findViewById(R.id.placeName);
        weatherDescription = (TextView) findViewById(R.id.weatherDescription);
        outdoorTemperature = (TextView) findViewById(R.id.outdoorTemp);
        outdoorHumidity = (TextView) findViewById(R.id.outdoorHumidity);

        // find buttons
        Button goBack = (Button) findViewById(R.id.goBackButton);
        Button openMap = (Button) findViewById(R.id.openMapButton);
        goBack.setOnClickListener(this);
        openMap.setOnClickListener(this);

        // get location coordinates from shared preferences
        // default coordinates are at Metropolia campus
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double latitude = Double.valueOf(prefGet.getString("latitude","60.2212543"));
        double longitude = Double.valueOf(prefGet.getString("longitude","24.8050686"));

        // create an instance of the weather API class and call its execute
        WeatherAPI gibWeather = new WeatherAPI();
        gibWeather.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude +
                        "&lon=" + longitude + "&appid=41bc4335b5c44b26947871ea435a4a49");

        // change some strings
        intro.setText("Now with Google Maps and OpenWeatherMap weather.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openMapButton:
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
                break;
            case R.id.goBackButton:
                finish();
                break;
        }
    }
}
