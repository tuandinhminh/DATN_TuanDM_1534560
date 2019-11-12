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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;



public class GraphCombine extends AppCompatActivity {
    private CombinedChart mChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<Entry> lineEntries;
    ArrayList<String> labels1,labels2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_combine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.combined);
        mChart = findViewById(R.id.graph_combine);
        mChart.getDescription().setText("km");
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart.setDrawBarShadow(true);
//        mChart.setHighlightFullBarEnabled(true);
        //Ve bar sau line
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });


        //khoi tao 2 mang gia tri
        barEntries = new ArrayList<>();
        labels1 = new ArrayList<>();
        //them du lieu vao mang barchart
        for(int i = 0;i < selectAll().size();i++){
            String type = selectAll().get(i).getTYPE();
            double distance = selectAll().get(i).getDISTANCE();
            barEntries.add(new BarEntry(i,(float) distance));
            labels1.add(type);
        }
        //them du lieu vao mang linechart
        lineEntries = new ArrayList<>();
        labels2 = new ArrayList<>();
        for(int i = 0;i < selectAll().size();i++){
            double distance = selectAll().get(i).getDISTANCE();
            int duration = Integer.parseInt(selectAll().get(i).getDURATION());
            int hours = duration/10000,
                    minutes = (duration%10000)/100,
                    seconds = (duration%10000)%100;
            if(seconds >= 60){
                minutes+=1;
                seconds-=60;
            }
            if (minutes >= 60){
                hours+=1;
                minutes-=60;
            }
            double pace = (seconds+minutes*60+hours*3600)/distance;
            hours = 0;
            minutes = (int)pace/60;
            seconds = (int)pace%60;
            if (minutes >= 60){
                hours=minutes/60;
                minutes%=60;
            }
            String minute = minutes+"",second = seconds+"";
            if(minutes < 10) minute = "0"+minute;
            if (seconds < 10) second = "0"+second;
            lineEntries.add(new Entry(i,(float)pace/60));
            labels2.add(hours+":"+minute+":"+second + "/km");
        }

        //tao ra du lieu line
        LineData d = new LineData();
        LineDataSet set = new LineDataSet(lineEntries, "Line");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        //tao ra du kieu bar
        BarDataSet set1 = new BarDataSet(barEntries, "Bar");
        set1.setColor(Color.rgb(60, 60, 60));
        set1.setValueTextColor(Color.rgb(0, 0, 0));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        float barWidth = 0.55f; // x2 dataset
        BarData d1 = new BarData(set1);
        d1.setBarWidth(barWidth);
        //legends orientation
        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //truc doc
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //nhan cua truc ngang
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels2.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels2));

        //tao doi tuong ket hop
        CombinedData data = new CombinedData();
        data.setData(d);
        data.setData(d1);
        //tao bieu do
        xAxis.setAxisMaximum(data.getXMax() + 0.24f);
        xAxis.setAxisMinimum(data.getXMin() - 0.24f);
        mChart.setData(data);
        mChart.animateY(1500);
        mChart.animateX(1500);
        mChart.invalidate();
    }
    //Ham lay du lieu tu database
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
    //khoi tao menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_graph, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_allworkout){
            Intent intent = new Intent(this,GraphAllWorkoutActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_weekly) {
            Intent intent = new Intent(this,GraphActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {

//            menu.getItem(1).setEnabled(false);
        // You can also use something like:
        menu.findItem(R.id.action_combine).setEnabled(false);

        return true;
    }
}
