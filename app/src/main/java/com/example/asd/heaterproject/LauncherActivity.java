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
        }

    }
}
