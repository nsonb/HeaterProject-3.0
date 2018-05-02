package com.example.asd.heaterproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graph extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    public TextView currentAlt;
    public String altitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        SharedPreferences altValue = getSharedPreferences("LOCATIONID", Activity.MODE_PRIVATE);
        altitude = altValue.getString("altitude", "0");
        TextView currentAlt = (TextView) findViewById(R.id.currentAlt);
        currentAlt.setText(String.valueOf(altitude));

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 26),
                new DataPoint(10, 23),
                new DataPoint(20, 22),
                new DataPoint(30, 33),
                new DataPoint(40, 37),
                new DataPoint(50, 40),
                new DataPoint(60, 38),
                new DataPoint(70, 32),
                new DataPoint(80, 28),
                new DataPoint(90, 30)
        });
        graph.addSeries(series);
    }
}