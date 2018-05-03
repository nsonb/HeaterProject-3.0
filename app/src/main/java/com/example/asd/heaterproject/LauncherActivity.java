package com.example.asd.heaterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // find text view
        TextView intro = (TextView) findViewById(R.id.introText);

        // find buttons
        Button indoorsButton = (Button)findViewById(R.id.indoorsButton);
        indoorsButton.setOnClickListener(this);
        Button hikingButton = (Button)findViewById(R.id.hikingButton);
        hikingButton.setOnClickListener(this);
        Button batteryButton = (Button)findViewById(R.id.batteryButton);
        batteryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.indoorsButton:
                Intent indoors = new Intent(this, Indoors.class);
                startActivity(indoors);
                break;
            case R.id.hikingButton:
                Intent hiking = new Intent(this, Hiking.class);
                startActivity(hiking);
                break;
            case R.id.batteryButton:
                Intent battery = new Intent(this, BatteryActivity.class);
                startActivity(battery);
                break;
        }
    }
}