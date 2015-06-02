/***************************************************************
 * Programm  : Android Cycling Trainer
 * Society   : ETML
 * Author    : Thomas Léchaire
 * Date      : 26.05.2015
 * Goal      : Class used to check if the method is called from the Main Thread
 ******************************************************************** //
 * Modifications:
 * Date       : XX.XX.XXXX
 * Author     :
 * Purpose     :
 *********************************************************************/
package tpi.lechaireth.com.androidcyclingtrainer.Adapter;

import android.os.Looper;

import tpi.lechaireth.com.androidcyclingtrainer.BuildConfig;

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