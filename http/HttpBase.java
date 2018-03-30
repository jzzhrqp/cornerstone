package com.cornerstone.http;

import android.text.TextUtils;

import com.smartcontrol.net.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/3/5.
 */

public class HttpBase {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient      = null;

    public static Retrofit getRetrofit( OkHttpClient client) {
        if (retrofit ==null){
            synchronized (""){
                if (client==null){
                    client=getOkHttpClient();
                }
                Retrofit.Builder builder =new Retrofit.Builder();
                retrofit = builder
                        .client(client)
                        .baseUrl(Constants.HOST)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }

        return retrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient==null){
            synchronized (""){
                OkHttpClient.Builder builder= new OkHttpClient.Builder();
                //设置超时
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.readTimeout(20, TimeUnit.SECONDS);
                builder.writeTimeout(20, TimeUnit.SECONDS);
                //错误重连
                builder.retryOnConnectionFailure(true);
                try {
                    // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
                    final X509TrustManager trustAllCert =
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return new java.security.cert.X509Certificate[]{};
                                }
                            };
                    final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
                    builder.sslSocketFactory(sslSocketFactory, trustAllCert);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                okHttpClient= builder.build();
            }
        }
        return okHttpClient;
    }

    /**
     * 拼接Headers头部数据
     * @param headers
     * @param name key值 ,cookie key是 “Set-Cookie”
     * @return
     */
    public static  String findInHeaders(Headers headers,String name)  {
        if (TextUtils.isEmpty(name)) name="Set-Cookie";
        String s= headers.toString();
        Logger.d(s);
        Map<String, List<String>> resHeaders = headers.toMultimap();
        StringBuffer sBuffer = new StringBuffer();

        Set<String> keySet = resHeaders.keySet();
        for (String key : keySet) {
            List<String> values=resHeaders.get(key);
            if (name.equalsIgnoreCase(key)){
                for (String value : values) {
                    String cookie = value.substring(0, value.indexOf(";") + 1);
                    sBuffer.append(cookie);
                }
            }
        }

        return  sBuffer.toString();
    }



}
