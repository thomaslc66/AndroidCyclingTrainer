package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.library.VerticalSeekBar;


/**
 * Created by Thomas on 05.05.2015.
 */
public class TrainingRowDeatilsActivity extends ActionBarActivity {

    //UI Elements
    private TextView txtView_gear, txtView_rpm, txtView_bpm;
    private Button btn_validate_row;
    private VerticalSeekBar verticlalBar_rpm, verticalBar_bpm;
    private Spinner spinner_work,spinner_rythm;
    private NumberPicker nbrPicker_front, nbrPicker_back;
    private AlertDialog alertDialog;
    private NumberPicker nbrPicker_min,nbrPicker_sec;

    //CONSTANTS
    private static final int INT_MIN_FRONT_GEAR = 36;
    private static final int INT_MAX_FRONT_GEAR = 56;
    private static final int INT_MIN_BACK_GEAR = 11;
    private static final int INT_MAX_BACK_GEAR = 27;
    private static final int INT_MAX_TIME = 59;

    //VARIABLES
    private int int_min_work, int_sec_work, int_min_rest, int_sec_rest, int_bpm, int_rpm;
    private String str_work = "";
    private String str_rythm = "";
    private String str_note = "";
    private String str_gear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row_details);

        //get data from last intent
        final int int_id = getIntent().getIntExtra("_id",0);

        //UI Elements Initialisation
        txtView_bpm = (TextView) findViewById(R.id.txtView_bpm);
        txtView_rpm = (TextView) findViewById(R.id.txtView_rpm);

        //initialisation of the two number pickers for the time
        nbrPicker_min = ( NumberPicker) findViewById(R.id.nbrPricker_min);
        nbrPicker_sec = (NumberPicker) findViewById(R.id.nbrPicker_sec);
        nbrPicker_min.setMaxValue(INT_MAX_TIME);
        nbrPicker_sec.setMaxValue(INT_MAX_TIME);

        /* Initialisation of the Button for the validation */
        btn_validate_row = (Button) findViewById(R.id.btn_validate_row);

        /* Initialisation of the 2 VerticalSeekBar*/
        verticlalBar_rpm = (VerticalSeekBar) findViewById(R.id.verticalBar_rpm);
        verticalBar_bpm = (VerticalSeekBar) findViewById(R.id.verticalBar_bpm);

        /* Initialisation of the 2 spinners */
        spinner_work = (Spinner) findViewById(R.id.spinner_travail);
        spinner_rythm = (Spinner) findViewById(R.id.spinner_rythme);

        // put the two list into the spinners
        ArrayAdapter<CharSequence> adapter_work = ArrayAdapter.createFromResource(this,R.array.work, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_rythm = ArrayAdapter.createFromResource(this,R.array.rythm, android.R.layout.simple_spinner_item);
        // link list to spinners
        spinner_work.setAdapter(adapter_work);
        spinner_rythm.setAdapter(adapter_rythm);

        //chose the gear for bike
        txtView_gear = (TextView) findViewById(R.id.txtView_plate);


        /*
        *  txtView_gear onClickListener
        *  Goal: let the user set the gear of the bike for the current row.
        *
        */
        txtView_gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creation of an inflater for the layout
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Create a view that inflate the layout plate_dialog
                View v1 = mInflater.inflate(R.layout.gear_dialog, null);

                //Build an Alert Dialod
                AlertDialog.Builder alrt_builder = new AlertDialog.Builder(TrainingRowDeatilsActivity.this);
                //set the view of the Dialog
                alrt_builder.setView(v1);
                alrt_builder.setTitle(getResources().getString(R.string.choice_braquets));
                alrt_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int int_front_value, int_back_value;
                        //numberPickers
                        int_front_value = nbrPicker_front.getValue();
                        int_back_value = nbrPicker_back.getValue();
                        txtView_gear.setText(int_front_value + "X" + int_back_value);
                    }
                });

                alertDialog = alrt_builder.create();


                //initialisation of the two number pickers for the gears
                nbrPicker_front = (NumberPicker) v1.findViewById(R.id.nbPicker_front);
                nbrPicker_back = (NumberPicker) v1.findViewById(R.id.nbPicker_back);

                nbrPicker_front.setWrapSelectorWheel(false);
                nbrPicker_back.setWrapSelectorWheel(true);

                nbrPicker_back.setMaxValue(INT_MAX_BACK_GEAR);
                nbrPicker_back.setMinValue(INT_MIN_BACK_GEAR);
                nbrPicker_front.setMaxValue(INT_MAX_FRONT_GEAR);
                nbrPicker_front.setMinValue(INT_MIN_FRONT_GEAR);

                alertDialog.show();
            }
        });

        //onClickListener Validate
        btn_validate_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValue();
                int bpm, min_work,sec_work,rpm;


                bpm = Integer.parseInt(txtView_bpm.getText().toString());
                min_work = nbrPicker_min.getValue();
                sec_work = nbrPicker_sec.getValue();
                rpm = Integer.parseInt(txtView_rpm.getText().toString());

                RealmDB realmDB = new RealmDB(TrainingRowDeatilsActivity.this);

                try{

                    //add a row to the training we have selected
                    realmDB.addAtrainingRowToTraining(int_id,realmDB.createRow(int_min_work, int_sec_work, int_rpm, int_bpm, int_min_rest, int_sec_rest,str_gear, str_work,str_rythm,str_note));

                }catch (Exception e){
                    Toast.makeText(TrainingRowDeatilsActivity.this, "Erreur d'ajout...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Intent backtoRows = new Intent(TrainingRowDeatilsActivity.this, TrainingRowActivity.class);
                //return to the same Training
                backtoRows.putExtra("_id",int_id);
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
                txtView_bpm.setText((progress + 40) + "");
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
                txtView_rpm.setText((progress + 30) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }//onCreate

    /***
     *
     */
    private void checkValue() {
        String str_error = "";
        //check if time is bigger than 10 sec;
        if((nbrPicker_min.getValue() * 10) + nbrPicker_sec.getValue() < 10){
            str_error += getResources().getString(R.string.error_time);
        }else{
            //if value is bigger than 10 secondes qe can set the time
            int_min_work = nbrPicker_min.getValue();
            int_sec_work = nbrPicker_sec.getValue();
        }
        //check if bpm is set
        if(txtView_bpm.equals(null) || txtView_bpm.getText().toString() == ""){
            str_error += getResources().getString(R.string.error_bpm);
        }else{
            //set int_bpm to the value of the progressBar
            int_bpm = verticalBar_bpm.getProgress();
        }
        //check if rpm is set
        if(txtView_rpm.equals(null) || txtView_rpm.getText().toString() == ""){
            str_error += getResources().getString(R.string.error_rpm);
        }else{
            //set int_rpm to the value of the progressBar
            int_rpm = verticlalBar_rpm.getProgress();
        }
        //check if spinner work is selected
        if (spinner_work.getSelectedItem().toString() == "" || spinner_work.getSelectedItem().toString().equals(null)){
            str_error += getResources().getString(R.string.error_work);
        }else{
            //if value is not empty set str_work to the value of th spinner
            str_work = spinner_work.getSelectedItem().toString();
        }
        //check if spinner rythm is selected
        if (spinner_rythm.getSelectedItem().toString() == "" || spinner_rythm.getSelectedItem().toString().equals(null)){
            str_error += getResources().getString(R.string.error_rythm);
        }else{
            //if value is not empty set str_rythm to the value of th spinner
            str_rythm = spinner_rythm.getSelectedItem().toString();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validate_row, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_validate) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private String checkTime(int hours, int min, int sec){
        String time = "";
        if (min > 60){
            min = min % 60;
            //if min is bigger than 60 we can a one hour to time.
            hours ++;
        }
        if (sec > 60){
            sec = sec % 60;
            min++;
        }
        time = hours+"h"+min+"m"+sec+"s";
        return time;
    }
}
