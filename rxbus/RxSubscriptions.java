package com.cornerstone.rxbus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 管理 CompositeSubscription
 * Created by liuhongtao on 2017/12/22.
 */

public class RxSubscriptions {

    private static CompositeDisposable mSubscriptions = null;

    public static void createCompositeDisposable() {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeDisposable();
        }
    }

    public static boolean isUnsubscribed() {
        return mSubscriptions.isDisposed();
    }

    public static void add(Disposable subscribe) {
        if (subscribe != null) {
            mSubscriptions.add(subscribe);
        }
    }

    public static void remove(Disposable subscribe) {
        if (subscribe != null) {
            mSubscriptions.remove(subscribe);
        }
    }

    public static void clear() {
        mSubscriptions.clear();
    }

    public static void onDispose() {
        mSubscriptions.dispose();
    }

    public static boolean hasSubscriptions() {
        return mSubscriptions.isDisposed();
    }

}




















