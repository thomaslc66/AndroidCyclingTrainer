package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.os.Looper;

import tpi.lechaireth.com.androidcyclingtrainer.BuildConfig;


/**
 * Created by Thomas on 04.05.2015.
 */

class ThreadPreconditions {
    /*******************************************
     *
     * checkOnMainThread
     * @goal permet de vérifier que la méthode est appellée uniquement depuis
     * le Thread Principal
     *
     *****************************************/
    public static void checkOnMainThread(){
        if(BuildConfig.DEBUG){
            if(Thread.currentThread() != Looper.getMainLooper().getThread()){
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }//checkOnMainThread

}