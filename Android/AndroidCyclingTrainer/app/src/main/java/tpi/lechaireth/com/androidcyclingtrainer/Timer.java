package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.RealmList;
import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;

/**
 * Created by Thomas on 23.05.2015.
 */
public class Timer extends Activity {

    //UI Elements
    private ProgressBar prBar_total, prBar_row;
    private TextView txtView_min, txtView_sec, txtView_timRow,txtView_rythm_value ,txtView_work_value ,txtView_bpm_value, txtView_rpm_value;
    private Button btn_start, btn_stop, btn_pause;

    //Object
    private List<TrainingRow> lst_trainingRow;
    private RealmDB realmDB;
    private List<Integer> lst_time;

    private TimerCountDown timerCountDown2;
    private TimerCountDown timerCountDown;

    //VARIABLES
    private static int INT_MILLIS = 1000;
    private int _id, int_total_min, int_total_sec;
    private int int_max_row;
    private int millis;
    private boolean isClicked = false;

    //backup Variable
    int int_progress_value, int_total_time, int_progress_row_value, int_row_time, int_i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        txtView_timRow = (TextView) findViewById(R.id.txtView_rowTime);
        txtView_rythm_value = (TextView) findViewById(R.id.txtView_rythm_value);
        txtView_work_value = (TextView) findViewById(R.id.txtView_work_value);
        txtView_bpm_value = (TextView) findViewById(R.id.txtView_bpm_value);
        txtView_rpm_value = (TextView) findViewById(R.id.txtView_rpm_value);

        prBar_row = (ProgressBar) findViewById(R.id.progressBar_row);
        prBar_total = (ProgressBar) findViewById(R.id.progressBar_total);

        _id = getIntent().getIntExtra("_id", 0);

        realmDB = new RealmDB(Timer.this);
        lst_time = realmDB.calculateTotalMinAndSec(_id);
        lst_trainingRow = realmDB.getAllTrainingRows(_id);

        //set the value for the first row
        //time for the row
        txtView_timRow.setText(lst_trainingRow.get(0).getStr_time());
        //value for the row
        txtView_rpm_value.setText(lst_trainingRow.get(0).getInt_rpm()+"");
        txtView_bpm_value.setText(lst_trainingRow.get(0).getInt_bpm()+"");
        txtView_rythm_value.setText(lst_trainingRow.get(0).getStr_rythm());
        txtView_work_value.setText(lst_trainingRow.get(0).getStr_work());

        //get time in miliseconds
        millis = ((lst_time.get(0) * 60) + lst_time.get(1)) * INT_MILLIS;

        //get the first row of training just for the begining
        int_max_row = ((lst_trainingRow.get(0).getInt_min()*60) + lst_trainingRow.get(0).getInt_sec()* INT_MILLIS);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startTimer(lst_time.get(0), lst_time.get(1), int_max_row);
                if(isClicked == false) {
                    timerCountDown = new TimerCountDown(millis, 100, int_max_row, 0 , 0 ,0);
                    timerCountDown.start();
                    isClicked = true;
                }
            }
        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to pause the timer.
                timerCountDown.onPause();
                btn_start.setText("Resume");
            }
        });


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timerCountDown2 = new TimerCountDown(int_total_time, 100, int_row_time, int_i, int_progress_value, int_progress_row_value);
                Log.w("i", int_i+"");
                Log.w("int_progress_value", int_progress_value+"");
                Log.w("int_progress_row_value", int_progress_row_value+"");

                timerCountDown2 = new TimerCountDown(millis, 100, int_max_row, int_i , int_progress_value ,int_progress_row_value);
                timerCountDown2.start();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop the asyncTask if when the Activity Is Destroyed
        if (timerCountDown != null) {
            timerCountDown.cancel();
            timerCountDown = null;
        }
    }

    /***
     * Private class TimerCountDown
     * extends CountDownTimer class
     */
    private class TimerCountDown extends CountDownTimer{
        //VARIABLES
        final int INTERVALS;
        long lng_total_time;
        int int_time_row;
        int timeProgress;
        int i;
        int time_row;
        boolean isPaused = false;

        //variables to create a TrainingRow
        int int_min, int_sec, int_rpm, int_bpm;
        String str_gear, str_time, str_work, str_rythm, str_note;

        /* constructor */
        public TimerCountDown(long millisInFuture, int Intervals, int int_max_row, int int_i, int prBar_total_progress, int prBar_row_progress ) {
            super(millisInFuture, Intervals);
            //set intervals
            this.INTERVALS = Intervals;
            //set total time
            this.lng_total_time = millisInFuture - prBar_total_progress;
            //set time for the first row
            this.int_time_row = (int_max_row + 900) - prBar_row_progress;
            //set i
            this.i = int_i;

            //set progress of ProgressBar
            prBar_total.setProgress(prBar_total_progress);
            prBar_row.setProgress(prBar_row_progress);

            //set timeProgress and time_row
            this.timeProgress =  0;
            this.time_row = 0 ;

        }

        @Override
        public void onTick ( long millisUntilFinished) {

            if (millisUntilFinished > 0) {
                Log.w("TIMEPROG", "" + timeProgress + "==" + lng_total_time);
                //add one second
                prBar_total.setProgress(timeProgress += INTERVALS);


                Log.w("TEST IF", "" + time_row + "==" + int_max_row);


                //check if prBar_row is done
                if (time_row == int_max_row) {
                    //try to get All training Row
                    lst_trainingRow = realmDB.getAllTrainingRows(_id);

                    Log.w("I BEFORE ", "" + i);
                    //increment i
                    i++;

                    Log.w("I AFTER ", "" + i);
                    //create Training Row
                    int_min = lst_trainingRow.get(i).getInt_min();
                    int_sec = lst_trainingRow.get(i).getInt_sec();
                    int_rpm = lst_trainingRow.get(i).getInt_rpm();
                    int_bpm = lst_trainingRow.get(i).getInt_bpm();
                    str_gear = lst_trainingRow.get(i).getStr_gear();
                    str_time = lst_trainingRow.get(i).getStr_time();
                    str_rythm = lst_trainingRow.get(i).getStr_note();
                    str_work = lst_trainingRow.get(i).getStr_work();
                    str_note = lst_trainingRow.get(i).getStr_work();

                    //set new max time for the row
                    int_max_row = ((int_min * 60) + int_sec * INT_MILLIS);
                        /*this line set int_time_row to int_max_row
                        because int_time_row is used for the countDown */
                    int_time_row = int_max_row + 900;
                    //change max value
                    prBar_row.setMax(int_max_row);

                    //on change set new value for the row
                    txtView_timRow.setText(str_time);
                    //value for the row
                    txtView_rpm_value.setText(int_rpm + "");
                    txtView_bpm_value.setText(int_bpm + "");
                    txtView_rythm_value.setText(str_rythm);
                    txtView_work_value.setText(str_work);
                    //note and gear missing

                    //reset Progress to 0
                    prBar_row.setProgress(0);

                    //reset time row to 0;
                    time_row = 0;
                }//if

                //progress of the row bar
                prBar_row.setProgress(time_row += INTERVALS);

                //update the time in the screen
                int_time_row -= INTERVALS;
                int row_min = int_time_row / INT_MILLIS;
                txtView_timRow.setText("" + row_min / 60 + ":" + row_min % 60);

                //backup prBar_total progression
                int_progress_value = timeProgress-INTERVALS;
                //backup prBar_row progression
                int_progress_row_value = time_row-INTERVALS;
                //backup i
                int_i = i;

            }
        }//onTick

        public void onPause(){
            //cancel the timer
            if(timerCountDown!=null && isPaused == false){
                timerCountDown.cancel();
                timerCountDown = null;
                isPaused = true;
            }
        }

        @Override
        public void onFinish() {
            txtView_timRow.setText("fini");
        }
    }//TimerCountDown

}//Class Timer
