package com.example.datn_tuandm_1534560;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphAllWorkoutActivity extends AppCompatActivity {
    private BarChart mChart;
    ArrayList<BarEntry> yValues;
    ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_all_workout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //bar chart
        getSupportActionBar().setTitle(R.string.all_workout);
        mChart = findViewById(R.id.graph_all_workout);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        yValues = new ArrayList<>();
        labels = new ArrayList<>();
        for(int i = 0;i < selectAll().size();i++){
            String date = selectAll().get(i).getDATE();
            String type = selectAll().get(i).getTYPE();
            double distance = selectAll().get(i).getDISTANCE();
            yValues.add(new BarEntry(i,(float) distance));
            labels.add(date);
        }

        BarDataSet barDataSet = new BarDataSet(yValues,"All Workout");
        barDataSet.setColor(Color.parseColor("#00BCD4"));
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        mChart.setData(barData);
        //X axis formatter
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        //set position of labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
//        xAxis.setLabelRotationAngle(270);
        mChart.animateY(1500);
        mChart.invalidate();

    }


    private ArrayList<Events> selectAll(){
        ArrayList<Events> events = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadAllEvents(database);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            double distance = cursor.getDouble(cursor.getColumnIndex(DBStructure.DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndex(DBStructure.DURATION));
            String type = cursor.getString(cursor.getColumnIndex(DBStructure.TYPE));
            String feel = cursor.getString(cursor.getColumnIndex(DBStructure.FEEL));
            String week = cursor.getString(cursor.getColumnIndex(DBStructure.WEEK_OF_YEAR));
            Events events1 = new Events(event,time,date,month,year,distance,duration,type,feel,week,id);
            events.add(events1);
        }
        cursor.close();
        dbOpenHelper.close();
        return events;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_graph, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_weekly) {
            Intent intent = new Intent(this,GraphActivity.class);
            startActivity(intent);

        }
        if(id == R.id.action_combine){
            Intent intent = new Intent(this,GraphCombine.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        // menu.getItem(1).setEnabled(false);
        // You can also use something like:
        menu.findItem(R.id.action_allworkout).setEnabled(false);

        return true;
    }
}
