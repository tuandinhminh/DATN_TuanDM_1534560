package com.example.datn_tuandm_1534560;

public class Events {
    String EVENT, TIME, DATE, MONTH, YEAR,DURATION,TYPE,FEEL,WEEK,NOTI;
    int ID;
    double DISTANCE;

    public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR, double DISTANCE,
                  String DURATION, String TYPE, String FEEL, String WEEK, int ID, String NOTI ) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
        this.DURATION = DURATION;
        this.TYPE = TYPE;
        this.FEEL = FEEL;
        this.WEEK = WEEK;
        this.NOTI = NOTI;
        this.ID = ID;
        this.DISTANCE = DISTANCE;
    }

    public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR, double DISTANCE
            , String DURATION, String TYPE, String FEEL, String WEEK, int ID) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
        this.DISTANCE = DISTANCE;
        this.DURATION = DURATION;
        this.TYPE = TYPE;
        this.FEEL = FEEL;
        this.WEEK = WEEK;
        this.ID = ID;
    }

    public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR, double DISTANCE
            , String DURATION, String TYPE, String FEEL) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
        this.DISTANCE = DISTANCE;
        this.DURATION = DURATION;
        this.TYPE = TYPE;
        this.FEEL = FEEL;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public double getDISTANCE() {
        return DISTANCE;
    }

    public void setDISTANCE(double DISTANCE) {
        this.DISTANCE = DISTANCE;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getFEEL() {
        return FEEL;
    }

    public void setFEEL(String FEEL) {
        this.FEEL = FEEL;
    }

    public String getWEEK() {
        return WEEK;
    }

    public void setWEEK(String WEEK) {
        this.WEEK = WEEK;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOTI() {
        return NOTI;
    }

    public void setNOTI(String NOTI) {
        this.NOTI = NOTI;
    }
}
