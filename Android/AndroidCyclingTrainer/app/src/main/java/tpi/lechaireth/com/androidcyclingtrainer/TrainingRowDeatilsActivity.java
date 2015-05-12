package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.library.VerticalSeekBar;


/**
 * Created by Thomas on 05.05.2015.
 */
public class TrainingRowDeatilsActivity extends Activity{

    //UI Elements
    private EditText edtTxt_min, edtTxt_sec, edtTxt_tour, edtTxt_Bpm;
    private Button btn_validate_row;
    private VerticalSeekBar verticlalBar_rpm, verticalBar_bpm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row_details);

        //get data from last intent
        final int _id = getIntent().getIntExtra("_id",0);

        //UI Elements Initialisation
        edtTxt_Bpm = (EditText) findViewById(R.id.edtTxt_bpm);
        edtTxt_min = (EditText) findViewById(R.id.edtTxt_min);
        edtTxt_sec = (EditText) findViewById(R.id.edtTxt_sec);
        edtTxt_tour = (EditText) findViewById(R.id.edtTxt_tour);

        /* Initialisation of the Button for the validation */
        btn_validate_row = (Button) findViewById(R.id.btn_validate_row);

        /* Initialisation of the 2 VerticalSeekBar*/
        verticlalBar_rpm = (VerticalSeekBar) findViewById(R.id.verticalBar_rpm);
        verticalBar_bpm = (VerticalSeekBar) findViewById(R.id.verticalBar_bpm);

        //onClickListener Validate
        btn_validate_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bpm, min,sec,tour;
                bpm = Integer.parseInt(edtTxt_Bpm.getText().toString());
                min = Integer.parseInt(edtTxt_min.getText().toString());
                sec = Integer.parseInt(edtTxt_sec.getText().toString());
                tour = Integer.parseInt(edtTxt_tour.getText().toString());

                RealmDB realmDB = new RealmDB(TrainingRowDeatilsActivity.this);

                try{
                    //add a row to the training we have selected
                    realmDB.addAtrainingRowToTraining(_id,realmDB.createRow(min, sec, tour, bpm));

                }catch (Exception e){
                    Toast.makeText(TrainingRowDeatilsActivity.this, "Erreur d'ajout...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Intent backtoRows = new Intent(TrainingRowDeatilsActivity.this, TrainingRowActivity.class);
                //return to the same Training
                backtoRows.putExtra("_id",_id);
                //reorder the activity to front
                backtoRows.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(backtoRows);
                finish();
            }
        });


        /* Method for the seek bar change listener*/
        verticalBar_bpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtTxt_tour.setText((progress+40)+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        verticlalBar_rpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtTxt_Bpm.setText((progress+30)+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }//onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
