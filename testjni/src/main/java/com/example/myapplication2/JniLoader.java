package com.example.myapplication2;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/07/17</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
public class JniLoader {
    static {
        System.loadLibrary("firstndk");
    }
    public native String getHelloString();
}
