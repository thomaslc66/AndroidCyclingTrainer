package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView txtView_gear, txtView_rpm, txtView_bpm, txtView_restTime;
    private Button btn_rep;
    private VerticalSeekBar verticlalBar_rpm, verticalBar_bpm;
    private Spinner spinner_work,spinner_rythm;
    private NumberPicker nbrPicker_front, nbrPicker_back, nbrPicker_recup_min, nbrPicker_recup_sec;
    private AlertDialog alertDialog;
    private NumberPicker nbrPicker_min,nbrPicker_sec;
    private EditText edtTxt_notes;

    //CONSTANTS
    private static final int INT_MIN_FRONT_GEAR = 36;
    private static final int INT_MAX_FRONT_GEAR = 56;
    private static final int INT_MIN_BACK_GEAR = 11;
    private static final int INT_MAX_BACK_GEAR = 27;
    private static final int INT_MAX_TIME = 59;

    //VARIABLES
    private int int_min_work, int_sec_work, int_min_rest, int_sec_rest, int_bpm, int_rpm;
    private int int_id,int_recupMin_value,int_recupSec_value, int_rowId;
    private int int_count = 1;
    private String str_work = "";
    private String str_rythm = "";
    private String str_note = "";
    private String str_gear = "";
    private String str_error;
    private boolean bln_recup = false;
    //int value for both progression of the vertical bar
    private int int_progress_bpm;
    private int int_progress_rpm;

    //CONSTANT
    private static int INT_MIN_BPM_VALUE = 30;
    private static int INT_MIN_RPM_VALUE = 40;
    private static int INT_MAX_BPM_VALUE = 200;
    private static int INT_MAX_RPM_VALUE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row_details);

        //get data from last intent
        int_id = getIntent().getIntExtra("_id",0);

        //UI Elements Initialisation
        txtView_bpm = (TextView) findViewById(R.id.txtView_bpm);
        txtView_rpm = (TextView) findViewById(R.id.txtView_rpm);
        //rest time UI element
        txtView_restTime = (TextView) findViewById(R.id.txtView_recup);

        //initialisation of the two number pickers for the time
        nbrPicker_min = ( NumberPicker) findViewById(R.id.nbrPricker_min);
        nbrPicker_sec = (NumberPicker) findViewById(R.id.nbrPicker_sec);
        nbrPicker_min.setMaxValue(INT_MAX_TIME);
        nbrPicker_sec.setMaxValue(INT_MAX_TIME);

        /* Initialisation of the Button for the validation */
        btn_rep = (Button) findViewById(R.id.btn_rep);

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

        //editText for the notes
        edtTxt_notes = (EditText) findViewById(R.id.edttext_note);
        //editText for the notes
        edtTxt_notes = (EditText) findViewById(R.id.edttext_note);
        //add done button
        edtTxt_notes.setImeOptions(EditorInfo.IME_ACTION_DONE);

        /**********************************************************************
         *
         *  txtView_gear onClickListener
         *  Goal: let the user set the gear of the bike for the current row.
         *
         *********************************************************************/
        txtView_gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creation of an inflater for the layout
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Create a view that inflate the layout gear_dialog
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
                alrt_builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        dialog.dismiss();
                    }
                });

                alertDialog = alrt_builder.create();

                //initialisation of the two number pickers for the gears
                nbrPicker_front = (NumberPicker) v1.findViewById(R.id.nbPicker_front);
                nbrPicker_back = (NumberPicker) v1.findViewById(R.id.nbPicker_back);

                nbrPicker_back.setMaxValue(INT_MAX_BACK_GEAR);
                nbrPicker_back.setMinValue(INT_MIN_BACK_GEAR);
                nbrPicker_front.setMaxValue(INT_MAX_FRONT_GEAR);
                nbrPicker_front.setMinValue(INT_MIN_FRONT_GEAR);

                alertDialog.show();
            }
        });

        /**********************************************************************
         *
         *  btn_rep onClickListener
         *  Goal: let the user set the nomber of time he wants to repeat the row
         *
         *********************************************************************/
        btn_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get max 10 rep.
                if(int_count > 10){
                    int_count = 0;
                }
                //add one to count
                int_count++;
                //set text of button
                btn_rep.setText(int_count+"X");
            }
        });

        txtView_restTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creation of an inflater for the layout
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Create a view that inflate the layout timePicker_dialog
                View v1 = mInflater.inflate(R.layout.timepicker_dialog, null);

                //Build an Alert Dialod
                AlertDialog.Builder alrt_builder = new AlertDialog.Builder(TrainingRowDeatilsActivity.this);
                //set the view of the Dialog
                alrt_builder.setView(v1);
                alrt_builder.setTitle(getResources().getString(R.string.define_row_time));
                alrt_builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        dialog.dismiss();
                    }
                });
                alrt_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //numberPickers
                        int_recupMin_value = nbrPicker_recup_min.getValue();
                        int_recupSec_value = nbrPicker_recup_sec.getValue();
                        txtView_restTime.setText(int_recupMin_value + ":" + int_recupSec_value);
                        bln_recup = true;
                    }
                });

                //build the alertDialog
                alertDialog = alrt_builder.create();

                //initialisation of the two number pickers for the gears
                nbrPicker_recup_min = (NumberPicker) v1.findViewById(R.id.nbPicker_recup_min);
                nbrPicker_recup_sec = (NumberPicker) v1.findViewById(R.id.nbPicker_recup_sec);

                //set max value to the number picker
                nbrPicker_recup_min.setMaxValue(INT_MAX_TIME);
                nbrPicker_recup_sec.setMaxValue(INT_MAX_TIME);
                //show the alert dialog
                alertDialog.show();
            }
        });

        //max bpm value
        verticalBar_bpm.setMax(INT_MAX_BPM_VALUE);
        /* Method for the seek bar change listener for bpm bar*/
        verticalBar_bpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //maximum = 200 and min = 30;
                int_progress_bpm = progress + INT_MIN_BPM_VALUE;
                //set progression to textView
                txtView_bpm.setText(int_progress_bpm+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //max rpm value
        verticlalBar_rpm.setMax(INT_MAX_RPM_VALUE);
        /* Method for the seek bar change listener for rpm bar */
        verticlalBar_rpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //maximum = 300 and min = 40;
                int_progress_rpm = progress + INT_MIN_RPM_VALUE;
                //set progression to textView
                txtView_rpm.setText(int_progress_rpm + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }//onCreate

    /*****************************************************************************
     *
     * Name: validateRow()
     * Goal: method to validate the row, check the details, and add it to realm
     *
     ****************************************************************************/
    private void validateRow(int nbr_repeat) {
        //checkValue return true if there is an error
        if(!checkValue()){
            RealmDB realmDB = new RealmDB(TrainingRowDeatilsActivity.this);

            try{
                //this loop check how many times the user wants to repeat a row
                for (int i = 0; i < nbr_repeat; i++){
                    //add a row to the training we have selected
                    realmDB.addAtrainingRowToTraining(int_id,realmDB.createRow(
                            int_min_work,
                            int_sec_work,
                            int_rpm,
                            int_bpm,
                            int_min_rest,
                            int_sec_rest,
                            str_gear,
                            str_work,
                            str_rythm,
                            str_note));

                    //here we check if bln_recup is true
                    if(bln_recup){
                        //if it's true we add a new training row for recuperation
                        //add a row to the training we have selected
                        realmDB.addAtrainingRowToTraining(int_id,realmDB.createRow(
                                int_recupMin_value,
                                int_recupSec_value,
                                0,
                                0,
                                0,
                                0,
                                str_gear,
                                getResources().getString(R.string.recup),
                                "",
                                str_note));
                    }//if(bln_recup)
                }//for


            }catch (Exception e){
                Toast.makeText(TrainingRowDeatilsActivity.this, getResources().getString(R.string.add_error), Toast.LENGTH_SHORT).show();
                Log.w("ERROR_ADD",e.getMessage().toString());
            }

            //close Realm
            realmDB.close();

            Intent backtoRows = new Intent(TrainingRowDeatilsActivity.this, TrainingRowActivity.class);
            //return to the same Training
            backtoRows.putExtra("_id",int_id);
            //reorder the activity to front
            backtoRows.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(backtoRows);
            finish();
        }else{
            Toast.makeText(TrainingRowDeatilsActivity.this,str_error,Toast.LENGTH_LONG).show();
        }

    }//validateRow


    /***********************************************************************
     *
     * Name: chackValue()
     * @return bln_error
     * Goal: check if all the value are set /time, bpm, rpm, work and rythm
     *
     ************************************************************************/
    private Boolean checkValue() {
        boolean bln_error = false;
        //error string back to empty
        str_error = "";
        //check if time is bigger than 10 sec;
        if((nbrPicker_min.getValue() * 10) + nbrPicker_sec.getValue() < 10){
            str_error += getResources().getString(R.string.error_time);
            bln_error = true;
        }else{
            //if value is bigger than 10 secondes qe can set the time
            int_min_work = nbrPicker_min.getValue();
            int_sec_work = nbrPicker_sec.getValue();
        }
        //check if bpm is set
        if(txtView_bpm.getText().length() > 3 || txtView_bpm.getText().length() < 2){
            str_error = str_error + "\n"+getResources().getString(R.string.error_bpm);
            bln_error = true;
        }else{
            //set int_bpm to the value of the progressBar
            int_bpm = int_progress_bpm;
        }
        //check if rpm is set
        if(txtView_rpm.getText().length() > 3 || txtView_rpm.getText().length() < 2){
            str_error += "\n"+getResources().getString(R.string.error_rpm);
            bln_error = true;
        }else{
            //set int_rpm to the value of the progressBar
            int_rpm = int_progress_rpm;
        }
        //check if spinner work is selected
        if (spinner_work.getSelectedItem().toString() == "" || spinner_work.getSelectedItem().toString().equals(null)){
            str_error += "\n"+getResources().getString(R.string.error_work);
        }else{
            //if value is not empty set str_work to the value of th spinner
            str_work = spinner_work.getSelectedItem().toString();
        }
        //check if spinner rythm is selected
        if (spinner_rythm.getSelectedItem().toString() == "" || spinner_rythm.getSelectedItem().toString().equals(null)){
            str_error += "\n"+getResources().getString(R.string.error_rythm);
            bln_error = true;
        }else{
            //if value is not empty set str_rythm to the value of th spinner
            str_rythm = spinner_rythm.getSelectedItem().toString();
        }
        //check for rest time if it's empty
        if(txtView_restTime.getText().toString() == getResources().getString(R.string.recup)) {
            int_min_rest = 0;
            int_sec_rest = 0;
        }
        //check for the gear if it is selected or not
        if (txtView_gear.getText().toString() != getResources().getString(R.string.gear)){
            str_gear = txtView_gear.getText().toString();
        }
        //check for notes
        if(edtTxt_notes.getText().length() > 0){
            str_note = edtTxt_notes.getText().toString();
        }

        return bln_error;
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
            //validate row with the number of repetitions
            validateRow(int_count);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        freeMemory();
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
        Intent back_to_trainingRow = new Intent(TrainingRowDeatilsActivity.this, TrainingRowActivity.class);
        //put id of training as extra, needed to charge the training Row
        back_to_trainingRow.putExtra("_id",int_id);
        back_to_trainingRow.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back_to_trainingRow);
        finish();
    }
}
