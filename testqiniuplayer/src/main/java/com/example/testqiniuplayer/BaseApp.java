package com.example.testqiniuplayer;

import android.app.Application;
import android.content.Context;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/23</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class BaseApp extends Application {

    private static Context context;

    public static Context getContext (){
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
