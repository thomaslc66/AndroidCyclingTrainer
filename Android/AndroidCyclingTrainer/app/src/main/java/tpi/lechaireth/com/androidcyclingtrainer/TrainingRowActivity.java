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


/**
 * Created by Thomas on 22.05.2015.
 */
public class TrainingRowActivity extends ActionBarActivity {

    /* UI Elements */
    private ListView listView_row;
    private TrainingRowAdapter trainingRowAdapter;
    private List<TrainingRow> trainingRowList;
    private RealmDB realmDB;
    private Button btn_start_training;
    private TextView txtView_row_total, txtView_rpm_bpm, txtView_time_total;

    /* variables */
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row);


        /* UI ELements */
        listView_row = (ListView) findViewById(R.id.listView_row);
        trainingRowList = new ArrayList<TrainingRow>();
        btn_start_training = (Button) findViewById(R.id.btn_start_training);
        txtView_row_total = (TextView) findViewById(R.id.txtView_Row_Total);
        txtView_rpm_bpm = (TextView) findViewById(R.id.txtView_Bpm_Rpm);
        txtView_time_total = (TextView) findViewById(R.id.txtView_Total_Time);

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
                }
            }
        });

        getCombinedData.start();

    }//onCreate


    /**
     * Thread to get all the data of the training
     *
     */
    Thread getCombinedData = new Thread(new Runnable() {
        @Override
        public void run() {
            final String time = realmDB.calculateTotalTime(_id);

            //run on main Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtView_time_total.setText(time+"");
                    txtView_row_total.setText(trainingRowList.size()+"");
                }
            });
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            /* Launching the intent to add a row and the details */
            Intent addRowIntent = new Intent(TrainingRowActivity.this, TrainingRowDeatilsActivity.class);
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

    /**
     * OnActivity Stop this will just kill acces to the realm Database to avoid memory leak
     */
    @Override
    protected void onStop() {
        super.onStop();
        //close Realm Instance
        realmDB.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
        trainingRowList = null;
        trainingRowAdapter = null;
        getCombinedData.interrupt();

    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    /**
     * Return Method
     */
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
