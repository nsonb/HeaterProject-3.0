package com.example.asd.heaterproject;

public class Environment {

    private String provider;
    private double temperature;
    private double humidity;
    private long time;

    public Environment(String prvd) {
        provider = prvd;
    }

    //set value methods
    public void setTime (long value) {
        time = value;
    }

    public void setTemperature(double value) {
        temperature = value;
    }
    public void setHumidity(double value) {
        humidity = value;
    }

    //get value methods
    public long getTime () {
        return time;
    }
    public double getTemperature () {
        return temperature;
    }

    public double getHumidity () {
        return humidity;
    }
}
