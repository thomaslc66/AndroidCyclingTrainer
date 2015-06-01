package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.Adapter.TrainingAdapter;
import tpi.lechaireth.com.androidcyclingtrainer.DB.HeartRate;
import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.Training;


/**
 * Created by Thomas on 27.05.2015.
 */
public class TrainingActivity extends ActionBarActivity {


    //UI Elements
    //The list View
    private ListView listView_training;

    //the list of training
    private List<Training> training_list;

    //Buton to create Training
    private Button btn_createTraining;
    private TrainingAdapter trainingAdapter;
    private EditText edt_input;
    private EditText edtTxt_fc_min, edtTxt_fc_max;
    private RadioGroup radio_group;
    private AlertDialog alertDialog;
    private RadioButton rdbtn_vtt;
    private TextView txtView_textWatcher;

    //VARIABLES
    private boolean isVtt = false;
    private RealmDB realmDB;
    private boolean isHeartBeat;

    //CONSTANTS
    private  static int INT_MAX_LENGTH = 20;
    private static int INT_MAX_LENGTH_BPM_RPM = 3;
    private static int INT_ZERO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);

        realmDB = new RealmDB(TrainingActivity.this);
        training_list = new ArrayList<>();

        try {
            training_list = realmDB.getListOfTraining();
            //get isHeartRate value from DB
            if(realmDB.getisHeartRate()){ //if return true
                isHeartBeat = true;
            }else{
                isHeartBeat = false;
            }
        }catch (Exception e ){
            e.printStackTrace();
        }


        //UI Elements
        btn_createTraining = (Button) findViewById(R.id.btn_add_training);
        listView_training = (ListView) findViewById(R.id.listView_traing);
        trainingAdapter = new TrainingAdapter(this,training_list);
        listView_training.setAdapter(trainingAdapter);

        /* onClickListener on btn_createTraining */
        btn_createTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to check if HeartBeat are filled
                if(!isHeartBeat){
                    Toast.makeText(TrainingActivity.this,getString(R.string.add_fc),Toast.LENGTH_LONG).show();
                }else{ //if isHeartBeat = true means that HeartRate are filled
                    LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View training_Dialog = mInflater.inflate(R.layout.training_dialog, null);

                    //set is Vtt to false because by default Road is choosen
                    isVtt = false;

                    //set the element of the view
                    edt_input = (EditText) training_Dialog.findViewById(R.id.edt_input);
                    radio_group = (RadioGroup) training_Dialog.findViewById(R.id.radio_Group);
                    rdbtn_vtt = (RadioButton) training_Dialog.findViewById(R.id.rdbtn_vtt);
                    txtView_textWatcher = (TextView) training_Dialog.findViewById(R.id.txtView_textWatcher);

                    txtView_textWatcher.setText(String.valueOf(INT_MAX_LENGTH));

                    //Create a TextWatcher for the editText input
                    TextWatcher mTextWatcher_forEdt_input = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            int int_nbr_letter = INT_MAX_LENGTH-charSequence.length();
                            //value is ok
                            if(int_nbr_letter >= INT_ZERO) {
                                txtView_textWatcher.setTextColor(getResources().getColor(R.color.numbers_text_color));
                                txtView_textWatcher.setText(String.valueOf(int_nbr_letter));
                            }
                            //text length is to big
                            else if(int_nbr_letter < INT_ZERO){
                                //set color to red
                                txtView_textWatcher.setTextColor(getResources().getColor(R.color.red));
                                txtView_textWatcher.setText(String.valueOf(int_nbr_letter));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    };

                    /* set On Focus Change Listener to open keyBord when editText for the training Name has focus*/
                    edt_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            edt_input.post(new Runnable() {
                                @Override
                                public void run() {
                                    //Make KeyBoard Visible
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(edt_input, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                        }
                    });

                    // set cursor into edit Text
                    edt_input.requestFocus();

                    /* link text Watcher to editText */
                    edt_input.addTextChangedListener(mTextWatcher_forEdt_input);

                    /* onCheckChange listener on the radio Group */
                    radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            if (rdbtn_vtt.isChecked()) {
                                isVtt = true;
                                Toast.makeText(TrainingActivity.this, "VTT", Toast.LENGTH_SHORT).show();
                            } else {
                                isVtt = false;
                                Toast.makeText(TrainingActivity.this, "Route", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    /* Build the AlertDialog with specific information from the layout */
                    AlertDialog.Builder trainingDialog_builder = new AlertDialog.Builder(TrainingActivity.this)
                            .setTitle(getString(R.string.add_training))
                            .setView(training_Dialog)
                            .setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                        /* we use a Custum Listener see the class */
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                    dialog.dismiss();
                                }
                            });

                    //create and show the builder
                    alertDialog = trainingDialog_builder.create();
                    alertDialog.show();


                    //link Button to the positive Button of the alertDialog
                    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    //link Button to Custom Listener
                    positiveButton.setOnClickListener(new CheckValueTrainingListener(alertDialog));
                }//else
            }
        });

    }//onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_fc) {
            Toast.makeText(this,getString(R.string.HeartRate), Toast.LENGTH_SHORT).show();
            //get the value
            HeartRate fc = realmDB.getHeartRate();

            //Inflate Layout
            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View bpm_dialog = mInflater.inflate(R.layout.bpm_dialog, null);

            edtTxt_fc_max = (EditText) bpm_dialog.findViewById(R.id.edtTxt_fc_max);
            edtTxt_fc_min = (EditText) bpm_dialog.findViewById(R.id.edtTxt_fc_min);

            //set keyboard to numeric
            edtTxt_fc_max.setRawInputType(Configuration.KEYBOARD_QWERTY);
            edtTxt_fc_min.setRawInputType(Configuration.KEYBOARD_QWERTY);

            /* set On Focus Change Listener to open keyBord when editText fc_max has focus*/
            edtTxt_fc_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    edtTxt_fc_max.post(new Runnable() {
                        @Override
                        public void run() {
                            //Make KeyBoard Visible
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(edtTxt_fc_max, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            });

            // set cursor into edit Text
            edtTxt_fc_max.requestFocus();

            //if fc is not empty. set the value
            if(fc != null){
                //fill the edit text With the last saved values
                edtTxt_fc_max.setText(fc.getInt_fc_max()+"");
                edtTxt_fc_min.setText(fc.getInt_fc_min()+"");
            }

            //Build AlerDialog
            AlertDialog.Builder bpm_dialogBuilder = new AlertDialog.Builder(TrainingActivity.this)
                    .setTitle(getString(R.string.HeartRate))
                    .setView(bpm_dialog)
                    .setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        /* All happend in the CheckValueHeartRateListener Class */
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                            dialog.dismiss();
                        }
                    });

            //create the builder
            alertDialog = bpm_dialogBuilder.create();
            alertDialog.show();

            //link Button to the positive Button of the alertDialog
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            //link Button to Custom Listener
            positiveButton.setOnClickListener(new CheckValueHeartRateListener(alertDialog));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    /********************************************************************
     * Name: onStop Method
     * Goal: Method called when the phone stop the Avtivity
     ***********************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        //close Realm Instance
        realmDB.close();
    }//onStop

    /********************************************************************
     * Name: onDestroy Method
     * Goal: Method called when the phone kill the Avtivity
     ***********************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        training_list = null;
        listView_training = null;
        txtView_textWatcher = null;
        freeMemory();
    }//onDestroy

    /********************************************************************
     * Name: freeMemory
     * Goal: Run the garbage collector to get back some allocated memory
     ***********************************************************************/
    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }//freeMemory

    /********************************************************************
     * Name: isNumeric
     * Goal: Method to check if the string is numeric or not
     ***********************************************************************/
    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }


    /*******************************************************************************
     *
     * private class Custom Listener to check data control on the dialog_training box
     *
     **********************************************************************************/
    class CheckValueTrainingListener implements View.OnClickListener{
        private final Dialog dialog;
        public CheckValueTrainingListener (Dialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            //get the name of the trining from editText
            String mValue = edt_input.getText().toString();
            //if value length is ok
            if(mValue.length() <= INT_MAX_LENGTH){
                //if name is correct
                if (realmDB.isUnique(mValue)) {
                    //add new training to DataBase with the boolean of the checkBox
                    realmDB.createTraining(mValue, isVtt);
                    //update the adapter with the new list
                    training_list = realmDB.getListOfTraining();

                    //notifiyDataSetChanged
                    trainingAdapter.updateLevel(training_list);
                    //everything is ok dismiss the dialog
                    dialog.dismiss();
                } else { //if name is already used
                    Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_training_isUnique), Toast.LENGTH_SHORT).show();
                }
            }//if
            else if(mValue.length() == INT_ZERO){ //if value is empty
                Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_training_empty), Toast.LENGTH_SHORT).show();
            }
            else if(mValue.length() > INT_MAX_LENGTH){ //if value is too big
                Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_training_length), Toast.LENGTH_SHORT).show();
            }
        }
    }// class CheckValueTrainingListener

    /*******************************************************************************
     *
     * private class Custom Listener to check data control on the dialog_heartBeat box
     *
     **********************************************************************************/
    class CheckValueHeartRateListener implements View.OnClickListener{
        private final Dialog dialog;
        public CheckValueHeartRateListener (Dialog dialog){
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            //get values from both edit Text
            String value_max = edtTxt_fc_max.getText().toString();
            String value_min = edtTxt_fc_min.getText().toString();

            //if value is empty
            if(value_max.length() == INT_ZERO || value_min.length() == INT_ZERO){
                Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_bpm_empty), Toast.LENGTH_SHORT).show();
            }
            else if(value_max.length() > INT_MAX_LENGTH_BPM_RPM || value_min.length() > INT_MAX_LENGTH_BPM_RPM){ //if value is to big
                Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_bpm_length), Toast.LENGTH_SHORT).show();
            }
            //if value length is ok
            else if(value_max.length() <= INT_MAX_LENGTH_BPM_RPM && value_min.length() <= INT_MAX_LENGTH_BPM_RPM ) {
                //check if value is numeric
                if (isNumeric(value_min) && isNumeric(value_max) ){
                    //if the values are correct save the new value in the same
                    realmDB.saveHeartRate(Integer.parseInt(value_max), Integer.parseInt(value_min));
                    //everything is ok dismiss the dialog
                    dialog.dismiss();
                    //boolean isHeartBeat
                    isHeartBeat = true;
                }//if isNumeric
                else{
                    Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_numeric), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }// class CheckValueHeartRateListener
}//class TrainingActivity
