package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import tpi.lechaireth.com.androidcyclingtrainer.DB.RealmDB;
import tpi.lechaireth.com.androidcyclingtrainer.DB.Training;
import tpi.lechaireth.com.androidcyclingtrainer.R;
import tpi.lechaireth.com.androidcyclingtrainer.TrainingRowActivity;


/**
 * Created by Thomas on 04.05.2015.
 */
public class TrainingAdapter extends BaseSwipeAdapter {

    /* Attributs of the object */
    private List<Training> trainingList;
    //inflater for the layout
    private LayoutInflater mInflater;
    //Context of the app
    private Context mContext;

    /* Constructor  */
    public TrainingAdapter(Context context, List<Training> mTraining) {
        this.trainingList = mTraining;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;

    }//TrainingAdapter

    /***************************************************************************
     *
     * updateLevel()
     * @param mTraining
     * Goal: Method that update the listView with the new data
     *
     **************************************************************************/
    public void updateLevel(List<Training> mTraining){
        ThreadPreconditions.checkOnMainThread();
        this.trainingList = mTraining;
        notifyDataSetChanged();
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


    public int getItemIdWithName(String name) {
        RealmDB realmDB = new RealmDB(mContext);
        int id = realmDB.getTraining(name).getInt_id();
        return id;
    }

    /**************************************************************************
     *
     * getItem() - Overrided method from the AdapterClass
     * @param position
     * @return id
     * Goal: method that returns a Training
     *
     *************************************************************************/
    @Override
    public Training getItem(int position) {
        return null;
    }

    /*************************************************************************
     *
     * getCount() - overrided method form the AdapterClass
     * @return size
     * Goal: method tha count the size of the training list
     *
     ************************************************************************/
    @Override
    public int getCount() {
        int size = 0;
        if (trainingList != null){
            size = trainingList.size();
        }
        return size;
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
        View v = mInflater.inflate(R.layout.training_list_item,null);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtView_date = (TextView) v.findViewById(R.id.txtView_date);

        viewHolder.txtView_name = (TextView) v.findViewById(R.id.txtView_Name);

        viewHolder.btn_delete = (Button) v.findViewById(R.id.delete);

        v.setTag(viewHolder);

        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.close();

        //swipelayout Listener
        swipeLayout.addSwipeListener(new SimpleSwipeListener(){
            SwipeLayout.Status status;
            @Override
            public void onOpen(SwipeLayout layout) {
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
                    //we start a new TrainingRowActivity
                    int id = trainingList.get(position).getInt_id();

                    Intent rowInTraining = new Intent(mContext, TrainingRowActivity.class);
                    rowInTraining.putExtra("_id", id);
                    rowInTraining.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    mContext.startActivity(rowInTraining);
                }//if
            }
        });

        //When we click on the delete button
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmDB realmDB = new RealmDB(mContext);
                //get the training we want to delete
                Training t = realmDB.getTraining(trainingList.get(position).getStr_name());

                Toast.makeText(mContext, "Id: " + t.getInt_id() + " NAME: " + t.getStr_name() + " POS: " + position, Toast.LENGTH_LONG).show();

                //delete the training
                trainingList.remove(t);
                realmDB.removeTraining(t);
                swipeLayout.close();

                notifyDataSetChanged();
            }
        });

        //name and date of the training
        //viewHolder.txtView_name = (TextView) convertView.findViewById(R.id.txtView_Name);
        //date = (TextView) convertView.findViewById(R.id.txtView_date);

        if (trainingList.size() > 0) {
            //fill data
            viewHolder.txtView_date.setText(trainingList.get(position).getDte_day());
            viewHolder.txtView_name.setText(trainingList.get(position).getStr_name());
        }
    }//filValues

    /**
     * View Holder Class to manage the view and the UI Elements
     */
    private class ViewHolder{
        public TextView txtView_date, txtView_name;
        private Button btn_delete;

    }//private class ViewHolder

}//class TrainingAdapter
