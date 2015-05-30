package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;
import tpi.lechaireth.com.androidcyclingtrainer.library.VerticalSeekBar;

/**
 * Created by Thomas on 30.05.15.
 */
public class TrainingRowModification extends Activity{

    //UI Elements
    private TextView txtView_gear, txtView_rpm, txtView_bpm, txtView_restTime;
    private Button btn_rep;
    private VerticalSeekBar verticlalBar_rpm, verticalBar_bpm;
    private Spinner spinner_work,spinner_rythm;
    private NumberPicker nbrPicker_front, nbrPicker_back, nbrPicker_recup_min, nbrPicker_recup_sec;
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
    private int int_id,int_recupMin_value,int_recupSec_value, int_rowId;
    private int int_count = 1;
    private String str_work = "";
    private String str_rythm = "";
    private String str_note = "";
    private String str_gear = "";
    private String str_error;
    private boolean bln_recup = false;

    //OBJECT
    private RealmDB realmdb;
    private TrainingRow tRow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row_details);

        //create realm Db object
        realmdb = new RealmDB(this);

        int_rowId = getIntent().getIntExtra("row_id",0);

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
                AlertDialog.Builder alrt_builder = new AlertDialog.Builder(TrainingRowModification.this);
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
                AlertDialog.Builder alrt_builder = new AlertDialog.Builder(TrainingRowModification.this);
                //set the view of the Dialog
                alrt_builder.setView(v1);
                alrt_builder.setTitle(getResources().getString(R.string.choice_braquets));
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


        /* Method for the seek bar change listener for bpm bar*/
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

        /* Method for the seek bar change listener for rpm bar */
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


        tRow = realmdb.getArowWithID(int_rowId);

        if(tRow != null){

            //Create a triningRow
            int bpm = tRow.getInt_bpm();
            int rpm = tRow.getInt_rpm();
            int min = tRow.getInt_min();
            int sec = tRow.getInt_sec();
            int min_rest = tRow.getInt_min_rest();
            int sec_rest = tRow.getInt_sec_rest();
            String gear = tRow.getStr_gear();
            String note = tRow.getStr_note();
            String work = tRow.getStr_work();
            String rythm = tRow.getStr_rythm();
            String time = tRow.getStr_time();

            //disable the rep button because it's only a modification of one Row
            btn_rep.setEnabled(false);

            //assign all value to TextView
            //Rpm and BPM
            verticalBar_bpm.setProgress(bpm);
            verticlalBar_rpm.setProgress(rpm);
            txtView_bpm.setText(""+bpm);
            txtView_rpm.setText(""+rpm);

            //Time
            nbrPicker_min.setValue(min);
            nbrPicker_sec.setValue(sec);

            //Rest

            //gear
            txtView_gear.setText(gear);

            //work and rythm
            int pos_rythm = adapter_rythm.getPosition(rythm);
            int pos_work = adapter_work.getPosition(work);
            spinner_rythm.setSelection(pos_rythm);
            spinner_work.setSelection(pos_work);

        }

    }//onCreate


}
