package com.example.asd.heaterproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatteryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MAXPOSITIONS = 20;
    private static final String PREFERENCEID = "Credentials";
    private static final String LOCATIONID = "LatLong";
    private String username, password;
    TextView batteryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        batteryView = findViewById(R.id.battery);

        // initialize the array so that every position has an object (even it is empty string)

        // setup the button event listener to receive onClick events
        ((Button) findViewById(R.id.mybutton)).setOnClickListener(this);

        // check that we know username and password for the Thingsee cloud

    }

    public void onClick(View v) {
        Log.d("USR", "Button pressed");

        // we make the request to the Thingsee cloud server in backgroud
        // (AsyncTask) so that we don't block the UI (to prevent ANR state, Android Not Responding)
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID,Activity.MODE_PRIVATE);
        batteryView.setText(prefGet.getString("battery","0"));
    }
}
