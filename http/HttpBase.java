package com.cornerstone.http;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/3/5.
 */

public class HttpBase {
    private static Retrofit retrofit;
    public static Retrofit getRetrofit(){
        if (retrofit==null){
            synchronized (HttpBase.class) {
                retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .baseUrl("http://www.baidu.com")
                        .build();
            }
        }
        return retrofit;
    }
}
