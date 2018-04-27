package com.example.asd.heaterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Indoors extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoors);

        // find text view
        TextView intro = (TextView) findViewById(R.id.introText);

        // find buttons
        Button sontrasButton = (Button)findViewById(R.id.sontrasButton);
        sontrasButton.setOnClickListener(this);
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sontrasButton:
                Intent sontrasTest = new Intent(this, SontrasTestActivity.class);
                startActivity(sontrasTest);
                break;
            case R.id.backButton:
                Intent mainMenu = new Intent(this, LauncherActivity.class);
                startActivity(mainMenu);
                break;
        }
    }
}