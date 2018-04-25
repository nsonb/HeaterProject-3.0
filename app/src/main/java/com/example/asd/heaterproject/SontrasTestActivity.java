package com.example.asd.heaterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SontrasTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sontras);

        // find text view
        TextView intro = (TextView) findViewById(R.id.textView1);

        // find buttons
        Button goBack = (Button) findViewById(R.id.goBackButton);
        Button openMap = (Button) findViewById(R.id.openMapButton);
        goBack.setOnClickListener(this);
        openMap.setOnClickListener(this);

        intro.setText("Now with a Maps activity!");
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
