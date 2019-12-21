package com.example.datn_tuandm_1534560;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater layoutInflater;
    public MyGridAdapter(@NonNull Context context, List<Date> dates,Calendar currentDate,List<Events> events) {
        super(context, R.layout.single_cell_layout);
        this.dates=dates;
        this.currentDate=currentDate;
        this.events = events;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime((monthDate));
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.single_cell_layout,parent,false);

        }
        TextView Day_Number = view.findViewById(R.id.calendar_day);
        TextView EventNumber = view.findViewById(R.id.events_id);
        Day_Number.setText(String.valueOf(DayNo));
        Calendar calendar1 = Calendar.getInstance();
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        //doi mau ngay hien tai

        if (displayMonth == currentMonth && displayYear == currentYear) {
            if(currentDate.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
                    && DayNo == currentDate.get(Calendar.DAY_OF_MONTH)
                    && displayMonth == calendar1.get(Calendar.MONTH)+1
                    && displayYear == calendar1.get(Calendar.YEAR)){
                Day_Number.setTextColor(getContext().getResources().getColor(R.color.sky));
                Day_Number.setPaintFlags(Day_Number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }else {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                Day_Number.setTextColor(Color.parseColor("#555555"));
                view.setClickable(false);
            }
        }
        else{
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            view.setClickable(true);
            Day_Number.setTextColor(getContext().getResources().getColor(R.color.white));
        }

        List<String> arrayList = new ArrayList<>();
        Calendar eventCalendar = Calendar.getInstance();
        for (int i = 0;i < events.size();i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));
            //kiem tra ngay co su kien co trung voi tuan hien tai hay khong
            if (DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH)
                    && calendar1.get(Calendar.WEEK_OF_YEAR) != eventCalendar.get(Calendar.WEEK_OF_YEAR)
                    && displayMonth == eventCalendar.get(Calendar.MONTH)+1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT());
                EventNumber.setText(arrayList.size()+"");
                Day_Number.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                Day_Number.setPaintFlags(Day_Number.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                FeelColors(events.get(i).getFEEL(),EventNumber);

            }
            if(DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH)
                    && calendar1.get(Calendar.WEEK_OF_YEAR) == eventCalendar.get(Calendar.WEEK_OF_YEAR)
                    && displayMonth == eventCalendar.get(Calendar.MONTH)+1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){

                arrayList.add(events.get(i).getEVENT());
                EventNumber.setText(arrayList.size()+"");
                Day_Number.setPaintFlags(Day_Number.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                FeelColors(events.get(i).getFEEL(),EventNumber);
                if (calendar1.get(Calendar.DATE) != eventCalendar.get(Calendar.DATE)){

                    Day_Number.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                }
            }

        }

        return view;
    }
    private Date ConvertStringToDate(String s){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    private void FeelColors(String feel,TextView tv){
        switch (feel){
            case "Exhausted":
                tv.setTextColor(getContext().getResources().getColor(R.color.red));
                break;
            case "Tired":
                tv.setTextColor(getContext().getResources().getColor(R.color.orange));
                break;
            case "Good":
                tv.setTextColor(getContext().getResources().getColor(R.color.yellow));
                break;
            case "Great":
                tv.setTextColor(getContext().getResources().getColor(R.color.green));
                break;
            case "Excellent":
                tv.setTextColor(getContext().getResources().getColor(R.color.sky));
                break;
            default:
                tv.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        }
    }
}
