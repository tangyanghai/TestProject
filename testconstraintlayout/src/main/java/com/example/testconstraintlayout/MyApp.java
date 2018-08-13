package com.example.testconstraintlayout;

import android.app.Application;

import org.xutils.x;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/08/10</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
    }
}
