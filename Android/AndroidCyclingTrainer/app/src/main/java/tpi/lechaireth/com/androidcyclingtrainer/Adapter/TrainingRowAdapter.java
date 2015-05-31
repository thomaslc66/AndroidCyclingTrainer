package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.TrainingRow;
import tpi.lechaireth.com.androidcyclingtrainer.R;
import tpi.lechaireth.com.androidcyclingtrainer.TrainingRowModification;


/**
 * Created by Thomas on 05.05.2015.
 */
public class TrainingRowAdapter extends BaseSwipeAdapter {

    private int int_training_id;
    /* List of all TrainingRow object */
    private List<TrainingRow> trainingRowList;
    //context of the Activity
    private Context mContext;
    private LayoutInflater mInflater;
    private int row_id;

    //constructor
    public TrainingRowAdapter(Context context, List<TrainingRow> trainingRowList, int int_training_id ){
        this.mContext = context;
        this.trainingRowList = trainingRowList;
        this.int_training_id = int_training_id;
        this.mInflater = LayoutInflater.from(context);
        this.row_id = 0;
    }

    /***************************************************************************
     *
     * updateLevel()
     * @param mTrainingRow
     * Goal: Method that update the listView with the new data
     *
     **************************************************************************/
    public void updateLevel(List<TrainingRow> mTrainingRow){
        ThreadPreconditions.checkOnMainThread();
        this.trainingRowList = mTrainingRow;
        notifyDataSetChanged();
    }

    /*************************************************************************
     *
     * getCount() - overrided method form the AdapterClass
     * @return size
     * Goal: method tha count the size of the trainingRow list
     *
     ************************************************************************/
    @Override
    public int getCount() {
        int size = 0;
        if (trainingRowList != null){
            size = trainingRowList.size();
        }
        return size;
    }

    /**************************************************************************
     *
     * getItem() - Overrided method from the AdapterClass
     * @param position
     * @return id
     * Goal: method that returns a TrainingRow
     *
     *************************************************************************/
    @Override
    public TrainingRow getItem(int position) {
        return trainingRowList.get(position);
    }

    /**************************************************************************
     *
     * getItemId() - Overrided method from the AdapterClass
     * @param position
     * @return id
     * Goal: returns position
     *
     *************************************************************************/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /***********************************************************************
     *
     * getSwipeLayoutResourceID
     * @param i
     * @return R.id.swipe
     * Goal: set the id of the layout
     *
     ***********************************************************************/
    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    /*******************************************************************
     *
     * generateView, overrided method from the Swipe Library
     * @param position
     * @param viewGroup
     * @return
     * Goal: Method to generate the view for one row
     *
     *********************************************************************/
    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        //inflate the view
        View v = mInflater.inflate(R.layout.training_row_list_item,null);
        //new ViewHolder.
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtView_time = (TextView) v.findViewById(R.id.txtView_time);
        viewHolder.txtView_bpm = (TextView) v.findViewById(R.id.txtView_bpm);
        viewHolder.txtView_work = (TextView) v.findViewById(R.id.txtView_work);
        viewHolder.txtView_rpm = (TextView) v.findViewById(R.id.txtView_rpm);
        viewHolder.txtView_rythm = (TextView) v.findViewById(R.id.txtView_rythm);
        viewHolder.txtView_gear = (TextView) v.findViewById(R.id.txtView_gear);
        viewHolder.btn_delete = (Button) v.findViewById(R.id.btn_delete);

        v.setTag(viewHolder);
        //set swipeLayout to proper linearLayout
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);
        //close the layout at the begining
        swipeLayout.close();

        //swipelayout Listener
        swipeLayout.addSwipeListener(new SimpleSwipeListener(){
            @Override
            public void onOpen(SwipeLayout layout) {
                //make a small animation on the trash
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        return v;
    }//generate View

    /********************************************
     *
     * fillValues, overrided method form the Swipe Library
     * @param position
     * @param convertView
     * Goal: method to fill all value in the adapter
     ************************************************/
    @Override
    public void fillValues(final int position, View convertView) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);


        //swipe layout onClickLister - if we Click on the surface
        //Surface is the upper view
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we test if the swipe is closed
                if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                    //we start a new TrainingRowDetailsActivity

                    Intent training_row = new Intent(mContext, TrainingRowModification.class);
                    //add the id of the trainingRow to the Intent
                    training_row.putExtra("row_id", trainingRowList.get(position).getId());
                    //add the training id specialy for the return method
                    training_row.putExtra("training_id",int_training_id);
                    training_row.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    mContext.startActivity(training_row);
                }//if
            }
        });

        //When we click on the delete button
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmDB realmDB = new RealmDB(mContext);
                //get the trainingRow we want to delete
                TrainingRow t_row = realmDB.getArowWithID(trainingRowList.get(position).getId());

                //delete the training Row from the list first
                trainingRowList.remove(t_row);

                //from the DB after
                realmDB.removeTrainingRow(t_row);

                swipeLayout.close();

                //we notify that the Data list has changed
                notifyDataSetChanged();
            }
        });

        //name and date of the training
        //viewHolder.txtView_name = (TextView) convertView.findViewById(R.id.txtView_Name);
        //date = (TextView) convertView.findViewById(R.id.txtView_date);

        if (trainingRowList.size() > 0) {
            //set the value to each row
            viewHolder.txtView_bpm.setText("BPM: "+ trainingRowList.get(position).getInt_bpm()+"");
            viewHolder.txtView_rpm.setText("RPM: "+ trainingRowList.get(position).getInt_rpm()+"");
            viewHolder.txtView_time.setText(trainingRowList.get(position).getStr_time()+"");
            viewHolder.txtView_work.setText(trainingRowList.get(position).getStr_work() + "");
            viewHolder.txtView_rythm.setText(trainingRowList.get(position).getStr_rythm());
            viewHolder.txtView_gear.setText(trainingRowList.get(position).getStr_gear());

        }
    }//filValues

    private class ViewHolder{
        TextView txtView_time,
                txtView_bpm,
                txtView_rpm,
                txtView_work,
                txtView_rythm,
                txtView_gear;
        Button btn_delete;
    }
}
