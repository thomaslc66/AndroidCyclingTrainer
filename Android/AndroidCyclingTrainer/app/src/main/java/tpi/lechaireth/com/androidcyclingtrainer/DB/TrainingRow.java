package tpi.lechaireth.com.androidcyclingtrainer.DB;

import io.realm.RealmObject;

/**
 * Created by Thomas on 01.05.2015.
 */
public class TrainingRow extends RealmObject {

    //id for the row
    private int int_id;
    // Time for the display
    private String str_time;
    //minutes for the timer
    private int int_min;
    //seconds for the timer
    private int int_sec;
    //Revolution per minute
    private int int_rpm;
    //Hear Beat per minute
    private int int_bpm;
    //Type of Work
    private String str_work;
    //intancity of the work
    private String str_rythm;
    //String for the front and back plates
    private String str_gear;
    //time for the recuperation
    private String str_time_recup;
    //minutes fot the recup time
    private int int_min_recup;
    //seconds for the recup time
    private int int_sec_recup;
    //String for the notes
    private String str_note;


    /* Getters and Setters generated automaticaly */

    public String getStr_gear() {return str_gear;}

    public void setStr_gear(String str_gear) {this.str_gear = str_gear;}

    public int getInt_bpm() {return int_bpm;}

    public void setInt_bpm(int int_bpm) {this.int_bpm = int_bpm; }

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public int getInt_min() {
        return int_min;
    }

    public void setInt_min(int int_min) {
        this.int_min = int_min;
    }

    public int getInt_min_recup() {
        return int_min_recup;
    }

    public void setInt_min_recup(int int_min_recup) {
        this.int_min_recup = int_min_recup;
    }

    public int getInt_rpm() {
        return int_rpm;
    }

    public void setInt_rpm(int int_rpm) {
        this.int_rpm = int_rpm;
    }

    public int getInt_sec() {
        return int_sec;
    }

    public void setInt_sec(int int_sec) {
        this.int_sec = int_sec;
    }

    public int getInt_sec_recup() {
        return int_sec_recup;
    }

    public void setInt_sec_recup(int int_sec_recup) {
        this.int_sec_recup = int_sec_recup;
    }

    public String getStr_note() {
        return str_note;
    }

    public void setStr_note(String str_note) {
        this.str_note = str_note;
    }

    public String getStr_rythm() {
        return str_rythm;
    }

    public void setStr_rythm(String str_rythm) {
        this.str_rythm = str_rythm;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_time_recup() {
        return str_time_recup;
    }

    public void setStr_time_recup(String str_time_recup) {
        this.str_time_recup = str_time_recup;
    }

    public String getStr_work() {
        return str_work;
    }

    public void setStr_work(String str_work) {
        this.str_work = str_work;
    }
}
