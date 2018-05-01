package com.example.asd.heaterproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
        altitude = altValue.getString("altitude", "Not found");
        TextView currentAlt = (TextView) findViewById(R.id.currentAlt);
        currentAlt.setText(String.valueOf(altitude));

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(60, 24),
                new DataPoint(61, 25),
                new DataPoint(62, 26),
                new DataPoint(63, 25),
                new DataPoint(67, 24)
        });
        graph.addSeries(series);
    }
}