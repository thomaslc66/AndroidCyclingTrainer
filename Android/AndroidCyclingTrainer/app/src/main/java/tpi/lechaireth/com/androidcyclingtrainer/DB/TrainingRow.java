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
    private int int_tourpm;
    //Hear Beat per minute
    private int int_batpm;
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

    public int getInt_tourpm() {
        return int_tourpm;
    }

    public int getInt_batpm() {
        return int_batpm;
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

    public void setInt_tourpm(int int_tourpm) {
        this.int_tourpm = int_tourpm;
    }

    public void setInt_batpm(int int_batpm) {
        this.int_batpm = int_batpm;
    }

    public void setInt_percent(int int_percent) {
        this.int_percent = int_percent;
    }
}
