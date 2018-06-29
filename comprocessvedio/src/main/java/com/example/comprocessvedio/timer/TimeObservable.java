package com.example.comprocessvedio.timer;

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
public class TimeObservable {

    private static TimeObservable instance = null;

    private TimeHandler mHandler;

    /**
     * 观察者集合
     */
    private volatile List<ITimeObserver> mListObserver;

    private TimeObservable() {
        mListObserver = new CopyOnWriteArrayList<>();
    }

    public static TimeObservable getInstance() {
        if (instance == null) {
            synchronized (TimeObservable.class) {
                if (instance == null) {
                    instance = new TimeObservable();
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
    public synchronized void subscribe(ITimeObserver observer) {
        if (observer == null) {
            return;
        }
        //当前是否正在运行-->集合为空就没有运行
        boolean isRunning = !mListObserver.isEmpty();
        //添加进入集合
        mListObserver.add(observer);
        //如果没有运行就要开始运行
        if (!isRunning) {
            start();
        }else {
            //防止第一次设置的时候,时间有一瞬间不动,所有每次设置的时候都要调用一下更新方法
            observer.update(System.currentTimeMillis());
        }
    }

    /**
     * 解除订阅
     * @param observer
     */
    public synchronized void unSubscribe(ITimeObserver observer) {
        if (observer == null) {
            return;
        }
        mListObserver.remove(observer);
        //检查集合是否为空了,为空就停止运行
        if (mListObserver.isEmpty()) {
            stop();
        }
    }


    /**
     * 开始计时
     */
    private void start() {
        mHandler = new TimeHandler(this);
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 停止计时,释放资源
     */
    private void stop() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    /**
     * 通知观察者,时间已经改变
     *
     * @param curTime 当前时间,单位ms
     */
    private void notifyUpdate(long curTime) {
        if (mListObserver == null||mListObserver.size() == 0) {
            stop();
            return;
        }

        for (ITimeObserver observer : mListObserver) {
            if (observer!=null) {
                observer.update(curTime);
            }
        }
    }


    /**
     * 计时handler
     * 真正的计时功能是通过这个handler来实现
     */
    private static class TimeHandler extends Handler {

        private WeakReference<TimeObservable> mOvservable;

        public TimeHandler(TimeObservable ovservable) {
            this.mOvservable = new WeakReference<>(ovservable);
        }

        @Override
        public void handleMessage(Message msg) {
            TimeObservable observable = mOvservable.get();
            if (observable == null) {
                return;
            }
            //1秒之后再次发送消息
            sendEmptyMessageDelayed(0,1_000);
            //被观察者通知观察者
            observable.notifyUpdate(System.currentTimeMillis());
        }
    }

}
