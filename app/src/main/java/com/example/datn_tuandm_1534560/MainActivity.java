package com.example.datn_tuandm_1534560;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import static com.example.datn_tuandm_1534560.CustomCalendarView.SetUpCalendar;
import static com.example.datn_tuandm_1534560.CustomCalendarView.calendar;

public class MainActivity extends AppCompatActivity  {
    private AdView mAdView,mAdView1;
    public static InterstitialAd interstitialAd;
    public static String LANGUAGE = "en";
    private CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LANGUAGE.equals("vi")){
            changeLanguage("vi");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCalendarView = findViewById(R.id.custom_calendar_view);
        //load quang cao
        mAdView = findViewById(R.id.adView);
        mAdView1 = findViewById(R.id.adView1);
        MobileAds.initialize(this, "ca-app-pub-2298280937767584~4259105170");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView1.loadAd(adRequest);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2298280937767584/5106310651");
        interstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_graph) {
            Intent intent = new Intent(this,GraphActivity.class);
            startActivity(intent);

        }
        if(id == R.id.action_current){
            if(interstitialAd.isLoaded()){
                interstitialAd.show();

            }
            else{
                Log.d("QQQ","ad hasn't been loaded");
            }
            calendar = Calendar.getInstance(Locale.ENGLISH);
            SetUpCalendar();
        }
        if (id == R.id.action_export){

            //luu du lieu ra file CSV
            StringBuilder data = new StringBuilder();
            data.append("ID,Name,Time,Date,Month,Year,Distance,Duration,Type,Feel,Week");
            for(int i = 0; i < selectAll().size();i++){
                data.append("\n"+ selectAll().get(i).getID() + ","+selectAll().get(i).getEVENT() + ","+
                        selectAll().get(i).getTIME() + ","+selectAll().get(i).getDATE() + ","+
                        selectAll().get(i).getMONTH() + ","+selectAll().get(i).getYEAR() + ","+
                        selectAll().get(i).getDISTANCE() + ","+selectAll().get(i).getDURATION() + ","+
                        selectAll().get(i).getTYPE() + ","+selectAll().get(i).getFEEL() + ","+selectAll().get(i).getWEEK());
            }
            try {
                //saving file into device
                FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                out.write((data.toString()).getBytes());
                out.close();
                //exporting
                Context context = getApplicationContext();
                File filelocation = new File(getFilesDir(),"data.csv");
                Uri path = FileProvider.getUriForFile(context,"com.example.datn_tuandm_1534560.fileprovider",filelocation);
                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                fileIntent.setType("text/csv");
                fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Data");
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileIntent.putExtra(Intent.EXTRA_STREAM,path);
                startActivity(Intent.createChooser(fileIntent,"Export Data"));

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        if(id== R.id.action_language){
            Intent intent = new Intent(this,LanguageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
    public void changeLanguage(String language){
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
    }
}
