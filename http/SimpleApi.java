package com.cornerstone.http;



import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Pancool_wtl on 2017/11/29.
 */

public interface SimpleApi {
    //获取数据
//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST
//    Observable<ResponseBody> post(@Url String url, @Body RequestBody requestBody);

    @FormUrlEncoded
    @POST
    Observable<Result<SimpleNetResultBase>> postRegister(@Url String url, @FieldMap Map<String, String> fieldMap);

    @FormUrlEncoded
    @POST
    Observable<Result<SimpleNetResultBase>> postLogin(@Url String url, @FieldMap Map<String, String> fieldMap);

    @GET
    Call<String> getWeatherData(@Url String url, @Header("Cookie") String Cookie, @QueryMap Map<String, Object> queryMap);

    @GET
    Observable<Result<Map<String,Object>>> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> queryMap);

    @GET
    Observable<Result<Map<String,Object>>> post(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> queryMap);


    //上传文件
    @Multipart
    @POST
    Observable<ResponseBody> upLoadFiles(@Url String url, @PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> parts);
}
