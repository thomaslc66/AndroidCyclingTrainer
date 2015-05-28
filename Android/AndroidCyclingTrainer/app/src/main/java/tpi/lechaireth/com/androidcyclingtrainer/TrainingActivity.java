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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.Adapter.TrainingAdapter;
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
    private RadioGroup radio_group;
    private AlertDialog alertDialog;
    private RadioButton rdbtn_race;
    private RadioButton rdbtn_vtt;

    //VARIABLES
    private boolean isVtt = false;
    private RealmDB realmDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);

        realmDB = new RealmDB(TrainingActivity.this);
        training_list = new ArrayList<>();

        try {
            training_list = realmDB.getListOfTraining();
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
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View training_Dialog = mInflater.inflate(R.layout.training_dialog, null);

                //set the element of the view
                edt_input = (EditText) training_Dialog.findViewById(R.id.edt_input);
                radio_group = (RadioGroup) training_Dialog.findViewById(R.id.radio_Group);
                rdbtn_race = (RadioButton) training_Dialog.findViewById(R.id.rdbtn_race);
                rdbtn_vtt = (RadioButton) training_Dialog.findViewById(R.id.rdbtn_vtt);

                radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        if (rdbtn_vtt.isChecked()) {
                            isVtt = true;
                            Toast.makeText(TrainingActivity.this, "VTT (true)", Toast.LENGTH_SHORT).show();
                        } else {
                            isVtt = false;
                            Toast.makeText(TrainingActivity.this, "Road (false)", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                AlertDialog.Builder trainingDialog_builder = new AlertDialog.Builder(TrainingActivity.this)
                        .setTitle("Ajout d'entraînement")
                        .setView(training_Dialog)
                        .setPositiveButton("Creer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get the name of the trining from editText
                                String value = edt_input.getText().toString();

                                //check if name is empty or to long
                                if (value == "" || value.length() > 25) {
                                    Toast.makeText(TrainingActivity.this, getResources().getString(R.string.error_training), Toast.LENGTH_SHORT).show();

                                } else {
                                    //if name is correct
                                    if (realmDB.isUnique(value)) {
                                        //add new training to DataBase with the boolean of the checkBox
                                        realmDB.createTraining(value, isVtt);
                                        //update the adapter with the new list
                                        training_list = realmDB.getListOfTraining();

                                        //notifiyDataSetChanged
                                        trainingAdapter.updateLevel(training_list);
                                    } else {
                                        Toast.makeText(TrainingActivity.this, "This name is used...", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                                dialog.dismiss();
                            }
                        });

                //create the builder
                alertDialog = trainingDialog_builder.create();
                alertDialog.show();

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
            Toast.makeText(this,"Fréquences Cardiaques", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //close Realm Instance
        realmDB.close();
    }

}//TrainingActivity
