package com.example.datn_tuandm_1534560;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.ads.MobileAds;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.datn_tuandm_1534560.ConstantVariables.ADMOB_ID;
import static com.example.datn_tuandm_1534560.ConstantVariables.DATA_NAME;
import static com.example.datn_tuandm_1534560.ConstantVariables.DATA_PATH;
import static com.example.datn_tuandm_1534560.ConstantVariables.LAN_EN;
import static com.example.datn_tuandm_1534560.ConstantVariables.LAN_VI;
import static com.example.datn_tuandm_1534560.CustomCalendarView.SetUpCalendar;
import static com.example.datn_tuandm_1534560.CustomCalendarView.calendar;
import static com.example.datn_tuandm_1534560.CustomCalendarView.context;
import static com.example.datn_tuandm_1534560.DBOpenHelper.CREAT_EVENTS_TABLE;
import static com.example.datn_tuandm_1534560.DBOpenHelper.DROP_EVENTS_TABLE;

public class MainActivity extends AppCompatActivity  {
    private AdView mAdView, mAdView1;
    public static String LANGUAGE = LAN_EN;
    private CustomCalendarView customCalendarView;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase database;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LANGUAGE.equals(LAN_VI)){
            changeLanguage(LAN_VI);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCalendarView = findViewById(R.id.custom_calendar_view);
        //load quang cao
//        mAdView = findViewById(R.id.adView);
        mAdView1 = findViewById(R.id.adView1);
        MobileAds.initialize(context.getApplicationContext(), ADMOB_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        mAdView1.loadAd(adRequest);

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
            calendar = Calendar.getInstance(Locale.ENGLISH);
            SetUpCalendar();
        }
        if (id == R.id.action_export){

            //save data to CSV
            StringBuilder data = new StringBuilder();
            data.append("ID, Name, Time, Date, Month, Year, Distance, Duration, Type, Feel, Week, Notification");
            for(int i = 0; i < selectAll().size();i++){
                data.append("\n\""+ selectAll().get(i).getID() + "\",\""+selectAll().get(i).getEVENT() + "\",\""+
                        selectAll().get(i).getTIME() + "\",\""+selectAll().get(i).getDATE() + "\",\""+
                        selectAll().get(i).getMONTH() + "\",\""+selectAll().get(i).getYEAR() + "\",\""+
                        selectAll().get(i).getDISTANCE() + "\",\""+selectAll().get(i).getDURATION() + "\",\""+
                        selectAll().get(i).getTYPE() + "\",\""+selectAll().get(i).getFEEL() + "\",\""+
                        selectAll().get(i).getWEEK() + "\",\"" + selectAll().get(i).getNOTI() + "\"" );
            }
            try {
                //saving file into device
                FileOutputStream out = openFileOutput(DATA_NAME, Context.MODE_PRIVATE);
                out.write((data.toString()).getBytes());
                out.close();
                //exporting to drive
                Context context = getApplicationContext();
                File filelocation = new File(getFilesDir(), DATA_NAME);
                Uri path = FileProvider.getUriForFile(context, "com.example.datn_tuandm_1534560.fileprovider", filelocation);
                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                fileIntent.setType("text/csv");
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, DATA_NAME);
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                startActivity(Intent.createChooser(fileIntent, "Export Data"));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            // exporting to memory
            dbOpenHelper = new DBOpenHelper(getApplicationContext());
            //
            File exportDir = new File(Environment.getExternalStorageDirectory(), DATA_PATH);
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, DATA_NAME);
            try
            {
                verifyStoragePermissions(MainActivity.this);
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                database = dbOpenHelper.getReadableDatabase();
                Cursor curCSV = database.rawQuery("SELECT * FROM " + DBStructure.EVENT_TABLE_NAME,null);
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext())
                {
                    //Which column you want to exprort
                    String arrStr[] ={
                            curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4), curCSV.getString(5),
                            curCSV.getString(6), curCSV.getString(7), curCSV.getString(8),
                            curCSV.getString(9), curCSV.getString(10), curCSV.getString(11)
                    };
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
                Toast.makeText(this, R.string.confirm_export, Toast.LENGTH_SHORT).show();
            }
            catch(Exception sqlEx)
            {
                Log.e("QQQQQ", sqlEx.getMessage(), sqlEx);
            }
        }

        if (id == R.id.action_import){
            verifyStoragePermissions(MainActivity.this);
            FileReader file = null;
            try {
                file = new FileReader(Environment.getExternalStorageDirectory() + "/" + DATA_PATH + "/" + DATA_NAME);

                BufferedReader buffer = new BufferedReader(file);
                String columns = "ID, event, time, date, month, year, distance, duration, type, feel, week, notification";
                String str1 = "INSERT INTO " + DBStructure.EVENT_TABLE_NAME + " (" + columns + ") values(";
                String str2 = ");";
                dbOpenHelper = new DBOpenHelper(this);
                database = dbOpenHelper.getWritableDatabase();
                database.beginTransaction();
                String line = buffer.readLine();

                if ((line = buffer.readLine()) != null){
                    database.execSQL(DROP_EVENTS_TABLE);
                    database.execSQL(CREAT_EVENTS_TABLE);
                    do {
                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
                        Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
                        sb.append(str[0].substring(1,str[0].length()-1) + ",'");
                        sb.append(str[1].substring(1,str[1].length()-1) + "','");
                        sb.append(str[2].substring(1,str[2].length()-1) + "','");
                        sb.append(str[3].substring(1,str[3].length()-1) + "','");
                        sb.append(str[4].substring(1,str[4].length()-1) + "','");
                        sb.append(str[5].substring(1,str[5].length()-1) + "',");
                        sb.append(str[6].substring(1,str[6].length()-1) + ",'");
                        sb.append(str[7].substring(1,str[7].length()-1) + "','");
                        sb.append(str[8].substring(1,str[8].length()-1) + "','");
                        sb.append(str[9].substring(1,str[9].length()-1) + "','");
                        sb.append(str[10].substring(1,str[10].length()-1) + "','");
                        sb.append(str[11].substring(1,str[11].length()-1) + "'");

                        sb.append(str2);
                        database.execSQL(sb.toString());
                    }
                    while ((line = buffer.readLine()) != null);
                    Toast.makeText(this, R.string.confirm_import, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, R.string.alert3, Toast.LENGTH_SHORT).show();
                }

                database.setTransactionSuccessful();
                database.endTransaction();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.alert3, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
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
        dbOpenHelper = new DBOpenHelper(this);
        database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadAllEvents(database);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
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
            String noti = cursor.getString(cursor.getColumnIndex(DBStructure.NOTIFICATION));
            Events events1 = new Events(event,time,date,month,year,distance,duration,type,feel,week,id,noti);
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

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
