package com.example.datn_tuandm_1534560;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.example.datn_tuandm_1534560.ConstantVariables.*;


public class CustomCalendarView extends LinearLayout {
    ImageButton previousButton,nextButton;
    FloatingActionButton FAB;
    static TextView currentDate;
    static GridView gridView;
    private static int MAX_CALENDAR_DAYS = 42 ;
    static Calendar calendar = Calendar.getInstance();
    static DBOpenHelper dbOpenHelper;
    static Context context;
    ArrayAdapter arrayAdapter1;
    ArrayList<String>type;
    static MyGridAdapter myGridAdapter;
    static AlertDialog alertDialog;
    static List<Date> dates = new ArrayList<>();
    static List<Events> eventsList = new ArrayList<>();
    int alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinute;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalendar();
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalendar();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(adapterView.getContext())
                        .inflate(R.layout.add_newevent_layout,null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                final MaskEditText duration = addView.findViewById(R.id.edittime);
                final EditText eventdistance = addView.findViewById(R.id.eventdistance);
                final Spinner spinnerType = addView.findViewById(R.id.eventtype);
                final SeekBar seekBarFeel = addView.findViewById(R.id.SeekbarFeel);
                Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat hformat = new SimpleDateFormat(timePartern);
                String event_Time = hformat.format(calendar.getTime());
                EventTime.setText(event_Time);
                final String[] eventFeel = new String[1];
                //set default feel
                eventFeel[0] = FEEL_EXCELLENT;
                final String[] eventType = new String[1];
                seekBarFeel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        setFeel(i,eventFeel,seekBar);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                spinnerType.setAdapter(arrayAdapter1);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        eventType[0] = type.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //set time button
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                //Alarm button
                final CheckBox alarmMe = addView.findViewById(R.id.alarmMe);
                calendar.setTime(dates.get(i));
                alarmYear = calendar.get(Calendar.YEAR);
                alarmMonth = calendar.get(Calendar.MONTH);
                alarmDay = calendar.get(Calendar.DAY_OF_MONTH);
                alarmHour = calendar.get(Calendar.HOUR_OF_DAY);
                alarmMinute = calendar.get(Calendar.MINUTE);
                Button AddEvent = addView.findViewById(R.id.addevent);
                setTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY,i);
                                        c.set(Calendar.MINUTE,i1);
                                        c.setTimeZone(TimeZone.getDefault());
                                        String event_Time = hformat.format(c.getTime());
                                        EventTime.setText(event_Time);
                                        alarmHour = c.get(Calendar.HOUR_OF_DAY);
                                        alarmMinute = c.get(Calendar.MINUTE);
                                    }
                                },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormat.format(dates.get(i));
                final String month = monthFormat.format(dates.get(i));
                final String year = yearFormat.format(dates.get(i));
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alarmMe.isChecked()){
                            if (eventdistance.getText().toString().equals("")){
                                Toast.makeText(context, R.string.alert1, Toast.LENGTH_SHORT).show();
                            }else if(duration.getRawText().equals("")) {
                                Toast.makeText(context, R.string.alert2, Toast.LENGTH_SHORT).show();
                            }else{
                                Calendar calendar1 = Calendar.getInstance(Locale.ENGLISH);
                                calendar1.setTime(dates.get(i));
                                String week = calendar1.get(Calendar.WEEK_OF_YEAR) + "";
                                SaveEvent(eventName.getText().toString(), EventTime.getText().toString(), date, month, year,
                                        eventdistance.getText().toString(), duration.getRawText(), eventType[0],
                                        eventFeel[0], week, NOTI_ON);
                                SetUpCalendar();
                                //set alarm
                                Calendar _calendar = Calendar.getInstance();
                                _calendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinute);
                                setAlarm(_calendar, eventName.getText().toString(),EventTime.getText().toString(),
                                        getRequestCode(date, eventName.getText().toString(),EventTime.getText().toString()));
                                //
                                Toast.makeText(context, R.string.confirm_alarm, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                        else {
                            if (eventdistance.getText().toString().equals("")){
                                Toast.makeText(context, R.string.alert1, Toast.LENGTH_SHORT).show();
                            }else if(duration.getRawText().equals("")) {
                                Toast.makeText(context, R.string.alert2, Toast.LENGTH_SHORT).show();
                            }else{
                                Calendar calendar1 = Calendar.getInstance(Locale.ENGLISH);
                                calendar1.setTime(dates.get(i));
                                String week = calendar1.get(Calendar.WEEK_OF_YEAR) + "";
                                SaveEvent(eventName.getText().toString(), EventTime.getText().toString(), date, month, year,
                                        eventdistance.getText().toString(), duration.getRawText(), eventType[0],
                                        eventFeel[0], week, NOTI_OFF);
                                SetUpCalendar();
                                alertDialog.dismiss();
                            }
                        }

                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date = eventDateFormat.format(dates.get(i));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.show_events_layout,null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(),
                        CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SetUpCalendar();
                    }
                });
                return true;
            }
        });

        FAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View adapterView) {
                //return to current month
                calendar = Calendar.getInstance(Locale.ENGLISH); // mandatory
                SetUpCalendar();
                //
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(adapterView.getContext())
                        .inflate(R.layout.add_newevent_layout,null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                final MaskEditText duration = addView.findViewById(R.id.edittime);
                final EditText eventdistance = addView.findViewById(R.id.eventdistance);
                final Spinner spinnerType = addView.findViewById(R.id.eventtype);
                final SeekBar seekBarFeel = addView.findViewById(R.id.SeekbarFeel);
                Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat hformat = new SimpleDateFormat(timePartern);
                String event_Time = hformat.format(calendar.getTime());
                EventTime.setText(event_Time);
                final String[] eventFeel = new String[1];
                eventFeel[0] = FEEL_EXCELLENT;
                final String[] eventType = new String[1];
                seekBarFeel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        setFeel(i,eventFeel,seekBar);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                spinnerType.setAdapter(arrayAdapter1);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        eventType[0] = type.get(i);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                //Alarm button
                final CheckBox alarmMe = addView.findViewById(R.id.alarmMe);
                calendar.setTime(calendar.getTime());
                alarmYear = calendar.get(Calendar.YEAR);
                alarmMonth = calendar.get(Calendar.MONTH);
                alarmDay = calendar.get(Calendar.DAY_OF_MONTH);
                alarmHour = calendar.get(Calendar.HOUR_OF_DAY);
                alarmMinute = calendar.get(Calendar.MINUTE);
                Button AddEvent = addView.findViewById(R.id.addevent);
                setTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar1 = Calendar.getInstance();
                        int hours = calendar1.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar1.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY,i);
                                        c.set(Calendar.MINUTE,i1);
                                        c.setTimeZone(TimeZone.getDefault());
                                        String event_Time = hformat.format(c.getTime());
                                        EventTime.setText(event_Time);
                                        alarmHour = c.get(Calendar.HOUR_OF_DAY);
                                        alarmMinute = c.get(Calendar.MINUTE);
                                    }
                                },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormat.format(calendar.getTime());
                final String month = monthFormat.format(calendar.getTime());
                final String year = yearFormat.format(calendar.getTime());
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alarmMe.isChecked()){
                            if (eventdistance.getText().toString().equals("")) {
                                Toast.makeText(context, R.string.alert1, Toast.LENGTH_SHORT).show();
                            } else if (duration.getRawText().equals("")) {
                                Toast.makeText(context, R.string.alert2, Toast.LENGTH_SHORT).show();
                            } else {
                                Calendar calendar1 = Calendar.getInstance();
//                                calendar1.setTime(calendar.getTime());
                                String week = calendar1.get(Calendar.WEEK_OF_YEAR) + "";
                                SaveEvent(eventName.getText().toString(), EventTime.getText().toString(), date, month, year,
                                        eventdistance.getText().toString(), duration.getRawText(), eventType[0],
                                        eventFeel[0], week, NOTI_ON);
                                SetUpCalendar();
                                //set alarm
                                Calendar _calendar = Calendar.getInstance();
                                _calendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinute);
                                setAlarm(_calendar, eventName.getText().toString(),EventTime.getText().toString(),
                                        getRequestCode(date, eventName.getText().toString(),EventTime.getText().toString()));
                                //
                                Toast.makeText(context, R.string.confirm_alarm, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        } else {
                            if (eventdistance.getText().toString().equals("")) {
                                Toast.makeText(context, R.string.alert1, Toast.LENGTH_SHORT).show();
                            } else if (duration.getRawText().equals("")) {
                                Toast.makeText(context, R.string.alert2, Toast.LENGTH_SHORT).show();
                            } else {
                                Calendar calendar1 = Calendar.getInstance();
//                                calendar1.setTime(calendar.getTime());
                                String week = calendar1.get(Calendar.WEEK_OF_YEAR) + "";
                                SaveEvent(eventName.getText().toString(), EventTime.getText().toString(), date, month, year,
                                        eventdistance.getText().toString(), duration.getRawText(), eventType[0],
                                        eventFeel[0], week, NOTI_OFF);
                                SetUpCalendar();
                                alertDialog.dismiss();
                            }
                        }
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private ArrayList<Events> CollectEventByDate(String _date){
        ArrayList<Events> events = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(_date,database);
        while(cursor.moveToNext()){
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
            Events events1 = new Events(event,time,date,month,year,distance,duration,type,feel,week,id);
            events.add(events1);
        }
        cursor.close();
        dbOpenHelper.close();
        return events;
    }

    private int getRequestCode(String _date, String event, String time){
        int code = 0;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(_date, event, time, database);
        while(cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();
        return  code;
    }

    public void setAlarm(Calendar calendar, String event, String time, int requestCode){
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", event);
        intent.putExtra("time", time);
        intent.putExtra("id", requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(String event,String time,String date,String month,String year,String distance,String duration,
                           String type,String feel,String week, String noti){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, distance, duration, type, feel, week, noti, database);
        dbOpenHelper.close();
    }
    private void InitializeLayout(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.calendar_layout,this);
        nextButton = view.findViewById(R.id.nextBtn);
        previousButton = view.findViewById(R.id.previousBtn);
        FAB = view.findViewById(R.id.fab);
        currentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);
        type = new ArrayList<>();
        type.add(TYPE_BASE);
        type.add(TYPE_FARTLEK);
        type.add(TYPE_TEMPO);
        type.add(TYPE_INTERVALS);
        type.add(TYPE_EASY);
        type.add(TYPE_RECOVERY);
        type.add(TYPE_RACE);
        type.add(TYPE_WORKOUT);
        type.add(TYPE_LONG_RUN);
        arrayAdapter1 = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,type);
    }

    public static void SetUpCalendar(){
        String currentMonth = monthFormat.format(calendar.getTime());
        String currentYear = yearFormat.format(calendar.getTime());
        monthConfig(currentDate, currentMonth);
        currentDate.append(" " + currentYear);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 0);
        int firstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 2;
        Toast.makeText(context, ""+firstDayofMonth, Toast.LENGTH_SHORT).show();
        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
        while(dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            //1 day step between days
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }
        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }

    private static void CollectEventsPerMonth(String Month, String Year){
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsPerMonth(Month, Year, database);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
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
            Events events = new Events(event, time, date, month, year, distance, duration, type, feel, week, id);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }

    void setFeel(int i,String[] eventFeel,SeekBar seekBar){
        if(i == 0){
            seekBar.setBackgroundColor(context.getResources().getColor(R.color.red));
            eventFeel[0] = FEEL_EXHAUSTED;
        }
        if(i == 1){
            seekBar.setBackgroundColor(context.getResources().getColor(R.color.orange));
            eventFeel[0] = FEEL_TIRED;
        }
        if(i == 2){
            seekBar.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            eventFeel[0] = FEEL_GOOD;
        }
        if(i == 3){
            seekBar.setBackgroundColor(context.getResources().getColor(R.color.green));
            eventFeel[0] = FEEL_GREAT;
        }
        if(i == 4){
            seekBar.setBackgroundColor(context.getResources().getColor(R.color.sky));
            eventFeel[0] = FEEL_EXCELLENT;
        }
    }

    public static void monthConfig(TextView tv, String month){
        switch (month){
            case JAN:
                tv.setText(context.getResources().getString(R.string.jan));
                break;
            case FEB:
                tv.setText(context.getResources().getString(R.string.feb));
                break;
            case MAR:
                tv.setText(context.getResources().getString(R.string.mar));
                break;
            case APR:
                tv.setText(context.getResources().getString(R.string.apr));
                break;
            case MAY:
                tv.setText(context.getResources().getString(R.string.may));
                break;
            case JUN:
                tv.setText(context.getResources().getString(R.string.jun));
                break;
            case JUL:
                tv.setText(context.getResources().getString(R.string.jul));
                break;
            case AUG:
                tv.setText(context.getResources().getString(R.string.aug));
                break;
            case SEP:
                tv.setText(context.getResources().getString(R.string.sep));
                break;
            case OCT:
                tv.setText(context.getResources().getString(R.string.oct));
                break;
            case NOV:
                tv.setText(context.getResources().getString(R.string.nov));
                break;
            case DEC:
                tv.setText(context.getResources().getString(R.string.dec));
                break;
                default:
                    tv.setText("");

        }
    }
}
