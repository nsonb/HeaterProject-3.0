package com.example.asd.heaterproject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    // shared preferences
    private static final String LOCATIONID = "LatLong";
    private static final String PREFERENCEID = "Credentials";
    private static final String OPTIONID = "Option";
    private static final int MAXPOSITIONS = 20;
    private String username,password;

    // booleans for different checks
    public boolean temperatureAlertON = false;
    public boolean humidityAlertON = false;
    public boolean fireAlertON = false;
    public boolean locationAlertON = false;
    public boolean batteryAlertON = false;

    // other variables
    public long counter = 1;
    IssueNotification notifier;

    // for comparing location
    public double previousLatitude = 0.0;
    public double previousLongitude = 0.0;

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

        // schedules the timer to wake up every 15 seconds
        timer.schedule(timerTask, 15000, 15000);
    }

    // make checks every time the timer wakes up
    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                Log.i("Timer", "The background process is running... " + (counter++));
                SharedPreferences prefGet = getSharedPreferences(PREFERENCEID,Activity.MODE_PRIVATE);
                username = prefGet.getString("username","");
                password = prefGet.getString("password","");
                new TalkToThingsee().execute("QueryState");
                // always checks battery level
                checkBattery();
                // check other stuff only if user wants it
                SharedPreferences prefGetty = getSharedPreferences(OPTIONID,Activity.MODE_PRIVATE);
                if(prefGetty.getBoolean("inExceedsOut", false)){
                    compareTemperatures();
                }
                if(prefGetty.getBoolean("humidity", false)){
                    checkHumidity();
                }
                if(prefGetty.getBoolean("tempExceeds", false)){
                    checkFire();
                }
                if(prefGetty.getBoolean("locationChange", false)){
                    checkLocation();
                }
            }
        };
    }
    // NOTIFICATIONS
    // compare values and issue the appropriate notifications
    public void compareTemperatures(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double indoorTemperature = Double.valueOf(prefGet.getString("temperature","0.0."));
        if(!temperatureAlertON && WeatherAPI.tempCelsius != 999) {
            if (indoorTemperature <
                    WeatherAPI.tempCelsius + 5 && WeatherAPI.tempCelsius < 8) {
                notifier.notify("Temperature alert from In & Out",
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

    public void checkHumidity(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double indoorHumidity = Double.valueOf(prefGet.getString("humidity","0.0."));
        if(!humidityAlertON && indoorHumidity >= 50.0) {
            notifier.notify("Humidity alert from In & Out",
                    "Indoor humidity has increased critically.",
                    4288);
            humidityAlertON = true;
        }
        else if (temperatureAlertON && indoorHumidity < 50.0){
            humidityAlertON = false;
        }
    }

    public void checkFire(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double indoorTemperature = Double.valueOf(prefGet.getString("temperature","0.0."));
        if(!fireAlertON && indoorTemperature >= 60.0) {
            notifier.notify("Fire alert from In & Out!",
                    "Indoor temperature is over 60 °C!",
                    6666);
            fireAlertON = true;
        }
        else if (fireAlertON && indoorTemperature < 60.0){
            fireAlertON = false;
        }
    }

    public void checkLocation(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double latitude = Double.valueOf(prefGet.getString("latitude","0.0"));
        double longitude = Double.valueOf(prefGet.getString("longitude","0.0"));
        latitude = (double)Math.round(latitude * 100000000d) / 100000000d;
        longitude = (double)Math.round(longitude * 100000000d) / 100000000d;
        if(!locationAlertON && latitude != 0.0 && longitude != 0.0) {
            if ((latitude != previousLatitude || longitude != previousLongitude)&& previousLatitude != 0.0 && previousLongitude != 0.0) {
                notifier.notify("Device alert from In & Out",
                        "Location of Thingsee device has changed.",
                        1221);
                locationAlertON = true;
            }
        }
        else if (locationAlertON && latitude != 0.0 && longitude != 0.0){
            if (latitude == previousLatitude && longitude == previousLongitude) {
                locationAlertON = false;
            }
        }
        previousLatitude = latitude;
        previousLongitude = longitude;
    }

    public void checkBattery(){
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double battery = Double.parseDouble(prefGet.getString("battery","-1"));
        if(!batteryAlertON && battery != (-1)){
            if(battery < 10){
                notifier.notify("Battery alert from In & Out",
                        "Thingsee device battery is below 10%.",
                        7733);
                batteryAlertON = true;
            }
        }
        else if(batteryAlertON && battery != (-1)){
            if(battery >= 10){
                batteryAlertON = false;
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

    private class TalkToThingsee extends AsyncTask<String, Integer, String> {
        ThingSee thingsee;
        List<Location> coordinates = new ArrayList<Location>();
        List<Environment> conditions = new ArrayList<Environment>();

        @Override
        protected String doInBackground(String... params) {
            String result = "NOT OK";

            // here we make the request to the cloud server for MAXPOSITION number of coordinates
            try {
                thingsee = new ThingSee(username, password);

                JSONArray events = thingsee.Events(thingsee.Devices(), MAXPOSITIONS);
                //System.out.println(events);
                coordinates = thingsee.getPath(events);
                conditions = thingsee.getEnvironment(events);

//                for (Location coordinate: coordinates)
//                    System.out.println(coordinate);
                result = "OK";
            } catch(Exception e) {
                Log.d("NET", "Communication error: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // check that the background communication with the client was succesfull
            if (result.equals("OK")) {
                // now the coordinates variable has those coordinates
                // elements of these coordinates is the Location object who has
                // fields for longitude, latitude and time when the position was fixed

                for (int i = 0; i < coordinates.size(); i++) {

                    Location loc = coordinates.get(i);
                    //shared preference to put the latest lat long into
                    SharedPreferences prefPut = getSharedPreferences ("LOCATIONID", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefPut.edit();
                    prefEditor.putString("timestamp", String.valueOf(loc.getTime()));
                    prefEditor.putString("latitude", String.valueOf(loc.getLatitude()));
                    prefEditor.putString("longitude", String.valueOf(loc.getLongitude()));
                    prefEditor.putString("altitude", String.valueOf(loc.getAltitude()));
                    prefEditor.commit();
                }

                for (int i = 0; i < conditions.size(); i++) {
                    Environment environment = conditions.get(i);
                    //put temperature and humidity into a shared preference
                    SharedPreferences prefPut = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefPut.edit();
                    prefEditor.putString("temperature", String.valueOf(environment.getTemperature()));
                    prefEditor.putString("humidity", String.valueOf(environment.getHumidity()));
                    prefEditor.putString("battery" , String.valueOf(environment.getBattery()));
                    prefEditor.commit();

                }
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}
    }
}