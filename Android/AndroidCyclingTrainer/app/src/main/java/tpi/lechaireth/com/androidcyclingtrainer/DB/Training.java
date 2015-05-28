/***************************************************************
 * Programm  : Android Cycling Trainer
 * Society   : ETML
 * Author    : Thomas Léchaire
 * Date      : 26.05.2015
 * Goal      : Class used to backup in the realm data base all Training created
 *             by the user.
 ******************************************************************** //
 * Modifications:
 * Date       : XX.XX.XXXX
 * Author     :
 * Purpose     :
 *********************************************************************/
package tpi.lechaireth.com.androidcyclingtrainer.DB;

/* Import for the class */
import io.realm.RealmList;
import io.realm.RealmObject;

/************************************************
 *  Begining of the TrainingRow class
 **********************************************/
public class Training extends RealmObject {

    //id for the row
    private int int_id;
    //name of the training
    private String str_name;
    //creation date of the training
    private String str_day;
    //list of all row for the training
    private RealmList<TrainingRow> rlst_row;
    //int for the recup indice
    private int int_recup;
    //boolean to tell if the training is VTT or not
    private boolean bln_isVtt;

    /* Getters and Setters for all attributs */
    public int getInt_id() {return int_id;}

    public void setInt_id(int int_id) {this.int_id = int_id;}

    public int getInt_recup() {return int_recup;}

    public void setInt_recup(int int_recup) {this.int_recup = int_recup;}

    public RealmList<TrainingRow> getRlst_row() {return rlst_row;}

    public void setRlst_row(RealmList<TrainingRow> rlst_row) {this.rlst_row = rlst_row;}

    public String getStr_day() {return str_day;}

    public void setStr_day(String str_day) {this.str_day = str_day;}

    public String getStr_name() {return str_name;}

    public void setStr_name(String str_name) {this.str_name = str_name;}

    public boolean isBln_isVtt() {return bln_isVtt;}

    public void setBln_isVtt(boolean bln_isVtt) {this.bln_isVtt = bln_isVtt;}
}
