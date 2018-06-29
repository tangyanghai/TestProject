package com.example.testtimer.timer;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : 计时器订阅接口</p>
 * <p>
 * 需要实现倒计时功能的页面、模块等继承这个接口，
 * 调用{@link TimerObservable#subscribe(ITimerObserver)}方法开始倒计时
 * 调用{@link TimerObservable#unSubscribe(ITimerObserver)}方法接触倒计时
 * </p>
 */
public interface ITimerObserver {

    /**
     * 倒计时逻辑实现方法
     *
     * @param curTime 当前时间
     */
    void update(long curTime);

}
