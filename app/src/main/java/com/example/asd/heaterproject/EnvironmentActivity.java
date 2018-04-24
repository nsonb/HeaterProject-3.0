package com.example.asd.heaterproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EnvironmentActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        // find text view
        TextView intro = (TextView) findViewById(R.id.textView1);

        // find button
        Button goBack = (Button) findViewById(R.id.goBackButton);
        goBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
