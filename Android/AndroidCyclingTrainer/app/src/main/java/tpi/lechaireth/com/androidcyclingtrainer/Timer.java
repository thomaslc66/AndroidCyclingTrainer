package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;

/**
 * Created by Thomas on 23.05.2015.
 */
public class Timer extends Activity {

    //UI Elements
    /* two progress Bar for time and row time */
    private ProgressBar prBar_total, prBar_row;
    /* all TextView for the UI display */
    private TextView txtView_gear, txtView_note, txtView_timRow,txtView_rythm_value ,txtView_work_value ,txtView_bpm_value, txtView_rpm_value,txtView_milis;
    private Button btn_start, btn_stop, btn_pause;

    //Object
    private List<TrainingRow> lst_trainingRow;
    private RealmDB realmDB;
    private List<Integer> lst_time;
    /* test with handler */
    private Handler handler = new Handler();
    private TimerRunnable runnable = new TimerRunnable();
    private AlertDialog alertDialog;

    //VARIABLES
    private int _id;

    //Varibales for the Timer
    /* Int to translate seconds in milliseconds */
    private static int INT_MILLIS = 1000;
    /* variable for the seconds/minutes */
    private static int INT_SEC = 60;
    /* time of the first row */
    private int int_time_first_row;
    /* total time in milliseconds */
    private int int_total_timeInMillis;
    /* boolean if the time isCanceled or isPaused */
    private boolean isCanceled = false;
    /* variable to hold the value of the Intervals */
    private final int INTERVALS = 100;
    /* variable incremented for the progress */
    private int int_progress_value;
    /* variable to hold the progress of the row time */
    private int int_progress_rowTime;
    // Volatile beacause those variable are accesed by to different class
    /* int_i variable that hold the id of the row */
    private volatile int int_i = 0;
    /* variable to hold the time for the row */
    private volatile int int_row_time;
    /* variable to hold the time only for display */
    private volatile int int_rowTime_display;

    int test = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        /* the 3 buttons */
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        /* all textView to display data */
        txtView_timRow = (TextView) findViewById(R.id.txtView_rowTime);
        txtView_rythm_value = (TextView) findViewById(R.id.txtView_rythm_value);
        txtView_work_value = (TextView) findViewById(R.id.txtView_work_value);
        txtView_bpm_value = (TextView) findViewById(R.id.txtView_bpm_value);
        txtView_rpm_value = (TextView) findViewById(R.id.txtView_rpm_value);
        txtView_milis = (TextView) findViewById(R.id.txtView_milis);
        txtView_note = (TextView) findViewById(R.id.txtView_notes_value);
        txtView_gear = (TextView) findViewById(R.id.txtView_gear_value);

        /* the two progressBar */
        prBar_row = (ProgressBar) findViewById(R.id.progressBar_row);
        prBar_total = (ProgressBar) findViewById(R.id.progressBar_total);

        //get the id of the training from the last actvity
        _id = getIntent().getIntExtra("_id", 0);

        //get from RealmDB total time and all Rows
        realmDB = new RealmDB(Timer.this);
        //get all Rows
        lst_trainingRow = realmDB.getAllTrainingRows(_id);
        //get time of all cumulate rows
        lst_time = realmDB.calculateTotalMinAndSec(_id);

        //set the value for the first row
        //time for the row
        txtView_timRow.setText(lst_trainingRow.get(int_i).getStr_time());
        //value for the row
        txtView_rpm_value.setText(lst_trainingRow.get(int_i).getInt_rpm()+""); //rpm
        txtView_bpm_value.setText(lst_trainingRow.get(int_i).getInt_bpm()+""); //bpm
        txtView_rythm_value.setText(lst_trainingRow.get(int_i).getStr_rythm()); //rythme
        txtView_work_value.setText(lst_trainingRow.get(int_i).getStr_work()); //work
        txtView_note.setText(lst_trainingRow.get(int_i).getStr_note()); //notes
        txtView_gear.setText(lst_trainingRow.get(int_i).getStr_gear()); //gear

        //get total time in miliseconds
        int_total_timeInMillis = ((lst_time.get(0) * INT_SEC) + lst_time.get(1)) * INT_MILLIS;

        //get time of the first row of training
        int_time_first_row = (((lst_trainingRow.get(int_i).getInt_min()*INT_SEC) + lst_trainingRow.get(int_i).getInt_sec())* INT_MILLIS);

        //set the time for the first display
        int_rowTime_display = int_time_first_row + 1000;

        //set the row time for the first display
        int_row_time = int_time_first_row;

        //disable Buttons stop and pause before timer is started
        btn_stop.setEnabled(false);
        btn_pause.setEnabled(false);

        /************************************
         * Button start onClickListener
         ************************************/
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set check values to false
                isCanceled = false;

                if (btn_start.getText().equals(getResources().getString(R.string.start))) {
                    /* set the value for the ProgressBar total = total amount of time */
                    prBar_total.setMax(int_total_timeInMillis);
                    /* set the max Value for the progressBar row = tiem of the first Row*/
                    prBar_row.setMax(int_time_first_row);

                    //enable/disable button
                    btn_start.setEnabled(false);
                    btn_pause.setEnabled(true);
                    btn_stop.setEnabled(true);

                    //call handler post delayed to start the runnable
                    handler.postDelayed(runnable, 100);

                } else { //part for the resume button
                    //set the already done progression to the progressBar (prBar_row)
                    prBar_row.setProgress(int_progress_rowTime);

                    btn_start.setEnabled(false);
                    btn_start.setText(getResources().getString(R.string.start));

                    //countDown reRun so we enable pause or stop
                    btn_pause.setEnabled(true);
                    btn_stop.setEnabled(true);

                    //call handler post delayed to resume the runnable
                    handler.postDelayed(runnable, 100);

                }

            }
        });

        /************************************
         * Button pause onClickListener
         ************************************/
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call those two methods to cancel the Timer (Runnable)
                runnable.killRunnable();
                handler.removeCallbacks(runnable);

                //enable resume button. disable Pause button
                /* change text of the button start. This way he become the resume button */
                btn_start.setText(getResources().getString(R.string.resume));
                btn_start.setEnabled(true);
                btn_stop.setEnabled(true);
                btn_pause.setEnabled(false);
            }
        });

        /************************************
         * Button stop onClickListener
         ************************************/
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable all button
                btn_start.setEnabled(true);
                //change text of start button in case the user want to continue
                btn_start.setText(getResources().getString(R.string.resume));
                btn_stop.setEnabled(false);
                btn_pause.setEnabled(false);

                //call those two methods to pause the Timer (Runnable)
                runnable.killRunnable();
                handler.removeCallbacks(runnable);

                //check with an alertDialog if the user realy wants to stop the training.

                AlertDialog.Builder builder = new AlertDialog.Builder(Timer.this)
                        .setTitle(getResources().getString(R.string.stop_timer))
                        .setMessage(getResources().getString(R.string.stop_message))
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //disable start bouton
                                btn_start.setEnabled(false);
                                //set TxtView_timeRow to 0:0
                                txtView_timRow.setText("0:0");
                                //set PrBar_row progress to 0
                                prBar_row.setProgress(0);
                                //same for total time
                                prBar_total.setProgress(0);

                                //go back to previous activiy
                                Intent intent_trainingRow = new Intent(Timer.this, TrainingRowActivity.class);
                                //put extra id to get all Rows back
                                intent_trainingRow.putExtra("_id", _id);
                                //put a Flag to avoid recreate intent if he already exist in the queue
                                intent_trainingRow.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                //get context to startActivity
                                getBaseContext().startActivity(intent_trainingRow);
                                //finish and cancel activty
                                finish();
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //cancel the alertDialog
                                dialogInterface.dismiss();
                            }
                        });
                //from the builder create the alertDialog
                alertDialog = builder.create();
                //show the dialog to user
                alertDialog.show();

                txtView_milis.setText("CountDownTimer Canceled/Stopped");

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null){
            // kill runnable if the activity is destroyed
            runnable.killRunnable();

        }
    }//onDestroy


    /********************************************************
     *
     * Custom class TimerRunnable
     * Manage the Timer element and the progressBar progress
     *
     *******************************************************/
    private class TimerRunnable implements Runnable {

        //variables to create a TrainingRow
        private int int_min, int_sec, int_rpm, int_bpm;
        private String str_gear, str_time, str_work, str_rythm, str_note;

        @Override
        public void run() {
                //check if timer is paused or if total time is finish
                if (isCanceled == true || int_total_timeInMillis == int_progress_value) {
                    //set runnable to null
                    runnable = null;
                    //return
                    return;
                } else {
                    //increment int_progess_value
                    int_progress_value += INTERVALS;
                    //increment int_progress_rowTime
                    int_progress_rowTime += INTERVALS;

                    Log.w("progress= row_time", int_progress_rowTime +"=" +int_row_time);

                    if(int_progress_rowTime == int_row_time){

                        //try to get All training Row
                        lst_trainingRow = realmDB.getAllTrainingRows(_id);

                        //this if control the incrementation of i
                        if (int_i < lst_trainingRow.size() - 1) {
                            //increment i
                            int_i++;

                            //create Training Row
                            int_min = lst_trainingRow.get(int_i).getInt_min();
                            int_sec = lst_trainingRow.get(int_i).getInt_sec();
                            int_rpm = lst_trainingRow.get(int_i).getInt_rpm();
                            int_bpm = lst_trainingRow.get(int_i).getInt_bpm();
                            str_gear = lst_trainingRow.get(int_i).getStr_gear();
                            str_time = lst_trainingRow.get(int_i).getStr_time();
                            str_rythm = lst_trainingRow.get(int_i).getStr_note();
                            str_work = lst_trainingRow.get(int_i).getStr_work();
                            str_note = lst_trainingRow.get(int_i).getStr_work();

                            //set new max time for the row
                            int_row_time = ((int_min * INT_SEC) + int_sec) * INT_MILLIS;

                            /* only for the display we had 1000 millis.
                            like this the bar is full */
                            int_rowTime_display = int_row_time + 1000;

                            //change max value
                            prBar_row.setMax(int_row_time);

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

                    //set the progression to the progressBar (prBar_total)
                    prBar_total.setProgress(int_progress_value);
                    //set the progression to the progressBar (prBar_row)
                    prBar_row.setProgress(int_progress_rowTime);

                    //update CountDown Timer for the Row
                    //update the time in the screen for the countdown
                    int_rowTime_display -= INTERVALS;
                    //get the time in seconds by dividing by 1000
                    int row_min = int_rowTime_display / INT_MILLIS;
                    //divide row_min by 60 to get minutes. and row_min % 60 to get seconds
                    txtView_timRow.setText("" + row_min / INT_SEC + ":" + row_min % INT_SEC);

                    //$TEST
                    test++;
                    txtView_milis.setText(test +" - "+ "i = " + int_i);

                }
            //delayed post handler every second
            handler.postDelayed(this, 100);

        }

        public void killRunnable(){
            //when timer is paused or stopped isCanceled is set to true.
            isCanceled = true;
        }

    }//Class TimerRunnable

}//Class Timer
