package tpi.lechaireth.com.androidcyclingtrainer.DB;

import io.realm.RealmObject;

/**
 * Created by Thomas on 28.05.2015.
 */
public class HeartRate extends RealmObject {

    //id for the HeartRate
    private int int_id;
    //int heartRate Max
    private int int_fc_max;
    //int heartRate Min
    private int int_fc_min;

    public int getInt_fc_max() {
        return int_fc_max;
    }

    public void setInt_fc_max(int int_fc_max) {
        this.int_fc_max = int_fc_max;
    }

    public int getInt_fc_min() {
        return int_fc_min;
    }

    public void setInt_fc_min(int int_fc_min) {
        this.int_fc_min = int_fc_min;
    }

    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }
}//HearRate
