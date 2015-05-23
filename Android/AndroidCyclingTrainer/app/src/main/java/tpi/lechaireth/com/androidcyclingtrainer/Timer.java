package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Thomas on 23.05.2015.
 */
public class Timer extends Activity {
    private ProgressBar p;
    private RowTimer mTimer;
    private static int INT_MILLIS = 1000;
    private TextView txtView_min, txtView_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
    }

    private void startTimer(int min, int sec){
        //if the timer is not null or if the status is different from FINISHED
        if(mTimer != null && mTimer.getStatus() != RowTimer.Status.FINISHED){
            //cancel the task
            mTimer.cancel(true);
        }//if
        mTimer = (RowTimer) new RowTimer().execute(min,sec);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = (ProgressBar) findViewById(R.id.progressBar_total);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected String doInBackground(Integer... time) {

            final int INTERVALS = 100;
            if(time.length > 0) {
                //get back min and sec
                int min = time[0];
                int sec = time[1];

                //get time in seconds
                int millis = ((min * 60) + sec) * INT_MILLIS;
                p.setMax(millis);
                Log.w("MILLIS ", "" + millis);

                //timer to run the progress
                while (timeProgress < millis) {


                    //add one second
                    publishProgress(timeProgress += INTERVALS);
                    //wait for one seconds
                    SystemClock.sleep(INTERVALS);
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}//Class Timer
