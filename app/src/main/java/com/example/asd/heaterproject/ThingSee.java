/*
 * ThingSee.java -- ThingSee Cloud server client
 *
 * API documentation:
 *  http://api.thingsee.com/doc/rest
 *  https://thingsee.zendesk.com/hc/en-us/articles/205188962-Thingsee-Property-API-
 *  https://thingsee.zendesk.com/hc/en-us/articles/205188982-Thingsee-Events-API-
 * Web interface:
 *  http://app.thingsee.com
 *
 * Copyright (C) 2017 by ZyMIX Oy. All rights reserved.
 * Author(s): Jarkko Vuori
 * Modification(s):
 *   First version created on 04.02.2017
 *   Fixed timeout issue on 24.04.2018
 */
package com.example.asd.heaterproject;

import android.location.Location;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.*;

public class ThingSee {
    private final static String charset = "UTF-8";
    private final static String url     = "http://api.thingsee.com/v2";

    //private URLConnection connection;
    private String        accountAuthUuid;
    private String        accountAuthToken;
    private Boolean       fConnection;

    /**
     * Authenticates the user
     * <p>
     * Credentials returned from the cloud server are used in the following method calls
     * (so that it is not needed to send email/password information every time to the server).
     *
     * @param email      User's email address used for the authentication
     * @param passwd     User's password
     * @throws Exception Gives an exception with text information if there was an error
     */
    ThingSee(String email, String passwd) throws Exception {
        JSONObject param = new JSONObject();

        param.put("email", email);
        param.put("password", passwd);

        JSONObject resp = getThingSeeObject(param, "/accounts/login");
        accountAuthUuid = resp.getString("accountAuthUuid");
        accountAuthToken = resp.getString("accountAuthToken");
    }

    /**
     * Send a request to the ThingSee server at the subpath
     * <p>
     * Authentication is supposed to have been done before (using a constructor)
     *
     * @param  request   Request parameter (optional, null if not needed)
     * @param  path      URI-name for the object to be requested
     * @return           Requested object in JSON-format
     * @throws Exception Gives an exception with text information if there was an error
     */
    private JSONObject getThingSeeObject(JSONObject request, String path) throws Exception {
        HttpURLConnection connection = null;
        JSONObject        resp     = null;

        fConnection = false;
        try {
            connection = (HttpURLConnection) new URL(url + path).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);    // Spesify timeouts for the slow ThingSee server
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            if (accountAuthToken != null)
                connection.setRequestProperty("Authorization", "Bearer " + accountAuthToken);

            // send a request (if needed)
            if (request != null) {
                connection.setDoOutput(true);   // Triggers HTTP POST request
                connection.setChunkedStreamingMode(0);
                OutputStream output = connection.getOutputStream();
                output.write(request.toString().getBytes(charset));
                output.flush();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200)
                throw new Exception("Bad responce code " + responseCode);
            // wait for the reply
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            //System.out.println("Responce: " + out.toString());
            resp = new JSONObject(out.toString());
            fConnection = true;
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return (resp);
    }

    /**
     * Request the handle to the first ThingSee device at the cloud account
     * <p>
     * Cloud account may have multiple devices, this function selects always the first device
     *
     * @return Return JSON decription of the selected device
     */
    public JSONObject Devices() {
        JSONObject item, resp;

        try {
            resp = getThingSeeObject(null, "/devices");
            JSONArray devices = (JSONArray) resp.get("devices");
            item = devices.getJSONObject(0);
        } catch (Exception e) {
            Log.d("THINGSEE", "No Thingsee device");
            item = null;
        }

        return (item);
    }

    /**
     * Request all device events
     * <p>
     * Every device has an event log. This method read the given devices event log.
     *
     * @param  device    Device JSON description (given by Devices() method
     * @param  limit     Maximum number of events retrieved
     * @return           Events in JSON format
     * @throws Exception Gives an exception with text information if there was an error
     */
    public JSONArray Events(JSONObject device, int limit) throws Exception {
        JSONObject resp;
        JSONArray  events;

        try {
            resp = getThingSeeObject(null, "/events/" + device.getString("uuid") + "?limit=" + limit);
            events  = (JSONArray)resp.get("events");
        } catch (Exception e) {
            Log.d("THINGSEE", "ThingseeEvents error " + e);
            throw new Exception("No events");
        }

        return (events);
    }

    /* senseID groupID field */
    private static final int GROUP_LOCATION     = 0x01 << 16;
    private static final int GROUP_SPEED        = 0x02 << 16;
    private static final int GROUP_ENERGY       = 0x03 << 16;
    private static final int GROUP_ORIENTATION  = 0x04 << 16;
    private static final int GROUP_ACCELERATION = 0x05 << 16;
    private static final int GROUP_ENVIRONMENT  = 0x06 << 16;
    private static final int GROUP_HW_KEYS      = 0x07 << 16;

    /* senseID propertyID field */
    private static final int PROPERTY1          = 0x01 << 8;
    private static final int PROPERTY2          = 0x02 << 8;
    private static final int PROPERTY3          = 0x03 << 8;
    private static final int PROPERTY4          = 0x04 << 8;

    /**
     * Obtain Location objects from the events array
     * <p>
     * Collects all location events and construct Location object
     *
     * @param  events Device JSON description (given by Devices() method)
     * @return        List of Location objects (coordinates), empty if there are no coordinates available
     * @throws        Exception Gives an exception with text information if there was an error
     */

    public List getPath(JSONArray events) throws Exception {
        List   coordinates = new ArrayList();
        int    k;

        try {
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                Location   loc   = new Location("ThingseeONE");

                loc.setTime(event.getLong("timestamp"));
                k = 0;
                JSONArray senses = event.getJSONObject("cause").getJSONArray("senses");
                for (int j = 0; j < senses.length(); j++) {
                    JSONObject sense   = senses.getJSONObject(j);
                    int        senseID = Integer.decode(sense.getString("sId"));
                    double     value   = sense.getDouble("val");


                    switch (senseID) {
                        case GROUP_LOCATION | PROPERTY1:
                            loc.setLatitude(value);
                            k++;
                            break;

                        case GROUP_LOCATION | PROPERTY2:
                            loc.setLongitude(value);
                            k++;
                            break;

                        case GROUP_LOCATION | PROPERTY3:
                            loc.setAltitude(value);
                            k++;
                            break;
                    }

                    if (k == 3) {
                        coordinates.add(loc);
                        k = 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("No coordinates");
        }

        return coordinates;
    }

    /**
     * Obtain environment data from the thingsee cloud server
     *
     * @param events Device JSON description (given by Devices() method)
     * @return       List of Location objects (coordinates), empty if there are no coordinates available
     * @throws       Exception Gives an exception with text information if there was an error
     */

    public List getEnvironment(JSONArray events) throws Exception {
        List   conditions = new ArrayList();
        int    k;
        try {
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                Environment environment = new Environment("ThingseeONE");
                environment.setTime(event.getLong("timestamp"));
                k = 0;
                JSONArray senses = event.getJSONObject("cause").getJSONArray("senses");
                for (int j = 0; j < senses.length(); j++) {
                    JSONObject sense   = senses.getJSONObject(j);
                    int        senseID = Integer.decode(sense.getString("sId"));
                    double     value   = sense.getDouble("val");


                    switch (senseID) {
                        case GROUP_ENVIRONMENT | PROPERTY1:
                            environment.setTemperature(value);
                            k++;
                            break;

                        case GROUP_ENVIRONMENT | PROPERTY2:
                            environment.setHumidity(value);
                            k++;
                            break;
                    }

                    switch (senseID) {
                        case GROUP_ENERGY | PROPERTY2:
                            environment.setBattery(value);
                            k++;
                            break;
                    }

                    if (k == 3) {
                        conditions.add(environment);
                        k = 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("No coordinates");
        }

        return conditions;
    }

    /*
    @Override
    public String toString() {
        String s;

        if (fConnection)
            s = "Uuid: " + accountAuthUuid + "\nToken: " + accountAuthToken;
        else
            s = "Not authenticated";

        return (s);
    }
    */
    /**
     * Convert events to string
     * <p>
     * Converts timestamp and senses information of the event to string
     *
     * @param  events Events in JSON format
     * @return        Events in string format
     */
    /*
    public String toString(JSONArray events) {
        StringBuilder s = new StringBuilder();
        String        ss;

        try {
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);

                s.append(new Date(event.getLong("timestamp")) + ": ");
                //System.out.println("    type: " + event.getString("type"));
                JSONArray senses = event.getJSONObject("cause").getJSONArray("senses");
                for (int j = 0; j < senses.length(); j++) {
                    JSONObject sense = senses.getJSONObject(j);

                    s.append("sId " + sense.getString("sId") + ": " + sense.getDouble("val") + ",");
                }
                s.append("\n");
            }
            ss = s.toString();
        } catch (Exception e) {
            ss = null;
        }

        return ss;
    }
    */
}