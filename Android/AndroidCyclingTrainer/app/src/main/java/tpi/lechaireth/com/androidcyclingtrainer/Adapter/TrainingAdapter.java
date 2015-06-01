package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    /* List of all Training object */
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
        //new ViewHolder.
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtView_date = (TextView) v.findViewById(R.id.txtView_date);
        viewHolder.img_bike = (ImageView) v.findViewById(R.id.imageView);
        viewHolder.txtView_name = (TextView) v.findViewById(R.id.txtView_Name);
        viewHolder.txtView_recupIndice = (TextView) v.findViewById(R.id.txtView_recupIndice);
        viewHolder.btn_delete = (Button) v.findViewById(R.id.delete);

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

                    int id = trainingList.get(position).getInt_id();
                    //we start a new TrainingRowActivity
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
                Training t = realmDB.getATrainingWithID(trainingList.get(position).getInt_id());

                //delete the training from the list first
                trainingList.remove(t);

                //from the DB after
                realmDB.removeTraining(t);

                //hide the swipe Layout and display infos and training Name
                swipeLayout.close();

                //we notify that the Data list has changed
                notifyDataSetChanged();
            }
        });

        //name and date of the training
        //viewHolder.txtView_name = (TextView) convertView.findViewById(R.id.txtView_Name);
        //date = (TextView) convertView.findViewById(R.id.txtView_date);

        if (trainingList.size() > 0) {
            //fill data of the date and the training name.
            viewHolder.txtView_date.setText(trainingList.get(position).getStr_day());
            viewHolder.txtView_name.setText(trainingList.get(position).getStr_name());
            //set textView_recupIndice depending of the rest_indice
            if(trainingList.get(position).getInt_recup() > 0){
                viewHolder.txtView_recupIndice.setText("Indice de récupération: " + trainingList.get(position).getInt_recup());
            }else{
                viewHolder.txtView_recupIndice.setText("-");
            }


            //check if it is a VTT or ROAD training and set the img.
            if(trainingList.get(position).isBln_isVtt() == true){
                //we set the color of the bike. Blue for VTT
                viewHolder.img_bike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vtt_icon));
            }else{
                //Road Bike. Red
                viewHolder.img_bike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bike_icon));
            }

        }
    }//filValues

    /**
     * View Holder Class to manage the view and the UI Elements
     */
    private class ViewHolder{
        public TextView txtView_date, txtView_name, txtView_recupIndice;
        private Button btn_delete;
        private ImageView img_bike;

    }//private class ViewHolder

}//class TrainingAdapter
