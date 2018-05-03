package com.example.asd.heaterproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    // some variables for the notification service
    Intent nomnomServiceIntent;
    private NotificationService nomnomNotificationService;
    Context cntx;
    public Context getCntx(){return cntx;}

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
            case R.id.batteryButton:
                Intent battery = new Intent(this, BatteryActivity.class);
                startActivity(battery);
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