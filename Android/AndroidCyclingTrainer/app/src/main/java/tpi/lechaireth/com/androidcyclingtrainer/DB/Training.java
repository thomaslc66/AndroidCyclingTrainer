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
    private String dte_day;
    //list of all row for the training
    private RealmList<TrainingRow> rlst_row;

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getDte_day() {
        return dte_day;
    }

    public void setDte_day(String dte_day) {
        this.dte_day = dte_day;
    }

    public RealmList<TrainingRow> getRlst_row() {
        return rlst_row;
    }

    public void setRlst_row(RealmList<TrainingRow> rlst_row) {
        this.rlst_row = rlst_row;
    }
}
