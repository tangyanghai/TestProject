package com.example.comprocessvedio;

import android.app.Application;
import android.content.Context;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/22</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class App extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
