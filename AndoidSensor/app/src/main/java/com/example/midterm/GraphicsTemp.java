package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GraphicsTemp extends AppCompatActivity {
    final tempDB tempDBB = new tempDB(GraphicsTemp.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_temp);
        TempActivity tempActivity = new TempActivity();


        tempActivity.tempDatas=tempDBB.veriListele();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < tempActivity.tempDatas.size(); i++) {
            DataPoint point = new DataPoint(i, Double.parseDouble(tempActivity.tempDatas.get(i)));
            series.appendData(point, true, tempActivity.tempDatas.size());
        }
        graph.addSeries(series);



    }




}










