package com.example.datn_tuandm_1534560;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.datn_tuandm_1534560.MainActivity.LANGUAGE;


public class GraphActivity extends AppCompatActivity {
    private BarChart mChart;
    ArrayList<BarEntry> yValues;
    ArrayList<String> labels;
    ArrayList<String> weeklyWorkout = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.weekly_mileage);

        mChart = findViewById(R.id.bar_chart);
//        mChart.setOnChartGestureListener(this);
//        mChart.setOnChartValueSelectedListener(this);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        yValues = new ArrayList<>();
        labels = new ArrayList<>();
        for(int i = 0;i < selectAll().size();i++){
            String week = selectAll().get(i).getDATE();
            double tong = Double.parseDouble(weeklyWorkout.get(i));
            yValues.add(new BarEntry(i,(float) tong));
            labels.add("Week "+week);
        }

        BarDataSet barDataSet = new BarDataSet(yValues,"Weekly Workout");
        barDataSet.setColors(Color.DKGRAY);
        Description description = new Description();
        description.setText("weeks");
        mChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        mChart.setData(barData);
        //X axis formatter
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        //set position of labels
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
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
        Cursor cursor = dbOpenHelper.SelectAllByWeek(database);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Double distance = cursor.getDouble(cursor.getColumnIndex(DBStructure.DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndex(DBStructure.DURATION));
            String type = cursor.getString(cursor.getColumnIndex(DBStructure.TYPE));
            String feel = cursor.getString(cursor.getColumnIndex(DBStructure.FEEL));
            String week = cursor.getString(cursor.getColumnIndex(DBStructure.WEEK_OF_YEAR));
            String ww = cursor.getString(cursor.getColumnIndex("tong"));
            Events events1 = new Events(event,time,date,month,year,distance,duration,type,feel,week,id);
            events.add(events1);
            weeklyWorkout.add(ww);
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

        if (id == R.id.action_allworkout) {
            Intent intent = new Intent(this,GraphAllWorkoutActivity.class);
            startActivity(intent);

        }
        if(id == R.id.action_combine){
            Intent intent = new Intent(this,GraphCombine.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    //disable menu hien tai
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
//            menu.getItem(1).setEnabled(false);
            // You can also use something like:
             menu.findItem(R.id.action_weekly).setEnabled(false);
        return true;
    }

}
