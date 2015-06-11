/***************************************************************
 * Programm  : Android Cycling Trainer
 * Society   : ETML
 * Author    : Thomas Léchaire
 * Date      : 26.05.2015
 * Goal      : Class used to display ListView of rows
 ******************************************************************** //
 * Modifications:
 * Date       : XX.XX.XXXX
 * Author     :
 * Purpose     :
 *********************************************************************/
package tpi.lechaireth.com.androidcyclingtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.Adapter.TrainingRowAdapter;
import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;


public class TrainingRowActivity extends ActionBarActivity {

    /* UI Elements */
    private ListView listView_row;
    private TrainingRowAdapter trainingRowAdapter;
    private List<TrainingRow> trainingRowList;
    private RealmDB realmDB;
    private Button btn_start_training;
    private TextView txtView_row_total, txtView_time_total, txtView_bpm_rpm;

    /* variables */
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row);

        /* MANAGE ACTION BAR TITLE*/
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.app_name));
        ab.setSubtitle(getString(R.string.tRow_subTitle));

        /* UI ELements */
        listView_row = (ListView) findViewById(R.id.listView_row);
        trainingRowList = new ArrayList<TrainingRow>();
        btn_start_training = (Button) findViewById(R.id.btn_start_training);
        txtView_row_total = (TextView) findViewById(R.id.txtView_Row_Total);
        txtView_time_total = (TextView) findViewById(R.id.txtView_Total_Time);
        txtView_bpm_rpm = (TextView) findViewById(R.id.txtView_Bpm_Rpm);

        realmDB = new RealmDB(this);

        //from the last intent we get the Training id value
         _id = getIntent().getIntExtra("_id",0);

        //maybe add a thread
        /* we try to get all the training row from the db */
        try{
            trainingRowList = realmDB.getAllTrainingRows(_id);
            /* check if the trainingRow list is empty */
            if (trainingRowList.size() == 0){
                Toast.makeText(this,"Pas de séquence dans cet entraînement",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Erreur de récupération",Toast.LENGTH_SHORT).show();
        }

        //we set the adapter
        trainingRowAdapter = new TrainingRowAdapter(this,trainingRowList,_id);
        listView_row.setAdapter(trainingRowAdapter);

        /* we add an onClickListener on the add Button */
        btn_start_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //control that there is at least one trainingRow in the training before starting
                if(trainingRowList.size() > 0){
                //start the new activity Timer
                Intent start_timer = new Intent(TrainingRowActivity.this, Timer.class);
                start_timer.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                start_timer.putExtra("_id", _id);
                startActivity(start_timer);
                finish();
                }else{
                    //dispaly error message when start button is clicked but not row are added
                    Toast.makeText(TrainingRowActivity.this,getString(R.string.no_training),Toast.LENGTH_LONG).show();
                }
            }
        });

        getCombinedData.start();

    }//onCreate


    /********************************************
     *
     * Thread to get all the data of the training
     *
     *****************************************************/
    Thread getCombinedData = new Thread(new Runnable() {
        @Override
        public void run() {
            final String time = realmDB.calculateTotalTime(_id);
            //get avrg_bpm
            final int avrg_bpm = realmDB.getAverageBpmOfTraining(_id);

            //run on main Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtView_time_total.setText(time+"");
                    txtView_row_total.setText(trainingRowList.size()+"");
                    txtView_bpm_rpm.setText("Bpm moyen: "+ avrg_bpm);
                }
            });
        }
    });


    /*************************************************************************
     * onCreateOptionsMenu Method
     * @param menu
     * Goal: get the layout for the actionBar menu
     ***********************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    /********************************************************************
     * Name: onOptionsItemSelected
     * @param item
     * Goal: Method to tell what to do on menu item click
     ***********************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            /* Launching the intent to add a row and the details */
            Intent addRowIntent = new Intent(TrainingRowActivity.this, TrainingRowDetailsActivity.class);
            //add id of the training
            addRowIntent.putExtra("_id", _id);
            /* add a flag to update to reorder the activity to front if it already exists */
            addRowIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(addRowIntent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************************
     * Name: onStop Method
     * Goal: Method called when the phone stop the Avtivity
     ***********************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        //close Realm. Close Acces to data
        realmDB.close();
        //free object
        trainingRowList = null;
        trainingRowAdapter = null;
        getCombinedData.interrupt();
    }

    /********************************************************************
     * Name: onDestroy Method
     * Goal: Method called when the phone kill the Avtivity
     ***********************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();

    }

    /********************************************************************
     * Name: freeMemory
     * Goal: Run the garbage collector to get back some allocated memory
     ***********************************************************************/
    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    /********************************************************************
     * Name: onBackPressed
     * Goal: Method called when the user press on the back Button of the phone
     ***********************************************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //get Back to TrainingRow Activit
        Intent back_to_training = new Intent(TrainingRowActivity.this, TrainingActivity.class);
        /* this flag is here because if we don't create a new Activity from begining there will be a problem
         accesing the realm instance created in the first launch of TrainingActivity. both are noth from the same thread and
         there will be an error */
        back_to_training.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back_to_training);
        finish();
    }

}//TrainingRowActivity
