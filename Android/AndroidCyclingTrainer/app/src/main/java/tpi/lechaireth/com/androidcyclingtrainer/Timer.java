package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private RowTimer mTimer;
    private List<TrainingRow> lst_trainingRow;
    private RealmDB realmDB;
    private List<Integer> lst_time;

    //VARIABLES
    private static int INT_MILLIS = 1000;
    private int _id, int_total_min, int_total_sec;
    private int int_max_row;



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

        //get the first row of training just for the begining
        int_max_row = ((lst_trainingRow.get(0).getInt_min()*60) + lst_trainingRow.get(0).getInt_sec()* INT_MILLIS);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(lst_time.get(0), lst_time.get(1), int_max_row);
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void startTimer(int min, int sec, int int_max_row){
        //if the timer is not null or if the status is different from FINISHED
        if(mTimer != null && mTimer.getStatus() != RowTimer.Status.FINISHED){
            //cancel the task
            mTimer.cancel(true);
        }//if
        mTimer = (RowTimer) new RowTimer().execute(min,sec, int_max_row);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop the asyncTask if when the Activity Is Destroyed
        if (mTimer != null
                && mTimer.getStatus() != RowTimer.Status.FINISHED) {
            mTimer.cancel(true);
            mTimer = null;
        }
    }

    //AsyncTask <TypeOfVarArgParams , ProgressValue , ResultValue>
    private class RowTimer extends AsyncTask<Integer,Integer,String> {

        int timeProgress = 0;
        int i, row_time;
        int int_min, int_sec, int_rpm, int_bpm;
        String str_gear, str_time, str_work, str_rythm, str_note;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prBar_total = (ProgressBar) findViewById(R.id.progressBar_total);
            prBar_row = (ProgressBar) findViewById(R.id.progressBar_row);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            prBar_total.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Integer... time) {

            final int INTERVALS = 100;
            if(time.length > 0) {
                //get back min and sec
                int min = time[0];
                int sec = time[1];
                int int_max_row = time[2];
                row_time = int_max_row + 1000;
                //set i to 0
                i = 0;
                //get time in miliseconds
                int millis = ((min * 60) + sec) * INT_MILLIS;

                prBar_total.setMax(millis);
                prBar_row.setMax(int_max_row);
                //get the training list

                Log.w("MILLIS ", "" + millis);

                int time_row = 0;


                //timer to run the progress
                while (timeProgress < millis) {
                    //add one second
                    publishProgress(timeProgress += INTERVALS);

                    //check if prBar_row is done
                    if (time_row == int_max_row){
                        lst_trainingRow = realmDB.getAllTrainingRows(_id);

                        //increment i
                        i++;
                        Log.w("TAG i", i+"");

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
                        int_max_row = ((int_min*60) + int_sec * INT_MILLIS);
                        row_time = int_max_row + 1000;
                        //change max value
                        prBar_row.setMax(int_max_row);

                        //on change set new value for the row
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtView_timRow.setText(str_time);
                                //value for the row
                                txtView_rpm_value.setText(int_rpm+"");
                                txtView_bpm_value.setText(int_bpm+"");
                                txtView_rythm_value.setText(str_rythm);
                                txtView_work_value.setText(str_work);
                                //note and gear missing
                            }
                        });

                        //reset Progress to 0
                        prBar_row.setProgress(0);

                        //reset time row to 0;
                        time_row =  0;
                    }

                    //progress of the row bar
                    prBar_row.setProgress(time_row += INTERVALS);

                    //update the time in the screen
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            row_time -= INTERVALS;
                            int row_min = row_time/INT_MILLIS;
                            txtView_timRow.setText(""+row_min/60+":"+row_min%60);
                        }
                    });

                    //wait for one seconds
                    SystemClock.sleep(INTERVALS);
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtView_timRow.setText("0:0");


            //entrer l'indice de récupération

        }
    }

}//Class Timer
