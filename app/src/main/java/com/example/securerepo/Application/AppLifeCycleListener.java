package com.example.securerepo.application;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AppLifeCycleListener implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground(){
        Log.d("DEBUG","background");

    }

}
