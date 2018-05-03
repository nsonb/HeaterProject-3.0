package com.example.asd.heaterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Hiking extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking);

        // find text view
        TextView intro = (TextView) findViewById(R.id.introText);

        // find buttons
        Button mainButton = (Button)findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
        Button environmentButton = (Button)findViewById(R.id.environmentButton);
        environmentButton.setOnClickListener(this);
        Button graphButton = (Button)findViewById(R.id.graphButton);
        graphButton.setOnClickListener(this);
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        Button distanceButton = (Button)findViewById(R.id.distanceButton);
        distanceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainButton:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.environmentButton:
                Intent environment = new Intent(this, EnvironmentActivity.class);
                startActivity(environment);
                break;
            case R.id.backButton:
                Intent mainMenu = new Intent(this, LauncherActivity.class);
                startActivity(mainMenu);
                break;
            case R.id.graphButton:
                Intent graph = new Intent(this, Graph.class);
                startActivity(graph);
                break;
            case R.id.distanceButton:
                Intent distance = new Intent(this, Distance.class);
                startActivity(distance);
                break;
        }
    }
}