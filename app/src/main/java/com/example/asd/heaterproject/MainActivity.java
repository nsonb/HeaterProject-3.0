/*
 * MainActivity.java -- Simple demo application for the Thingsee cloud server agent
 *
 * Request 20 latest position measurements and displays them on the
 * listview wigdet.
 *
 * Note: you need to insert the following line before application -tag in
 * the AndroidManifest.xml file
 *  <uses-permission android:name="android.permission.INTERNET" />
 *
 * Author(s): Jarkko Vuori
 * Modification(s):
 *   First version created on 04.02.2017
 *   Clears the positions array before button pressed 15.02.2017
 *   Stores username and password to SharedPreferences 17.02.2017
 */
package com.example.asd.heaterproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asd.heaterproject.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int    MAXPOSITIONS = 20;
    private static final String PREFERENCEID = "Credentials";

    //name for shared preference storing lat long data
    private static final String LOCATIONID = "LatLong";

    private String               username, password;
    private String[]             positions = new String[MAXPOSITIONS];
    private String[]             enCondition = new String[MAXPOSITIONS];
    private ArrayAdapter<String> myAdapter;
//    private ArrayAdapter<String> myEnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the array so that every position has an object (even it is empty string)
        for (int i = 0; i < positions.length; i++)
            positions[i] = "";
        for (int i = 0; i < enCondition.length; i++)
            enCondition[i] = "";

        // setup the adapter for the array
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, positions);
//        myEnAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, enCondition);

        // then connect it to the list in application's layout
        ListView listView = (ListView) findViewById(R.id.mylist);
//        listView.setAdapter(myEnAdapter);
        listView.setAdapter(myAdapter);



        // setup the button event listener to receive onClick events
        ((Button)findViewById(R.id.mybutton)).setOnClickListener(this);

        // check that we know username and password for the Thingsee cloud
        SharedPreferences prefGet = getSharedPreferences(PREFERENCEID, Activity.MODE_PRIVATE);
        username = prefGet.getString("username", "");
        password = prefGet.getString("password", "");
        if (username.length() == 0 || password.length() == 0)
            // no, ask them from the user
            queryDialog(this, getResources().getString(R.string.prompt));


    }

    private void queryDialog(Context context, String msg) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.credentials_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView dialogMsg      = (TextView) promptsView.findViewById(R.id.textViewDialogMsg);
        final EditText dialogUsername = (EditText) promptsView.findViewById(R.id.editTextDialogUsername);
        final EditText dialogPassword = (EditText) promptsView.findViewById(R.id.editTextDialogPassword);

        dialogMsg.setText(msg);
        dialogUsername.setText(username);
        dialogPassword.setText(password);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                username = dialogUsername.getText().toString();
                                password = dialogPassword.getText().toString();

                                SharedPreferences prefPut = getSharedPreferences(PREFERENCEID, Activity.MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = prefPut.edit();
                                prefEditor.putString("username", username);
                                prefEditor.putString("password", password);
                                prefEditor.commit();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
}

    public void onClick(View v) {
        Log.d("USR", "Button pressed");

        // we make the request to the Thingsee cloud server in backgroud
        // (AsyncTask) so that we don't block the UI (to prevent ANR state, Android Not Responding)
        new TalkToThingsee().execute("QueryState");
    }

    /* This class communicates with the ThingSee client on a separate thread (background processing)
     * so that it does not slow down the user interface (UI)
     */
    private class TalkToThingsee extends AsyncTask<String, Integer, String> {
        ThingSee thingsee;
        List<Location> coordinates = new ArrayList<Location>();
        List<Environment> conditions = new ArrayList<Environment>();
        List<Double> battery = new ArrayList<>();

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
//                battery = thingsee.getBattery(events);

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

                    positions[i] = (new Date(loc.getTime())) +
                                   " (" + loc.getLatitude() + "," +
                                   loc.getLongitude() + ")"; //coordinates.get(i).toString();

                    //put the latest lat long into the preference
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

                    enCondition[i] = (new Date(environment.getTime())) + "(Temperature: "
                                     + environment.getTemperature() + ",Humidity: "
                                     + environment.getHumidity()+ "%)";
                    SharedPreferences.Editor prefEditor = prefPut.edit();
                    prefEditor.putString("temperature", String.valueOf(environment.getTemperature()));
                    prefEditor.putString("humidity", String.valueOf(environment.getHumidity()));
                    prefEditor.putString("battery" , String.valueOf(environment.getBattery()));
                    prefEditor.commit();

                }
/*
                    SharedPreferences prefPut = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefPut.edit();
                    prefEditor.putString("battery", String.valueOf(battery));
                    prefEditor.commit();
*/
            } else {
                // no, tell that to the user and ask a new username/password pair
                SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
                positions[0] = "(" + prefGet.getString("latitude","") + "," +
                                prefGet.getString("longitude","") + ")";
//                positions[0] = getResources().getString(R.string.no_connection);
//                enCondition[0] = getResources().getString(R.string.no_connection);
                queryDialog(MainActivity.this, getResources().getString(R.string.info_prompt));
            }
            myAdapter.notifyDataSetChanged();
//            myEnAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            // first clear the previous entries (if they exist)
            for (int i = 0; i < positions.length; i++)
                positions[i] = "";
            for (int i = 0; i< enCondition.length; i++)
                enCondition[i] = "";
            myAdapter.notifyDataSetChanged();
//            myEnAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {}
    }
}
