package com.example.datn_tuandm_1534560;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.datn_tuandm_1534560.DBStructure.WEEK_TOTAL;


public class DBOpenHelper extends SQLiteOpenHelper {

    private final static String CREAT_EVENTS_TABLE = "create table if not exists "+DBStructure.EVENT_TABLE_NAME+"(" +
            DBStructure.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBStructure.EVENT + " TEXT, "+DBStructure.TIME + " TEXT," +
            DBStructure.DATE + " TEXT,"+DBStructure.MONTH + " TEXT," +
            DBStructure.YEAR + " TEXT ,"+DBStructure.DISTANCE + " DOUBLE," +
            DBStructure.DURATION + " TEXT, "+DBStructure.TYPE + " TEXT," +
            DBStructure.FEEL + " TEXT," + DBStructure.WEEK_OF_YEAR+" TEXT," +
            DBStructure.NOTIFICATION + " TEXT)";
    private static final String DROP_EVENTS_TABLE = "drop table if exists " +DBStructure.EVENT_TABLE_NAME;
    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAT_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public  void SaveEvent(String event, String time, String date, String month, String year, String distance, String duration,
                            String type, String feel, String week, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT, event);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        contentValues.put(DBStructure.DISTANCE, distance);
        contentValues.put(DBStructure.DURATION,duration);
        contentValues.put(DBStructure.TYPE, type);
        contentValues.put(DBStructure.FEEL, feel);
        contentValues.put(DBStructure.WEEK_OF_YEAR,week);
        db.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues);
    }

    public Cursor ReadEvents(String date,SQLiteDatabase db){
        String [] Projections = {DBStructure.ID, DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR,
                DBStructure.DISTANCE, DBStructure.DURATION,DBStructure.TYPE,DBStructure.FEEL,DBStructure.WEEK_OF_YEAR};
        String Selection = DBStructure.DATE + "=?";
        String [] SelectionArgs = {date};
        return db.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public Cursor ReadIDEvents(String date, String event, String time, SQLiteDatabase db){
        String [] Projections = {DBStructure.ID,DBStructure.NOTIFICATION};
        String Selection = DBStructure.DATE + "=? and " + DBStructure.EVENT +"=? and " + DBStructure.TIME + "=?";
        String [] SelectionArgs = {date, event, time};
        return db.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public Cursor ReadAllEvents(SQLiteDatabase db){
        String [] Projections = {DBStructure.ID,DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR,
                DBStructure.DISTANCE,DBStructure.DURATION,DBStructure.TYPE,DBStructure.FEEL,DBStructure.WEEK_OF_YEAR};

        return db.query(DBStructure.EVENT_TABLE_NAME,Projections,null,null,null,null,
                DBStructure.DATE + " desc limit 12");
    }

    public Cursor SelectAllByWeek(SQLiteDatabase database){
        return database.rawQuery("select *,sum("+DBStructure.DISTANCE+") as "+ WEEK_TOTAL +" from " + DBStructure.EVENT_TABLE_NAME +
                " group by "+DBStructure.WEEK_OF_YEAR+" order by "+DBStructure.WEEK_OF_YEAR+" desc limit 8",null);
    }

    public Cursor ReadEventsPerMonth(String month,String year,SQLiteDatabase db){
        String [] Projections = {DBStructure.ID,DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR,
                DBStructure.DISTANCE,DBStructure.DURATION,DBStructure.TYPE,DBStructure.FEEL,DBStructure.WEEK_OF_YEAR};
        String Selection = DBStructure.MONTH + "=? and " +DBStructure.YEAR+"=?";
        String [] SelectionArgs = {month,year};
        return db.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public void deleteEvent(String event,String date,String time,SQLiteDatabase database){
        String selection = DBStructure.EVENT+"=? and "+DBStructure.DATE+"=? and "+DBStructure.TIME+"=?";
        String [] selectionArg = {event,date,time};
        database.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArg);

    }

    public void deleteEventByID(String id, SQLiteDatabase database){
        database.execSQL("delete from " +DBStructure.EVENT_TABLE_NAME+" where ID = " + id);
    }

    public void editEventByID(String id,String name,String distance,String duration,String type,String feel,
                              String time,SQLiteDatabase database) {
        database.execSQL("update " +DBStructure.EVENT_TABLE_NAME+" set "+ DBStructure.EVENT +" = '"+name+
                "',"+ DBStructure.DISTANCE +" = "+ distance +","+ DBStructure.DURATION +" = '"+ duration +
                "',"+DBStructure.TYPE+" = '"+ type +"',"+ DBStructure.FEEL +" = '"+ feel +"',"+DBStructure.TIME+" = '"+ time +
                "' where ID = " + id);
    }
}
