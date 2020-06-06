package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


public class Graphics extends AppCompatActivity {
    final batteryDB batteryDBB = new batteryDB(Graphics.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        Battery2 battery2 = new Battery2();
        battery2.batteryDatas=batteryDBB.veriListele();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < battery2.batteryDatas.size(); i++) {
            DataPoint point = new DataPoint(i, Double.parseDouble(battery2.batteryDatas.get(i)));
            series.appendData(point, true, battery2.batteryDatas.size());
        }
        graph.addSeries(series);




    }
}
