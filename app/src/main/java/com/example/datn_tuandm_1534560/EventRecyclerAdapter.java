package com.example.datn_tuandm_1534560;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.example.datn_tuandm_1534560.CustomCalendarView.SetUpCalendar;
import static com.example.datn_tuandm_1534560.CustomCalendarView.alertDialog;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;

    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_envents_row_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Events events = arrayList.get(i);
        //su kien sua
        myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(view.getContext())
                        .inflate(R.layout.edit_event_layout,null);
                final EditText eventName = addView.findViewById(R.id.eventname);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                final MaskEditText duration = addView.findViewById(R.id.edittime);
                final EditText eventdistance = addView.findViewById(R.id.eventdistance);
                final Spinner spinnerType = addView.findViewById(R.id.eventtype);
                final SeekBar seekBarFeel = addView.findViewById(R.id.SeekbarFeel);

                //lay gia tri dua len giao dien
                eventName.setText(events.getEVENT());
                EventTime.setText(events.getTIME());
                duration.setText(events.getDURATION());
                eventdistance.setText(events.getDISTANCE()+"");
                final String[] eventFeel = new String[1];
                eventFeel[0]  = events.getFEEL();
                if(eventFeel[0].equals("Rất mệt")){
                    seekBarFeel.setProgress(0);
                    seekBarFeel.setBackgroundColor(Color.RED);
                }
                if(eventFeel[0].equals("Hơi mệt")){
                    seekBarFeel.setProgress(1);
                    seekBarFeel.setBackgroundColor(Color.parseColor("#fa6c00"));
                }
                if(eventFeel[0].equals("Bình thường")){
                    seekBarFeel.setProgress(2);
                    seekBarFeel.setBackgroundColor(Color.YELLOW);
                }
                if(eventFeel[0].equals("Khỏe")){
                    seekBarFeel.setProgress(3);
                    seekBarFeel.setBackgroundColor(Color.GREEN);
                }
                if(eventFeel[0].equals("Rất khỏe")){
                    seekBarFeel.setProgress(4);
                    seekBarFeel.setBackgroundColor(Color.parseColor("#00bcd4"));
                }
                final String[] eventType = new String[1];
                eventType[0] = events.getTYPE();
                myViewHolder.type.set(0,eventType[0]);
                for (int i = 1;i < myViewHolder.type.size();i++){
                    if (myViewHolder.type.get(i).equals(eventType[0])){
                        myViewHolder.type.remove(i);
                    }
                }

                seekBarFeel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if(i == 0){
                            seekBar.setBackgroundColor(Color.RED);
                            eventFeel[0] = "Rất mệt";
                        }
                        if(i == 1){
                            seekBar.setBackgroundColor(Color.parseColor("#fa6c00"));
                            eventFeel[0] = "Hơi mệt";
                        }
                        if(i == 2){
                            seekBar.setBackgroundColor(Color.YELLOW);
                            eventFeel[0] = "Bình thường";
                        }
                        if(i == 3){
                            seekBarFeel.setBackgroundColor(Color.GREEN);
                            eventFeel[0] = "Khỏe";
                        }
                        if(i == 4){
                            seekBarFeel.setBackgroundColor(Color.parseColor("#00bcd4"));
                            eventFeel[0] = "Rất khỏe";
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                spinnerType.setAdapter(myViewHolder.arrayAdapter1);
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        eventType[0] = myViewHolder.type.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                ImageButton setTime = addView.findViewById(R.id.seteventtime);
                Button EditEvent = addView.findViewById(R.id.editevent);
                setTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
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
                                        SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String event_Time = hformat.format(c.getTime());
                                        EventTime.setText(event_Time);
                                    }
                                },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });
                EditEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (eventdistance.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn phải điền quãng đường", Toast.LENGTH_SHORT).show();
                        }else if(duration.getRawText().toString().equals("")) {
                            Toast.makeText(context, "Bạn phải điền thời gian", Toast.LENGTH_SHORT).show();
                        }else{
                            editCalendarEvent(events.getID()+"",eventName.getText().toString(),eventdistance.getText().toString(),
                                    duration.getRawText(), eventType[0], eventFeel[0], EventTime.getText().toString());
                            myViewHolder.alertDialog1.dismiss();
                            alertDialog.dismiss();
                            SetUpCalendar();
                        }
                    }
                });

                builder.setView(addView);
                myViewHolder.alertDialog1 = builder.create();
                myViewHolder.alertDialog1.show();

            }
        });
        myViewHolder.Event.setText(R.string.run_name+events.getEVENT()+ "-"+ events.getWEEK());
        FeelColors(events.getFEEL(),myViewHolder.Event);
        myViewHolder.DateTxt.setText(events.getDATE());
        myViewHolder.Time.setText(R.string.time+events.getTIME());
        myViewHolder.Distance.setText(R.string.distance+events.getDISTANCE() + " km");

        //chuyen doi thoi gian
        int duration1 = Integer.parseInt(events.getDURATION());
        double distance1 = events.getDISTANCE();
        int hours = duration1/10000,
                minutes = (duration1%10000)/100,
                seconds = (duration1%10000)%100;
        if(seconds >= 60){
            minutes+=1;
            seconds-=60;
        }
        if (minutes >= 60){
            hours+=1;
            minutes-=60;
        }
        String minute = minutes+"",second = seconds+"";
        if(minutes < 10) minute = "0"+minute;
        if (seconds < 10) second = "0"+second;

        myViewHolder.Duration.setText(R.string.duration+hours+":"+minute+":"+second);
        myViewHolder.Type.setText(R.string.run_type+events.getTYPE());
        myViewHolder.Feel.setText(R.string.feel+events.getFEEL());
        //tinh toan Pace
        double pace1 = (seconds+minutes*60+hours*3600)/distance1;
        hours = 0;
        minutes = (int)pace1/60;
        seconds = (int)pace1%60;
        if (minutes >= 60){
            hours=minutes/60;
            minutes%=60;
        }
        minute = minutes+"";
        second = seconds+"";
        if(minutes < 10) minute = "0"+minute;
        if (seconds < 10) second = "0"+second;

        myViewHolder.Pace.setText("Pace : "+ hours+":"+minute+":"+second + " / km");
        //su kien xoa
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCalendarEvent(events.getID()+"");
                arrayList.remove(i);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView DateTxt,Event,Time,Distance,Duration,Type,Feel,Pace;
        Button delete,edit;
        ArrayAdapter arrayAdapter1;
        ArrayList<String>type;
        AlertDialog alertDialog1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventime);
            Distance = itemView.findViewById(R.id.eventdistance);
            Duration = itemView.findViewById(R.id.eventduration);
            Type = itemView.findViewById(R.id.eventtype);
            Feel = itemView.findViewById(R.id.eventfeel);
            Pace =itemView.findViewById(R.id.eventpace);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

            type = new ArrayList<String>();
            type.add("");
            type.add("Cruise");
            type.add("Fartlek");
            type.add("Tempo");
            type.add("Intervals");
            type.add("Easy");
            type.add("Recovery");
            type.add("Race");
            type.add("Workout");
            type.add("Long Run");
            arrayAdapter1 = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,type);
        }
    }

    private void deleteCalendarEvent(String id){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEventByID(id,database);
        dbOpenHelper.close();
    }

    private void editCalendarEvent(String id,String name,String distance,String duration,String type,
                                   String feel, String time){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.editEventByID(id, name, distance,duration,type, feel, time, database);
        dbOpenHelper.close();
    }

    private void FeelColors(String feel,TextView tv){
        switch (feel){
            case "Rất mệt":
                tv.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case "Hơi mệt":
                tv.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case "Bình thường":
                tv.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case "Khỏe":
                tv.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "Rất khỏe":
                tv.setTextColor(context.getResources().getColor(R.color.sky));
                break;
            default:
                tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
