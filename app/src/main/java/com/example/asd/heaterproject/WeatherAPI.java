package com.example.asd.heaterproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// an async task that makes an API call
public class WeatherAPI extends AsyncTask<String, Void, String>{

    private String result;
    // I'm giving temperature some ridiculous default value for checking purposes
    public static int tempCelsius = 999;

    // this is the boring stuff
    @Override
    protected String doInBackground(String... urls) {
        result = "";
        URL link;
        HttpURLConnection openWeatherMap;
        try {
            link = new URL(urls[0]);
            openWeatherMap = (HttpURLConnection)link.openConnection();
            InputStream in = openWeatherMap.getInputStream();
            InputStreamReader stReader = new InputStreamReader(in);
            int data = stReader.read();
            while(data!= -1){
                char current = (char)data;
                result += current;
                data = stReader.read();
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // this is where the API respond is associated with program variables
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // create corresponding JSON objects
            JSONObject respond = new JSONObject(result);
            JSONObject main = new JSONObject(respond.getString("main"));
            JSONObject sys = new JSONObject(respond.getString("sys"));

            // fetch the data
            String placeName = respond.getString("name");
            String countryCode = sys.getString("country");
            if(countryCode.equals("FI")) countryCode = "Finland";
            double temperature = Double.parseDouble(main.getString("temp"));
            tempCelsius = (int) (temperature -273.15);
            String humidity = main.getString("humidity");

            // weather description is in an array, so a JSON array has to be used
            String weatherDesc;
            JSONArray weatherArr = respond.getJSONArray("weather");
            // fetch the description only if it exists for the location
            if(weatherArr.length() > 0){
                JSONObject weatherObj = weatherArr.getJSONObject(0);
                String descriptionResult = weatherObj.getString("description");
                weatherDesc = descriptionResult;
                // convert the description into initial upper case letter
                char[] IntoUpperCase = weatherDesc.toCharArray();
                IntoUpperCase[0] = Character.toUpperCase(IntoUpperCase[0]);
                weatherDesc = new String(IntoUpperCase);
            } else { weatherDesc = "No weather description found for this location"; }

            // set the data to TextViews
            Indoors.intro.setText("Weather in area:"+ placeName + ", " + countryCode);
            
            //Indoors.weatherDescription.setText(weatherDesc);

            Indoors.outdoorTemp.setText(tempCelsius + " °C");
            Indoors.outdoorHumidity.setText(humidity + " %");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}