package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;

/**
 * Created by Thomas on 23.05.2015.
 */
public class Timer extends Activity {

    //UI Elements
    private ProgressBar prBar_total, prBar_row;
    private TextView txtView_min, txtView_sec, txtView_timRow,txtView_rythm_value ,txtView_work_value ,txtView_bpm_value, txtView_rpm_value,txtView_milis;
    private Button btn_start, btn_stop, btn_pause;

    //Object
    private List<TrainingRow> lst_trainingRow;
    private RealmDB realmDB;
    private List<Integer> lst_time;
    /* test with handler */
    private Handler handler = new Handler();
    private TimerRunnable runnable = new TimerRunnable();
/*    private TimerCountDown timerCountDown2;
    private TimerCountDown timerCountDown;*/

    //VARIABLES
    /* Int to translate seconds in milliseconds */
    private static int INT_MILLIS = 1000;
    private int _id, int_total_min, int_total_sec;
    /* time of the first row */
    private int int_time_first_row;
    /* total time in milliseconds */
    private int int_total_timeInMillis;
    /* boolean if the time isCanceled or isPaused */
    private boolean isPaused = false;
    private boolean isCanceled = false;

    //variable to backup Timer progression
    /* variable to hold prBar_total value */
    int int_totalBar_value;
    /* variable to hold prBar_row value */
    int int_rowBar_value;
    /* variable to hold the value of i */
    int int_i;
    int int_total_time;
    int int_row_time;

    int test = 0;




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
        txtView_milis = (TextView) findViewById(R.id.txtView_milis);

        prBar_row = (ProgressBar) findViewById(R.id.progressBar_row);
        prBar_total = (ProgressBar) findViewById(R.id.progressBar_total);

        //get the id of the training from the last actvity
        _id = getIntent().getIntExtra("_id", 0);

        //get from RealmDB total time and all Rows
        realmDB = new RealmDB(Timer.this);
        //get time
        lst_time = realmDB.calculateTotalMinAndSec(_id);
        //get all Rows
        lst_trainingRow = realmDB.getAllTrainingRows(_id);

        //set the value for the first row
        //time for the row
        txtView_timRow.setText(lst_trainingRow.get(0).getStr_time());
        //value for the row
        txtView_rpm_value.setText(lst_trainingRow.get(0).getInt_rpm()+""); //rpm
        txtView_bpm_value.setText(lst_trainingRow.get(0).getInt_bpm()+""); //bpm
        txtView_rythm_value.setText(lst_trainingRow.get(0).getStr_rythm()); //rythme
        txtView_work_value.setText(lst_trainingRow.get(0).getStr_work()); //work

        //get total time in miliseconds
        int_total_timeInMillis = ((lst_time.get(0) * 60) + lst_time.get(1)) * INT_MILLIS;

        //get time of the first row of training
        int_time_first_row = ((lst_trainingRow.get(0).getInt_min()*60) + lst_trainingRow.get(0).getInt_sec()* INT_MILLIS);

        //disable Buttons
        btn_stop.setEnabled(false);
        btn_pause.setEnabled(false);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set both check values to false
                isPaused = false;
                isCanceled = false;

                handler.postDelayed(runnable,1000);



                if (btn_start.getText().equals("Départ")){
                    //enable/disable button
                    btn_start.setEnabled(false);
                    btn_pause.setEnabled(true);
                    btn_stop.setEnabled(true);

                    //create a new timer - TimerCountDown(total time, Intervals, id of the row, value of prBar_Total, value of prBar_row)
                    //timerCountDown = new TimerCountDown(int_total_timeInMillis, 100, 0 , 0 ,0);
                    //start CountDown
                    //timerCountDown.start();


                }else{ //part for the resume button
                    btn_start.setEnabled(false);
                    btn_start.setText("Départ");

                    //countDown reRun so we enable pause or stop
                    btn_pause.setEnabled(true);
                    btn_stop.setEnabled(true);

                    //create a new timer - TimerCountDown(total time, Intervals, id of the row, value of prBar_Total, value of prBar_row)
                    //new TimerCountDown(int_total_timeInMillis, 100, int_i , int_totalBar_value, int_rowBar_value).start();
                }

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set isPaused to true
                isPaused = true;

                runnable.killRunnable();
                handler.removeCallbacks(runnable);


                //enable resume button. disable Pause button
                /* change text of the button start. This way he become the resume button */
                btn_start.setText("Reprise");
                btn_start.setEnabled(true);
                btn_stop.setEnabled(true);
                btn_pause.setEnabled(false);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set isCanceled to true. Timer will be stopped
                isCanceled = true;

                runnable.killRunnable();
                handler.removeCallbacks(runnable);



                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_pause.setEnabled(false);

                txtView_milis.setText("CountDownTimer Canceled/Stopped");
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private class TimerRunnable implements Runnable {



        @Override
        public void run() {
                Log.w("KILLME", isCanceled + "");

                if (isCanceled == true) {
                    runnable = null;
                    return;
                } else {
                    test++;
                    txtView_milis.setText("" + test);

                }

            handler.postDelayed(this, 1000);
            //Toast.makeText(getBaseContext(), "test3 " + test, Toast.LENGTH_SHORT).show();
        }

        public void killRunnable(){
            isCanceled = true;
        }
    }






    /***
     * Private class TimerCountDown
     * extends CountDownTimer class
     */
    /*private class TimerCountDown extends CountDownTimer{
        //VARIABLES

        *//* variable to hold the value of the Intervals *//*
        final int INTERVALS;
        *//* variable to hold the value of the total of time *//*
        long lng_total_time;
        *//* variable tho hold total time progression*//*
        int int_progress_totalTime;
        *//* variable to hold the progress of the row time *//*
        int int_progress_rowTime;
        *//* variable to hold the id of the current row *//*
        int i;
        *//* variable to hold the time of the row *//*
        int int_time_row;
        *//* variable to hold the time only for display *//*
        int int_rowTime_display;

        //variables to create a TrainingRow
        int int_min, int_sec, int_rpm, int_bpm;
        String str_gear, str_time, str_work, str_rythm, str_note;

        *//* constructor *//*
        public TimerCountDown(long millisInFuture, int Intervals, int int_i, int prBar_total_progress, int prBar_row_progress ) {
            super(millisInFuture, Intervals);
            //set intervals
            this.INTERVALS = Intervals;
            //set total time
            this.lng_total_time = millisInFuture;

            //set time for the first row to total time of the row - time already done
            this.int_time_row = ((lst_trainingRow.get(int_i).getInt_min()*60) + lst_trainingRow.get(int_i).getInt_sec()* INT_MILLIS);

            //set the time for the countDown display. need to add 1000 second to match the progression of progressBAr
            this.int_rowTime_display = int_time_row-prBar_row_progress + 1000;

            //set i
            this.i = int_i;
            Log.w("total - row", lng_total_time + " - " + int_time_row);

            //the time progession equals the progress bar progression
            //if it's the start progression is equals to zero
            this.int_progress_totalTime =  prBar_total_progress;
            this.int_progress_rowTime = prBar_row_progress ;

            prBar_total.setMax((int) lng_total_time);
            prBar_row.setMax(int_time_row);

            //set progress of ProgressBar
            prBar_total.setProgress(prBar_total_progress);
            prBar_row.setProgress(prBar_row_progress);

        }

        @Override
        public void onTick ( long millisUntilFinished) {

            //stop onTick when millisUntilFinished is equal to zero
            if(int_progress_totalTime == lng_total_time) {
                Log.w("millis = time", +millisUntilFinished + "=" + lng_total_time);
                if (isPaused == true || isCanceled == true) {
                    //if pause or cancel = true, cancel the timer
                    this.cancel();

                } else {
                    txtView_milis.setText("" + millisUntilFinished / 1000 + " - " + "i = " + i);
                    //check if prBar_row is finish
                    Log.w("progress_row = time_row", +int_progress_rowTime + "=" + int_time_row);
                    if (int_progress_rowTime == int_time_row) {
                        //try to get All training Row
                        lst_trainingRow = realmDB.getAllTrainingRows(_id);

                        Log.w("I BEFORE ", "" + i);

                        //this if control the incrementation of i
                        if (i < lst_trainingRow.size() - 1) {
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
                            int_time_row = ((int_min * 60) + int_sec * INT_MILLIS);

                            *//* only for the display we had 1000 millis.
                             like this the bar is full *//*
                            int_rowTime_display = int_time_row + 1000;

                            //change max value
                            prBar_row.setMax(int_time_row);

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
                            int_progress_rowTime = 0;
                        }
                    }//if


                    //progress of total and row ProgressBar
                    prBar_total.setProgress(int_progress_totalTime += INTERVALS);
                    prBar_row.setProgress(int_progress_rowTime += INTERVALS);

                    //update the time in the screen for the countdown
                    int_rowTime_display -= INTERVALS;
                    //get the time in seconds by dividing by 1000
                    int row_min = int_rowTime_display / INT_MILLIS;
                    //divide row_min by 60 to get minutes. and row_min % 60 to get seconds
                    txtView_timRow.setText("" + row_min / 60 + ":" + row_min % 60);

                    //backup prBar_total progression
                    int_totalBar_value = int_progress_totalTime - INTERVALS;
                    //backup prBar_row progression
                    int_rowBar_value = int_progress_rowTime - INTERVALS;
                    //backup i
                    int_i = i;

                }
            }//if
        }//onTick

        @Override
        public void onFinish() {
            btn_pause.setEnabled(false);
            btn_stop.setEnabled(false);
            btn_start.setEnabled(false);

            //launch alert dialog
        }
    }//TimerCountDown
*/
}//Class Timer
