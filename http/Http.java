package com.cornerstone.http;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aoshuoCloud.AoshuoApi;
import com.smart.chat.http.SmartChatApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iron on 17-12-27.
 */

public class Http {

    private static AoshuoApi aoshuoApi=null;
    private static SmartChatApi smartChatApi=null;
    private static Application application=null;


    public static AoshuoApi getAoshuoApi(){
        if (aoshuoApi==null){
            aoshuoApi= HttpBase.getRetrofit().create(AoshuoApi.class);
        }
        return aoshuoApi;
    }

    public static SmartChatApi getSmartChatApi(){
        if (smartChatApi==null){
            smartChatApi= HttpBase.getRetrofit().create(SmartChatApi.class);
        }
        return smartChatApi;
    }

    public static <T>  void invoke(Observable<T> observable, Observer<T> callback) {
        /**
         * 先判断网络连接状态和网络是否可用，放在回调那里好呢，还是放这里每次请求都去判断下网络是否可用好呢？
         * 如果放在请求前面太耗时了，如果放回掉提示的速度慢，要10秒钟请求超时后才提示。
         * 最后采取的方法是判断网络是否连接放在外面，网络是否可用放在回掉。
         */
        if (application!=null&&!isNetConnected(application)) {
//            ToastUtil.showMsg("网络连接已断开");
            callback.onError(new Throwable("网络连接已断开"));
            return;
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        Http.application = application;
    }

    //判断网络是否已经连接
    public static boolean isNetConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }
}
