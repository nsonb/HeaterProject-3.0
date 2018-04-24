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
        Button mainButton = (Button)findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
        Button sontrasButton = (Button)findViewById(R.id.sontrasButton);
        sontrasButton.setOnClickListener(this);
        Button batteryButton = (Button)findViewById(R.id.batteryButton);
        batteryButton.setOnClickListener(this);
        Button environmentButton = (Button)findViewById(R.id.environmentButton);
        environmentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainButton:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.sontrasButton:
                Intent sontrasTest = new Intent(this, SontrasTestActivity.class);
                startActivity(sontrasTest);
                break;
            case R.id.batteryButton:
                Intent battery = new Intent(this, BatteryActivity.class);
                startActivity(battery);
                break;
            case R.id.environmentButton:
                Intent environment = new Intent(this, EnvironmentActivity.class);
                startActivity(environment);
                break;
        }

    }
}
