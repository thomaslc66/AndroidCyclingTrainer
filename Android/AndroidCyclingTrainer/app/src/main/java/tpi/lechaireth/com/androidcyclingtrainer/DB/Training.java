package tpi.lechaireth.com.androidcyclingtrainer.DB;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Thomas on 04.05.2015.
 */
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

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public int getInt_recup() {
        return int_recup;
    }

    public void setInt_recup(int int_recup) {
        this.int_recup = int_recup;
    }

    public RealmList<TrainingRow> getRlst_row() {
        return rlst_row;
    }

    public void setRlst_row(RealmList<TrainingRow> rlst_row) {
        this.rlst_row = rlst_row;
    }

    public String getStr_day() {
        return str_day;
    }

    public void setStr_day(String str_day) {
        this.str_day = str_day;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }
}
