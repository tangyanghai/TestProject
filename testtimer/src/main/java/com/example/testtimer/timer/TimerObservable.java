package com.example.testtimer.timer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : 计时器被观察者</p>
 * <p>
 * 单例模式
 * 一旦有观察者订阅,就开始发出消息,每秒发送一次
 * 一旦所有的观察者都解除订阅,就停止计时
 * </p>
 */
public class TimerObservable {

    private static TimerObservable instance = null;

    private TimerHandler mHandler;

    /**
     * 观察者集合
     */
    private volatile List<ITimerObserver> mListObserver;

    private TimerObservable() {
        mListObserver = new CopyOnWriteArrayList<>();
    }

    public static TimerObservable getInstance() {
        if (instance == null) {
            synchronized (TimerObservable.class) {
                if (instance == null) {
                    instance = new TimerObservable();
                }
            }
        }
        return instance;
    }

    /**
     * 订阅
     *
     * @param observer 观察者
     */
    public void subscribe(ITimerObserver observer) {
        if (observer == null) {
            return;
        }
        Log.i(TAG, "subscribe: ");
        //当前是否正在运行-->集合为空就没有运行
        boolean isRunning = !mListObserver.isEmpty();
        //添加进入集合
        mListObserver.add(observer);
        //如果没有运行就要开始运行
        if (!isRunning) {
            start();
        }
    }

    /**
     * 解除订阅
     * @param observer
     */
    public void unSubscribe(ITimerObserver observer) {
        if (observer == null) {
            return;
        }
        Log.i(TAG, "unSubscribe: ");
        mListObserver.remove(observer);
        //检查集合是否为空了,为空就停止运行
        if (mListObserver.isEmpty()) {
            stop();
        }
    }

    private static final String TAG = ">>TimerObservable<<";
    
    /**
     * 开始计时
     */
    private void start() {
        Log.i(TAG, "start: 开始计时");
        mHandler = new TimerHandler(this);
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 停止计时,释放资源
     */
    private void stop() {
        Log.i(TAG, "stop: 停止计时");
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    /**
     * 通知观察者,时间已经改变
     *
     * @param curTime 当前时间,单位ms
     */
    private void update(long curTime) {
        if (mListObserver == null||mListObserver.size() == 0) {
            stop();
            return;
        }

        for (ITimerObserver observer : mListObserver) {
            if (observer!=null) {
                Log.i(TAG, "update: "+observer.toString());
                observer.update(curTime);
            }
        }
    }


    /**
     * 计时handler
     * 真正的计时功能是通过这个handler来实现
     */
    private static class TimerHandler extends Handler {

        private WeakReference<TimerObservable> mOvservable;

        public TimerHandler(TimerObservable ovservable) {
            this.mOvservable = new WeakReference<>(ovservable);
        }

        @Override
        public void handleMessage(Message msg) {
            TimerObservable observable = mOvservable.get();
            if (observable == null) {
                return;
            }
            //1秒之后再次发送消息
            sendEmptyMessageDelayed(0,1_000);
            //被观察者通知观察者
            observable.update(System.currentTimeMillis());
        }
    }

}
