/**
 * Created by Thomas on 01.05.2015.
 */
package tpi.lechaireth.com.androidcyclingtrainer.DB;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/***********************************
 *
 * class RealmDB
 * This class regroups all method to manage the data base and display the
 * training and row data
 *
 ************************************/
public class RealmDB {

    private Realm realm;
    private Context context;
    /**
     * Empty constructor
     */
    public RealmDB(Context context) {
        this.context = context;

    }

    //############################################################# PART FOR TRAINIG CLASS OPERATION ##############################################################################

    public boolean isUnique(String name){
        boolean notUnique = true;
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        //find all training
        RealmResults results = realm.where(Training.class).equalTo("str_name",name).findAll();//send transaction
        realm.commitTransaction();
        //Try to add a row
        if(results.size() > 0){
            notUnique = false;
        }

        return notUnique;
    }
    /*********************************************************
     *
     * createTrainig
     * @param name
     * Goal: Method used to create a new training
     *
     ********************************************************/
    public void createTraining(String name){
        int id;
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        //find all training
        RealmResults results = realm.where(Training.class).findAll();
        //find the training with the max id and get the id
        id = results.size();
        //Try to add a row
        try{
            //Transaction began before ->realm.bedinTransaction();
            //Get date of the day with the calendar
            Calendar calendar = Calendar.getInstance();
            calendar.getTime();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayMonth = calendar.get(Calendar.DAY_OF_MONTH);

            //Add a trainingRow by creating a new realmObject with the class TrainingRow
            Training training = realm.createObject(Training.class);
            //set the id and the name of the training
            training.setInt_id(id);
            //set the name
            training.setStr_name(name);
            //set the date
            training.setStr_day(dayMonth + "/" + month + "/" + year);
            //commit the transaction
            realm.commitTransaction();
            //get a Toast for confirmation
            Toast.makeText(context,"New Training Created",Toast.LENGTH_SHORT).show();
            //catch Exceptions
        }catch (Exception e){
            //get error message
            e.printStackTrace();
            //clear transaction - we don't want to add an empty or partial row
            realm.cancelTransaction();
            //get status
            Toast.makeText(context,"Error.. Training not created",Toast.LENGTH_SHORT).show();
        }//catch
    }//createTraining

    /*****************************************************************************
     *
     * addAtrainingRowToTraining
     * @param realm_training_ID
     * @param row
     * Goal: Method that add a row to a training session
     *
     * *****************************************************************************/
    public void addAtrainingRowToTraining(int realm_training_ID, TrainingRow row){
        realm = Realm.getInstance(context);
        // no transaction because it has started in createRow Method
        //Try to add a row
        try{
            //get the training
            Training training = realm.where(Training.class).equalTo("int_id",realm_training_ID).findAll().first();

            int _id = training.getRlst_row().size();
            Log.w("_ID", _id+"" );
            row.setInt_id(_id++);
            //get list and add row to it.
            training.getRlst_row().add(row);
            //commit the transaction
            realm.commitTransaction();
            //get a Toast for confirmation
            Toast.makeText(context,"Row added to training " + training.getStr_name(),Toast.LENGTH_SHORT).show();
            //catch Exceptions
        }catch (Exception e){
            //get error message
            e.printStackTrace();
            //clear transaction - we don't want to add an empty or partial row
            realm.cancelTransaction();
            //get status
            Toast.makeText(context,"Row not added to training ",Toast.LENGTH_SHORT).show();
        }//catch
    }//createRow


    /***************************************************
     * getListofTraining
     * @return List<Training>
     * Goal: this method return all the training in the database
     *
     ***************************************************/
    public List<Training> getListofTraining(){
        List<Training> t = new ArrayList<>();
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            RealmResults<Training> realmResults = realm.where(Training.class).findAll();
            for(Training item : realmResults){
                t.add(item);
            }
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
        return t;
    }


    /***************************************************
     *
     * getTraining
     * @param name
     * @return Training t
     * Goal: this method return a Training with is name
     *
     *************************************************/
    public Training getTraining(String name){
        Log.w("get Training NAME", name);
        Training t = null;
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        try {
            //searching for the training with is name
            t = realm.where(Training.class).equalTo("str_name",name).findAll().first();

            Log.w("BIKE id", ""+t.getInt_id());
            Log.w("BIKE name", ""+t.getStr_name());
            Log.w("BIKE day", ""+t.getStr_day());

            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
        return t;
    }

    /*********************************************************
     *
     * removeTrainingWithID
     * @param realm_training_ID
     * Goal: Remove the training with it's id
     *
     *******************************************************/
    public void removeTrainingWithID(int realm_training_ID){
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            Training training = realm.where(Training.class).equalTo("int_id", realm_training_ID).findAll().first();
            training.removeFromRealm();
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
        finally {
            realm.commitTransaction();
        }
    }

    /*********************************************************
     *
     * removeTrainingWithID
     * @param t
     * Goal: Remove the training with it's id
     *
     *******************************************************/
    public void removeTraining (Training t){
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            t.removeFromRealm();
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
        finally {
            realm.commitTransaction();
        }
    }


    //############################################################# PART FOR TRAINIG ROW CLASS OPERATION ##############################################################################
    /*************************************************************
     *
     * getAllTrainingRows
     * @param realm_Row_ID
     * @return a list of training Row
     * Goal: By id we get a training a we get all the row within this training
     *
     * ***********************************************************/
    public List<TrainingRow> getAllTrainingRows(int realm_Row_ID){
        Log.w("ID TRAINING", realm_Row_ID+"");
        List<TrainingRow> list_of_row;
        realm = Realm.getInstance(context);
        //start transaction
        realm.beginTransaction();
        try{
            //get the training with the id
            Training t = realm.where(Training.class).equalTo("int_id",realm_Row_ID).findAll().first();
            //then get the list contained in the training
            list_of_row = t.getRlst_row();
            realm.commitTransaction();

        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
            list_of_row = null;
        }

        return list_of_row;
    }

    /***********************************************************************
     *
     * createRow
     * @param min
     * @param sec
     * @param tour
     * @param bpm
     * Goal: method used to add a training row in the realm db
     *
     *********************************************************************/
    public TrainingRow createRow(int min, int sec, int tour, int bpm){
        TrainingRow trainingRow;
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        //Add a trainingRow by creating a new realmObject with the class TrainingRow
        trainingRow = realm.createObject(TrainingRow.class);
        trainingRow.setInt_min(min); //set min
        trainingRow.setInt_sec(sec); //set sec
        trainingRow.setInt_rpm(tour); //set tpm
        trainingRow.setInt_bpm(bpm); //set heart beat
        trainingRow.setStr_time(setTimeWithInt(min, sec)); //set time for display
        return trainingRow;
    }//createRow


    /***************
     *
     * @param realm_Row_ID
     */
    public void removeRowWithID(int realm_Row_ID){
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            TrainingRow trainingRow = realm.where(TrainingRow.class).equalTo("int_id", realm_Row_ID).findAll().first();
            trainingRow.removeFromRealm();
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
        finally {
            realm.commitTransaction();
        }
    }

    /******
     *
     * @param id
     * @return
     */
    public TrainingRow getArowWithID(int id){
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        TrainingRow trainingRow = null;
        RealmResults<TrainingRow> realmResults = realm.where(TrainingRow.class).findAll();
        try{
            if (realmResults.size() > 0){
                trainingRow = realmResults.get(id);
                realm.commitTransaction();
            }else{
                Toast.makeText(context,"Can't get Row",Toast.LENGTH_SHORT).show();
                realm.cancelTransaction();
            }
        }catch (Exception e){
            realm.commitTransaction();

        }

        return trainingRow;
    }

    /*************
     *
     * @param id
     * @return
     */
    public Training getATrainingWithID(int id){
        //get realm Instance
        realm = Realm.getInstance(context);
        //start transaction
        realm.beginTransaction();
        //Create training object
        Training training = null;

        try{
           //get all and find into the training with the id
            training = realm.where(Training.class).findAll().get(id);
            realm.commitTransaction();
        }catch (Exception e){
            Toast.makeText(context,"Can't get training",Toast.LENGTH_SHORT).show();
            realm.cancelTransaction();

        }
        //return results
        return training;
    }

    /**********
     *
     * @return
     */
    public int getSizeofTrainingRowTable(){
        realm = Realm.getInstance(context);
        int size;
        realm.beginTransaction();
        RealmResults results = realm.where(TrainingRow.class).findAll();
        size = results.size();
        realm.commitTransaction();
        return size;
    }

    /********
     *
     * @return
     */
    public int getSizeofTrainingTable(){
        realm = Realm.getInstance(context);
        int size;
        realm.beginTransaction();
        RealmResults results = realm.where(Training.class).findAll();
        size = results.size();
        realm.commitTransaction();
        return size;
    }

    /*****************************************************
     *
     * @param min
     * @param sec
     * @return String time
     * Goal: return the time in a string format
     *       set String time with zero before minus 10 number
     *******************************************************/
    private String setTimeWithInt(int min, int sec) {
        String time = "";
        Log.w("TAG SEC ", sec+"");
        if(min < 10){
            time = "0"+min;
        }else{
            time = ""+min;
        }

        if (sec < 10){
            time = time + ":"+"0"+sec;
        }else{
            time = time+":"+sec;
        }

        return time;
    }//setTimeWithInt


   public String calculateTotalTime(int id){
       String time = "00:00";
       //get a realm Instance
       realm = Realm.getInstance(context);
       //get Training with the id
       Training t = getATrainingWithID(id);
       //we get the List of Rows
       int totalSec = 0;
       int totalMin = 0;

       for(TrainingRow row : t.getRlst_row()){
           int min = row.getInt_min();
           int sec = row.getInt_sec();
           //total of minutes and seconds
           totalSec += sec;
           totalMin += min;
       }

       int hour = totalMin / 60;
       int min = totalMin % 60;
       min += totalSec / 60;
       int sec = totalSec%60;

       time = ""+hour+":"+min +":"+sec;

       return time;
   }
}