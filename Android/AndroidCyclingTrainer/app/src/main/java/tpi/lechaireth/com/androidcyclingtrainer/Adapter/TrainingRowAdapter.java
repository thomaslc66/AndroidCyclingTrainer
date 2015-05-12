package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;
import tpi.lechaireth.com.androidcyclingtrainer.R;


/**
 * Created by Thomas on 05.05.2015.
 */
public class TrainingRowAdapter extends BaseAdapter {

    private int int_training_id;
    private List<TrainingRow> trainingRowList;
    private Context context;
    private LayoutInflater mInflater;

    public TrainingRowAdapter(Context context, List<TrainingRow> trainingRowList, int int_training_id ){
        this.context = context;
        this.trainingRowList = trainingRowList;
        this.int_training_id = int_training_id;
        this.mInflater = LayoutInflater.from(context);
    }


    public void updateLevel(List<TrainingRow> mTrainingRow){
        ThreadPreconditions.checkOnMainThread();
        this.trainingRowList = mTrainingRow;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = 0;
        if (trainingRowList != null){
            size = trainingRowList.size();
        }
        return size;
    }

    @Override
    public TrainingRow getItem(int position) {
        return trainingRowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getInt_id();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtView_bpm, txtView_rpm, txtView_time, txtView_id;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.training_row_list_item,parent,false);
            //set name and date to view
            txtView_bpm = (TextView) convertView.findViewById(R.id.txtView_bpm);
            txtView_rpm = (TextView) convertView.findViewById(R.id.txtView_rpm);
            txtView_time = (TextView) convertView.findViewById(R.id.txtView_time);
            txtView_id = (TextView) convertView.findViewById(R.id.txtView_id);
            //create new view Holder
            convertView.setTag(new ViewHolder(txtView_time,txtView_bpm,txtView_rpm, txtView_id));
        }
        /* ConvertView will be not null when ListView is asking to recycle the row */
        else{

            //add touch listener to track swipe motion

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            txtView_bpm = viewHolder.txtView_bpm;
            txtView_rpm = viewHolder.txtView_rpm;
            txtView_time = viewHolder.txtView_time;
            txtView_id  = viewHolder.txtView_id;
        }

        //set the value to each row
        txtView_bpm.setText(trainingRowList.get(position).getInt_batpm()+"");
        txtView_rpm.setText(trainingRowList.get(position).getInt_tourpm()+"");
        txtView_time.setText(trainingRowList.get(position).getStr_time()+"");
        txtView_id.setText(trainingRowList.get(position).getInt_id()+"");


        return convertView;
    }



    private class ViewHolder{
        TextView txtView_time, txtView_bpm, txtView_rpm,txtView_id;

        //Constructor for the ViewHolder
        private ViewHolder(TextView txtView_time, TextView txtView_bpm, TextView txtView_rpm, TextView txtView_id) {
            this.txtView_time = txtView_time;
            this.txtView_bpm = txtView_bpm;
            this.txtView_rpm = txtView_rpm;
            this.txtView_id = txtView_id;
        }
    }
}
