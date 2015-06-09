/***************************************************************
 * Programm  : Android Cycling Trainer
 * Society   : ETML
 * Author    : Thomas Léchaire
 * Date      : 26.05.2015
 * Goal      : Class used to backup in the realm data base all TrainingRow created
 *             by the user for one and only one Training.
 ******************************************************************** //
 * Modifications:
 * Date       : XX.XX.XXXX
 * Author     :
 * Purpose     :
 *********************************************************************/
package tpi.lechaireth.com.androidcyclingtrainer.DB;

/* Import for the class */
import io.realm.RealmObject;

/************************************************
 *  Begining of the TrainingRow class
 **********************************************/
public class TrainingRow extends RealmObject {

    //id for the row
    private int id;
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
    //Intansity of the work
    private String str_rythm;
    //String for the front and back plates
    private String str_gear;
    //time for the recuperation
    private String str_time_rest;
    //minutes fot the rest time
    private int int_min_rest;
    //seconds for the test time
    private int int_sec_rest;
    //String for the notes
    private String str_note;

    /* Getters and Setters for all attributs of the class */

    public String getStr_gear() {return str_gear;}

    public void setStr_gear(String str_gear) {this.str_gear = str_gear;}

    public int getInt_bpm() {return int_bpm;}

    public void setInt_bpm(int int_bpm) {this.int_bpm = int_bpm; }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getInt_min() {return int_min;}

    public void setInt_min(int int_min) {this.int_min = int_min;}

    public int getInt_min_rest() {return int_min_rest;}

    public void setInt_min_rest(int int_min_rest) {this.int_min_rest = int_min_rest;}

    public int getInt_rpm() {return int_rpm;}

    public void setInt_rpm(int int_rpm) {this.int_rpm = int_rpm;}

    public int getInt_sec() {return int_sec;}

    public void setInt_sec(int int_sec) {this.int_sec = int_sec;}

    public int getInt_sec_rest() {return int_sec_rest;}

    public void setInt_sec_rest(int int_sec_rest) {this.int_sec_rest = int_sec_rest;}

    public String getStr_note() {return str_note;}

    public void setStr_note(String str_note) {this.str_note = str_note;}

    public String getStr_rythm() {return str_rythm;}

    public void setStr_rythm(String str_rythm) {this.str_rythm = str_rythm;}

    public String getStr_time() {return str_time;}

    public void setStr_time(String str_time) {this.str_time = str_time;}

    public String getStr_time_rest() {return str_time_rest;}

    public void setStr_time_rest(String str_time_rest) {this.str_time_rest = str_time_rest;}

    public String getStr_work() {return str_work;}

    public void setStr_work(String str_work) {this.str_work = str_work;}
}
