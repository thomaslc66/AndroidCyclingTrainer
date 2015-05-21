package tpi.lechaireth.com.androidcyclingtrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.Adapter.TrainingAdapter;
import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.Training;


/**
 * Created by Thomas on 04.05.2015.
 */
public class TrainingActivity extends ActionBarActivity {


   //UI Elements
   ListView listView_training;
   List<Training> training_list;
   Button btn_createTraining;
   private TrainingAdapter trainingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);

        final RealmDB realmDB = new RealmDB(TrainingActivity.this);
        training_list = new ArrayList<>();

        try {
            training_list = realmDB.getListofTraining();
        }catch (Exception e ){
            e.printStackTrace();
        }


        //UI Elements
        btn_createTraining = (Button) findViewById(R.id.btn_add_training);
        listView_training = (ListView) findViewById(R.id.list);
        trainingAdapter = new TrainingAdapter(this,training_list);
        listView_training.setAdapter(trainingAdapter);

        btn_createTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* initialisation of the edit text for the */
                final EditText edt_input = new EditText(TrainingActivity.this);
                edt_input.setMaxLines(1);

                new AlertDialog.Builder(TrainingActivity.this)
                        .setTitle("Ajout d'entraînement")
                        .setView(edt_input)
                        .setPositiveButton("Creer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get the name of the trining from editText
                                String value = edt_input.getText().toString();
                                //check if name is empty or to long
                                if (value == "" || value.length() > 25){
                                    Toast.makeText(TrainingActivity.this, "Name empty or to long", Toast.LENGTH_SHORT).show();

                                }else{
                                    //if name is correct
                                    if (realmDB.isUnique(value)) {
                                        realmDB.createTraining(value);
                                        //update the adapter with the new list
                                        training_list = realmDB.getListofTraining();

                                        //notifiyDataSetChanged
                                        trainingAdapter.updateLevel(training_list);
                                    } else {
                                        Toast.makeText(TrainingActivity.this, "This name is used...", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        dialog.dismiss();
                    }
                }).show();

            }
        });


    }
}
