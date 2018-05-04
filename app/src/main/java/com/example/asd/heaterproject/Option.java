package com.example.asd.heaterproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Option extends AppCompatActivity implements View.OnClickListener{

    Button BackButton;
    CheckBox indoorExceedsOutdoorBox, humidExceedsBox, tempExceedsBox, locationChangeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        BackButton = findViewById(R.id.optionBack);
        indoorExceedsOutdoorBox = findViewById(R.id.indoorExceedsOutdoor);
        humidExceedsBox = findViewById(R.id.humidExceeds);
        tempExceedsBox = findViewById(R.id.temperatureExceeds);
        locationChangeBox = findViewById(R.id.locationChange);
        BackButton.setOnClickListener(this);

        //to get the value of the checkBox Option
        SharedPreferences prefGet = getSharedPreferences("Option", Activity.MODE_PRIVATE);
        Boolean inExceedsOut = prefGet.getBoolean("inExceedsOut", false);
        Boolean humidExceeds = prefGet.getBoolean("humidity", false);
        Boolean tempExceeds = prefGet.getBoolean("tempExceeds" , false);
        Boolean locationChange = prefGet.getBoolean("locationChange" , false);

        indoorExceedsOutdoorBox.setChecked(inExceedsOut);
        humidExceedsBox.setChecked(humidExceeds);
        tempExceedsBox.setChecked(tempExceeds);
        locationChangeBox.setChecked(locationChange);
    }

    @Override
    public void onClick(View v)
    {
        Intent indoor = new Intent(this, Indoors.class);
        startActivity(indoor);
    }

    public void OnCheckBoxClicked(View v)
    {
        // Is the view now checked?
        boolean checked = ((CheckBox) v).isChecked();
        boolean inExceedsOut, humidExceeds, tempExceeds, locationChange;

        SharedPreferences prefGet = getSharedPreferences("Option", Activity.MODE_PRIVATE);
        inExceedsOut = prefGet.getBoolean("inExceedsOut", false);
        humidExceeds = prefGet.getBoolean("humidity", false);
        tempExceeds = prefGet.getBoolean("tempExceeds" , false);
        locationChange = prefGet.getBoolean("locationChange" , false);
        // Check which checkbox was clicked
        switch(v.getId())
        {
            case R.id.indoorExceedsOutdoor:
                inExceedsOut = !inExceedsOut;
                ((CheckBox) v).setChecked(inExceedsOut);
                break;
            case R.id.humidExceeds:
                humidExceeds = !humidExceeds;
                ((CheckBox) v).setChecked(humidExceeds);
                break;
            case R.id.temperatureExceeds:
                tempExceeds = !tempExceeds;
                ((CheckBox) v).setChecked(tempExceeds);
                break;
            case R.id.locationChange:
                locationChange = !locationChange;
                ((CheckBox) v).setChecked(locationChange);

                break;
        }
        SharedPreferences prefPut = getSharedPreferences("Option", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        prefEditor.putBoolean("inExceedsOut", inExceedsOut);
        prefEditor.putBoolean("humidity", humidExceeds);
        prefEditor.putBoolean("tempExceeds" , tempExceeds);
        prefEditor.putBoolean("locationChange" , locationChange);
        prefEditor.commit();

    }
}
