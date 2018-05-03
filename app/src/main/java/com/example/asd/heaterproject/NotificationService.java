package com.example.asd.heaterproject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private static final String LOCATIONID = "LatLong";
    // booleans for different checks
    public boolean temperatureAlertON = false;

    // other variables
    public int counter = 1;
    IssueNotification notifier;

    // constructor
    public NotificationService(Context applicationContext){
        super();
        Log.i("START", "The app was launched.");
    }

    // an empty constructor is also required by the compiler
    public NotificationService(){ }

    @Override
    public int onStartCommand(Intent intent, int flags, int start_id){
        super.onStartCommand(intent, flags, start_id);
        notifier = new IssueNotification(this, LauncherActivity.class);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("EXIT", "The app was closed.");
        Intent broadcastIntent = new Intent("com.example.asd.ActivityRecognition.RestartNotifications");
        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    // create a timer
    private Timer timer;
    private TimerTask timerTask;
    public void startTimer(){
        timer = new Timer();
        initializeTimerTask();
        // schedules the timer to wake up every 5 seconds
        timer.schedule(timerTask, 5000, 5000);
    }

    // make checks every time the timer wakes up
    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                Log.i("Timer", "The background process is running... " + (counter++));
                compareTemperatures();
            }
        };
    }

    // compare values and issue the appropriate notifications
    public void compareTemperatures(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double indoorTemperature = Double.valueOf(prefGet.getString("temperature","0.0."));
        if(!temperatureAlertON && WeatherAPI.tempCelsius != 999) {
            if (indoorTemperature <
                    WeatherAPI.tempCelsius + 5) {
                notifier.notify("Alert from The Thingsee App",
                        "Indoor temperature has decreased critically.",
                        3322);
                temperatureAlertON = true;
            }
        }
        else if (temperatureAlertON && WeatherAPI.tempCelsius != 999){
            if (indoorTemperature >=
                    WeatherAPI.tempCelsius + 5) {
                temperatureAlertON = false;
            }
        }
    }

    public void stopTimerTask(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}