package com.example.chenyong.demoanimation;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by focus on 16/11/16.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
