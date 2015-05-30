/***************************************************************
 * Programm  : Android Cycling Trainer
 * Society   : ETML
 * Author    : Thomas Léchaire
 * Date      : 26.05.2015
 * Goal      : This class regroups all method to manage the data base
 ******************************************************************** //
 * Modifications:
 * Date       : XX.XX.XXXX
 * Author     :
 * Purpose    :
 *********************************************************************/
package tpi.lechaireth.com.androidcyclingtrainer.DB;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import tpi.lechaireth.com.androidcyclingtrainer.R;


/***********************************
 * begining of the class RealmDB
 ************************************/
public class RealmDB {

    private Realm realm;
    private Context context;

    /*********************************************************
     * Name: -> Constructor for the class
     * @param context
     * return: empty
     * Goal: create a new instance of RealmDB to acces all the management method
     *
     ********************************************************/
    public RealmDB(Context context) {
        //get the context of the Activity and set it to the RealmDB object
        this.context = context;
    }

    public void close(){
        //close realm instance
        realm.close();
    }

    //############################################################# PART FOR TRAINING CLASS OPERATION ##############################################################################

    /*********************************************************
     *
     * Name: isUnique
     * @param name
     * @return boolean notUnique
     * Goal: check if the name is already taken
     *
     ********************************************************/
    public boolean isUnique(String name){
        boolean isNotUnique = true;
        //get a realm Instance
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        //find all training equal to the name we passed in parameters (str_name)
        RealmResults results = realm.where(Training.class).equalTo("str_name", name).findAll();//send transaction
        //end of transaction
        realm.commitTransaction();
        //check if the size of the result table is bigger than 0
        //if it is that means there is already and training named the same
        if(results.size() > 0){
            isNotUnique = false;
        }

        return isNotUnique;
    }
    /*********************************************************
     *
     * Name: createTrainig
     * @param name
     * Goal: Method used to create a new training
     *
     ********************************************************/
    public void createTraining(String name, boolean isVtt){
        int id;
        //get a realm Instance
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
            //training set isVTT
            training.setBln_isVtt(isVtt);
            //commit the transaction
            realm.commitTransaction();
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
        int _id = 0;
        //get a realm Instance
        realm = Realm.getInstance(context);
        // no transaction because it has started in createRow Method
        //Try to add a row
        try{
            //get the training with realm_training_ID
            Training training = realm.where(Training.class).equalTo("int_id",realm_training_ID).findAll().first();
            //From this training get the List of TrainingRow.
            RealmList<TrainingRow> trainingRows = training.getRlst_row();

            //get list and add row to it.
            training.getRlst_row().add(row);
            //commit the transaction
            realm.commitTransaction();
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
     * Name: getListOfTraining
     * @return List<Training>
     * Goal: this method return all the training in the database
     *
     ***************************************************/
    public List<Training> getListOfTraining(){
        //arrayList for the return
        List<Training> t = new ArrayList<>();
        //get a realm Instance
        realm = Realm.getInstance(context);
        //start transaction
        realm.beginTransaction();
        try {
            //get all the training from the data base
            RealmResults<Training> realmResults = realm.where(Training.class).findAll();
            //pass all the training from a RealmReasults list to a ArrayList
            for(Training item : realmResults){
                //add item in the ArrayList t
                t.add(item);
            }//for
            //commit Transaction
            realm.commitTransaction();

        }catch (Exception e){ //catch Exceptions
            e.printStackTrace();
            //in case of error cancel the transaction
            realm.cancelTransaction();
        }

        //return the list
        return t;
    }


    /***************************************************
     *
     * Name: getTraining
     * @param name
     * @return Training t
     * Goal: this method return a Training with is name
     *
     *************************************************/
    public Training getTraining(String name){
        //Create Variable
        Training t = null;
        //get a realm Instance
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        try {
            //searching for the training with is name
            t = realm.where(Training.class).equalTo("str_name", name).findAll().first();
            //commit the transaction
            realm.commitTransaction();
        }catch (Exception e){ //catch Exceptions
            e.printStackTrace();
            //in cas of error cancel the transaction
            realm.cancelTransaction();
        }

        //return the training
        return t;
    }

    /*********************************************************
     *
     * removeTrainingWithID
     * @param realm_training_ID
     * Goal: Remove the training with his id
     * NOT USED - NEEDED TIME TO ADD IT IN THE CODE
     *
     *******************************************************/
    public void removeTrainingWithID(int realm_training_ID){
        //get a realm Instance
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        try {
            //find the training with his id
            Training training = realm.where(Training.class).equalTo("int_id", realm_training_ID).findAll().first();
            //remove the training from the DB
            training.removeFromRealm();
            //commit
            realm.commitTransaction();
        //catch Exceptions
        }catch (Exception e){
            e.printStackTrace();
            //cancel transaction
            realm.cancelTransaction();
        }

    }

    /**********************************************
     *
     * Name: getATrainingWithID
     * @param id
     * @return Training
     * Goal: find and return a training using his ID
     *
     ********************************************/
    public Training getATrainingWithID(int id){
        //get a realm Instance
        realm = Realm.getInstance(context);
        //start transaction
        realm.beginTransaction();
        //Create training object
        Training training = null;

        try{
            //get all and find into the training with the id
            training = realm.where(Training.class).equalTo("int_id",id).findAll().first();
            //commit
            realm.commitTransaction();
        }catch (Exception e){
            Toast.makeText(context,context.getResources().getString(R.string.error_recup_training),Toast.LENGTH_SHORT).show();
            realm.cancelTransaction();

        }

        //return results
        return training;
    }

    /*********************************************************
     *
     * Name: removeTrainingWithID
     * @param t
     * Goal: Remove the training passed in parameter
     *
     *******************************************************/
    public void removeTraining (Training t){
        //get a realm Instance
        realm = Realm.getInstance(context);
        //start transaction to facilitate multithreading
        realm.beginTransaction();
        try {
            //remove the training from the DB
            t.removeFromRealm();
            //commit the transaction
            realm.commitTransaction();
        }catch (Exception e){ //catch Exceptions
            //print error message
            e.printStackTrace();
            //if and Exceptions is catched we need to cancel the transaction
            realm.cancelTransaction();
        }

    }


    //############################################################# PART FOR TRAINIG ROW CLASS OPERATION ##############################################################################

    /*************************************************************
     *
     * Name: getAllTrainingRows
     * @param realm_training_id
     * @return a list of training Row
     * Goal: By id we get a training a we get all the row within this training
     *
     * ***********************************************************/
    public List<TrainingRow> getAllTrainingRows(int realm_training_id){
        Log.w("ID TRAINING", realm_training_id+"");
        List<TrainingRow> list_of_row;
        //get a realm Instance
        realm = Realm.getInstance(context);
        //start transaction
        realm.beginTransaction();
        try{
            //get the training with the id
            Training t = realm.where(Training.class).equalTo("int_id",realm_training_id).findAll().first();
            //then get the list of row contained in the training
            list_of_row = t.getRlst_row();
            //commit the transcation
            realm.commitTransaction();
        }catch (Exception e){ //catch Exceptions
            //print error message in logcat
            e.printStackTrace();
            //if Exception is catched cancel the transaction
            realm.cancelTransaction();
            //and set the list to null
            list_of_row = null;
        }

        //return the list of all rows
        return list_of_row;
    }

    /********************************************************************
     *
     * Name: createRow
     * @param min_work
     * @param sec_work
     * @param tour
     * @param bpm
     * @param min_recup
     * @param sec_recup
     * @param str_gear
     * @param str_work
     * @param str_rythm
     * @param str_note
     * @return TrainingRow
     * Goal: method used to add a training row in the realm db
     *
     ******************************************************************/
    public TrainingRow createRow(int min_work, int sec_work, int tour, int bpm, int min_recup, int sec_recup, String str_gear, String str_work, String str_rythm, String str_note ){
        //get a realm Instance
        realm = Realm.getInstance(context);
        //All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        //Try to add a row
        //Add a trainingRow by creating a new realmObject with the class TrainingRow
        TrainingRow trainingRow = realm.createObject(TrainingRow.class);
        // increatement index
        int nextID = (int) (realm.where(TrainingRow.class).maximumInt("id") + 1);

        //set id
        trainingRow.setId(nextID);
        //set min
        trainingRow.setInt_min(min_work);
        //set sec
        trainingRow.setInt_sec(sec_work);
        //set tpm
        trainingRow.setInt_rpm(tour);
        //set heart beat
        trainingRow.setInt_bpm(bpm);
        // set min for rest time
        trainingRow.setInt_min_rest(min_recup);
        // set seconds for rest time
        trainingRow.setInt_sec_rest(sec_recup);
        // set String for the work
        trainingRow.setStr_work(str_work);
        // set String for the rythm
        trainingRow.setStr_rythm(str_rythm);
        // set String for the front and black gear
        trainingRow.setStr_gear(str_gear);
        // set String for the note
        trainingRow.setStr_note(str_note);
        // set String for the work time, used for the display
        trainingRow.setStr_time(setTimeWithInt(min_work, sec_work));
        // set String for the rest time, used for the display
        trainingRow.setStr_time_rest(setTimeWithInt(min_recup, sec_recup));

        //return the traing row freshly created+
        return trainingRow;
    }//createRow


    /*****************************************************************************
     *
     * Name: removeRowWithID
     * @param realm_Row_ID
     * Goal: method used to remove a row from the training
     *
     *****************************************************************************/
    public void removeRowWithID(int realm_Row_ID){
        //get instance of Realm
        realm = Realm.getInstance(context);
        //begin a transaction to facilitate safe multi threading
        realm.beginTransaction();
        try {
            //find the training row equal to the id passed in parameter
            TrainingRow trainingRow = realm.where(TrainingRow.class).equalTo("id", realm_Row_ID).findAll().first();
            //remove the row
            trainingRow.removeFromRealm();
            //commit the transaction
            realm.commitTransaction();
        }catch (Exception e){ //catch Exceptions
            e.printStackTrace();// print error message in logcat
            //if Exception is catched cancel the transaction
            realm.cancelTransaction();
        }

    }

    /*********************************************************
     *
     * Name: removeTrainingRow
     * @param t
     * Goal: Remove the trainingRow passed in parameter
     *
     *******************************************************/
    public void removeTrainingRow (TrainingRow t){
        //get a realm Instance
        realm = Realm.getInstance(context);
        //start transaction to facilitate multithreading
        realm.beginTransaction();
        try {
            //remove the training from the DB
            t.removeFromRealm();
            //commit the transaction
            realm.commitTransaction();
        }catch (Exception e){ //catch Exceptions
            //print error message
            e.printStackTrace();
            //if and Exceptions is catched we need to cancel the transaction
            realm.cancelTransaction();
        }

    }

    /***************************************************
     *
     * Name: getArowWithID
     * @param id
     * @return
     *
     **************************************************/
    public TrainingRow getArowWithID(int id){
        TrainingRow trainingRow;
        //get a realm Instance
        realm = Realm.getInstance(context);
        realm.beginTransaction();
        try{
            trainingRow = realm.where(TrainingRow.class).equalTo("id", id).findAll().first();
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
            trainingRow = null;
        }

        return trainingRow;
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

    /***
     *
     * @param id
     * @return
     *
     */
    public List<Integer> calculateTotalMinAndSec(int id){
        List<Integer> lst_time = new ArrayList<>();
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
        //adding the two int in the ArrayList
        lst_time.add(totalMin);
        lst_time.add(totalSec);

        //return the arrayList
        return lst_time;
    }


    //####################################### PART FOR THE HEARTRATE CLASS ###################################################

    public void saveHeartRate(int fc_max,int fc_min){

        HeartRate fc_return;
        //get a realm Instance
        realm = Realm.getInstance(context);

        //begin a transaction
        realm.beginTransaction();

        //try to create a Realm Object HeartRate
        try {
            RealmResults<HeartRate> results = realm.where(HeartRate.class).equalTo("int_id", 0).findAll();

            //if results.size < 1 that means that there is not HeartRate Backuped
            if (results.size() < 1){
                //Create a new HeartRate object to backup Data
                HeartRate fc = realm.createObject(HeartRate.class);
                //set id = 0
                fc.setInt_id(0);
                //set fc_max and fC_min
                fc.setInt_fc_max(fc_max);

                fc.setInt_fc_min(fc_min);
                //don't need to get the fc back because it's the first time we
                fc_return = null;
            }else{ /* Here update the current row */
                //get the id 0 for modification;
                fc_return = realm.where(HeartRate.class).equalTo("int_id", 0).findAll().first();
                fc_return.setInt_fc_min(fc_min);
                fc_return.setInt_fc_max(fc_max);
                fc_return.setInt_id(0);
               }

            //commit one of the both transaction
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            //cancel transaction if try not succeded
            realm.cancelTransaction();
            //fc_return is empty because an Exception is raised
        }
    }//saveHeartRate

    public HeartRate getHeartRate(){
        HeartRate fc_return;
        //get a realm Instance
        realm = Realm.getInstance(context);
        //begin a transaction
        realm.beginTransaction();

        try{
            //get the id 0;
            fc_return = realm.where(HeartRate.class).equalTo("int_id", 0).findAll().first();
            //commit transaction
            realm.commitTransaction();
        }catch (Exception e){
            //raise Exception and print message
            e.printStackTrace();
            //cancel realm transaction
            realm.cancelTransaction();
            //fc_return is empty
            fc_return = null;
           }
        return fc_return;
    }
}