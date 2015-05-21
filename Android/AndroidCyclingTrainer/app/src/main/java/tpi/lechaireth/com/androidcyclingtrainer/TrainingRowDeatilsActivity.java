package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import org.w3c.dom.Text;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.library.VerticalSeekBar;


/**
 * Created by Thomas on 05.05.2015.
 */
public class TrainingRowDeatilsActivity extends ActionBarActivity {

    //UI Elements
    private EditText edtTxt_sec, edtTxt_tour, edtTxt_Bpm;
    private TextView txtView_plates;
    private Button btn_validate_row;
    private VerticalSeekBar verticlalBar_rpm, verticalBar_bpm;
    private Spinner spinner_work,spinner_rythm;
    private String[] tab_work;
    private NumberPicker nbrPicker_front, nbrPicker_back;
    private AlertDialog alertDialog;
    private NumberPicker nbrPicker_min,nbrPicker_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_row_details);

        //get data from last intent
        final int _id = getIntent().getIntExtra("_id",0);

        //UI Elements Initialisation
        edtTxt_Bpm = (EditText) findViewById(R.id.edtTxt_bpm);
        edtTxt_tour = (EditText) findViewById(R.id.edtTxt_tour);

        //initialisation of the two number pickers for the time
        nbrPicker_min = ( NumberPicker) findViewById(R.id.nbrPricker_min);
        nbrPicker_sec = (NumberPicker) findViewById(R.id.nbrPicker_sec);
        nbrPicker_min.setMaxValue(59);
        nbrPicker_sec.setMaxValue(59);

        /* Initialisation of the Button for the validation */
        btn_validate_row = (Button) findViewById(R.id.btn_validate_row);

        /* Initialisation of the 2 VerticalSeekBar*/
        verticlalBar_rpm = (VerticalSeekBar) findViewById(R.id.verticalBar_rpm);
        verticalBar_bpm = (VerticalSeekBar) findViewById(R.id.verticalBar_bpm);

        /* Initialisation of the 2 spinners */
        spinner_work = (Spinner) findViewById(R.id.spinner_travail);
        spinner_rythm = (Spinner) findViewById(R.id.spinner_rythme);

        ArrayAdapter<CharSequence> adapter_work = ArrayAdapter.createFromResource(this,R.array.work, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_rythm = ArrayAdapter.createFromResource(this,R.array.rythm, android.R.layout.simple_spinner_item);

        spinner_work.setAdapter(adapter_work);
        spinner_rythm.setAdapter(adapter_rythm);

        //chose the plates
        txtView_plates = (TextView) findViewById(R.id.txtView_plate);


        txtView_plates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater mInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v1 = mInflater.inflate(R.layout.plates_dialog, null);

                AlertDialog.Builder alrt_builder = new AlertDialog.Builder(TrainingRowDeatilsActivity.this);
                alrt_builder.setView(v1);
                alrt_builder.setTitle("Choix des plateaux");
                alrt_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int int_front_value, int_back_value;
                        //numberPickers
                        int_front_value = nbrPicker_front.getValue();
                        int_back_value = nbrPicker_back.getValue();
                        txtView_plates.setText(int_front_value + "X" + int_back_value);
                    }
                });

                alertDialog = alrt_builder.create();


                //initialisation of the two number pickers for the plates
                nbrPicker_front = ( NumberPicker) v1.findViewById(R.id.nbPicker_front);
                nbrPicker_back = (NumberPicker) v1.findViewById(R.id.nbPicker_back);

                nbrPicker_front.setWrapSelectorWheel(false);
                nbrPicker_back.setWrapSelectorWheel(true);

                nbrPicker_back.setMaxValue(60);
                nbrPicker_front.setMaxValue(60);

                alertDialog.show();
            }
        });

        //onClickListener Validate
        btn_validate_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int bpm, min,sec,tour;
                bpm = Integer.parseInt(edtTxt_Bpm.getText().toString());
                min = nbrPicker_min.getValue();
                sec = nbrPicker_sec.getValue();
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
                edtTxt_Bpm.setText((progress+40)+"");
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
                edtTxt_tour.setText((progress+30)+"");
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
