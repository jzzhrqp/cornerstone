package com.cornerstone.rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by liuhongtao on 2017/12/22.
 */

public class RxBus {

    public static RxBus mRxBus = null;

    /**
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     */
//    private AsyncSubject<Object> mRxBusObserverable;

    Observable mSubscribe;

    ObservableEmitter<Object> me;

    private final Map<Class<?>, Object> mStickyEventMap;

    public RxBus() {
//        mRxBusObserverable = AsyncSubject.create();
        mSubscribe = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                me = e;
            }
        });

        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static synchronized RxBus getInstance() {
        if (mRxBus == null) {
            mRxBus = new RxBus();
        }
        return mRxBus;
    }

    /**
     * 发送事件
     */
    public void post(final Object o) {
        if (!me.isDisposed()) {
            me.onNext(o);
//            mSubscribe = Observable.create(new ObservableOnSubscribe<Object>(){
//                @Override
//                public void subscribe(ObservableEmitter<Object> e) throws Exception {
//                    e.onNext(o);
//                }
//            });
        }
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> tObservable(Class<T> eventType) {
        return mSubscribe.ofType(eventType);
    }

    /**
     * 判断是否有订阅者
     */
//    public boolean haObservers() {
//        return mSubscribe.;
//    }

    public void reset() {
        mRxBus = null;
    }

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mSubscribe.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> e) throws Exception {
                        e.onNext(eventType.cast(event));
                    }
                }));

            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }

}


































