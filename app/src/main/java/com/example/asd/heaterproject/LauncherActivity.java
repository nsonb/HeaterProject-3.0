package com.example.asd.heaterproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    // some variables for the notification service
    Intent nomnomServiceIntent;
    private NotificationService nomnomNotificationService;
    Context cntx;
    public Context getCntx(){return cntx;}


    private static final String LOCATIONID = "LatLong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // find text view
        TextView intro = (TextView) findViewById(R.id.introText);

        // find buttons
        ProgressBar batterylevel = findViewById(R.id.batteryBar);

        Button indoorsButton = (Button)findViewById(R.id.indoorsButton);
        indoorsButton.setOnClickListener(this);
        Button hikingButton = (Button)findViewById(R.id.hikingButton);
        hikingButton.setOnClickListener(this);
        Button requestButton = (Button)findViewById(R.id.request_button);
        requestButton.setOnClickListener(this);

        TextView batteryView = (TextView) findViewById(R.id.batteryButton);
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        batteryView.setText(prefGet.getString("battery","-1") + "%");
        batterylevel.setProgress((int)Double.parseDouble(prefGet.getString("battery","0")));

        // notification service onCreate stuff
        cntx = this;
        nomnomNotificationService = new NotificationService(getCntx());
        nomnomServiceIntent = new Intent(getCntx(), nomnomNotificationService.getClass());
        if(!isBackgroundServiceRunning(nomnomNotificationService.getClass())){
            startService(nomnomServiceIntent);
        }
    }

    // button clicks
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
            case R.id.request_button:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
        }
    }

    // method for starting and restarting the background service process
    private boolean isBackgroundServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())){
                Log.i("Is the service running?", true+"");
                return true;
            }
        }
        Log.i("Is the service running?", false+"");
        return false;
    }

    // stopping the background service in onDestroy enables us to restart it
    @Override
    protected void onDestroy(){
        stopService(nomnomServiceIntent);
        Log.i("LAUNCHER", "onDestroy.");
        super.onDestroy();
    }
}