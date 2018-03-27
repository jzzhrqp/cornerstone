package com.cornerstone.rxbus;

import io.reactivex.Observer;

/**
 * 为RxBus使用的Subscriber, 主要提供next事件的try,catch
 * Created by liuhongtao on 2017/12/22.
 */
public abstract class RxBusSubscriber<T> implements Observer<T> {
    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        }catch (Exception e){

        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    protected abstract void onEvent(T t);

}
