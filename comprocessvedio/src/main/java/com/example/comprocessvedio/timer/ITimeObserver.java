package com.example.comprocessvedio.timer;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/20</p>
 * <p>@for : 计时器订阅接口</p>
 * <p>
 * 需要实现倒计时功能的页面、模块等继承这个接口，
 * 调用{@link TimeObservable#subscribe(ITimeObserver)}方法开始倒计时
 * 调用{@link TimeObservable#unSubscribe(ITimeObserver)}方法接触倒计时
 * </p>
 */
public interface ITimeObserver {

    /**
     * 倒计时逻辑实现方法
     *
     * @param curTime 当前时间
     */
    void update(long curTime);

}
