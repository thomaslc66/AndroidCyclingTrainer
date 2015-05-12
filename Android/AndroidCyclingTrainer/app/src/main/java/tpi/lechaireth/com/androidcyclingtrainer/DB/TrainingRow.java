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
    //percent of effort
    private int int_percent;

    //getters
    public int getInt_id(){
        return int_id;
    }

    public String getStr_time() {
        return str_time;
    }

    public int getInt_min() {
        return int_min;
    }

    public int getInt_sec() {
        return int_sec;
    }

    public int getInt_rpm() {
        return int_rpm;
    }

    public int getInt_bpm() {
        return int_bpm;
    }

    public int getInt_percent() {
        return int_percent;
    }

    //setters
    public void setInt_id(int int_id){
        this.int_id = int_id;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public void setInt_min(int int_min) {
        this.int_min = int_min;
    }

    public void setInt_sec(int int_sec) {
        this.int_sec = int_sec;
    }

    public void setInt_rpm(int int_rpm) {
        this.int_rpm = int_rpm;
    }

    public void setInt_bpm(int int_bpm) {
        this.int_bpm = int_bpm;
    }

    public void setInt_percent(int int_percent) {
        this.int_percent = int_percent;
    }
}
