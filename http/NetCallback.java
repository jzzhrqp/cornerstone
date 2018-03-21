package cornerstone.http;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Headers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by Administrator on 2018/3/2.
 */

public abstract class NetCallback<T> implements Observer<Result<T>> {
    Disposable mDisposable = null;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable=d;
    }

    @Override
    public void onNext(Result<T> tResult) {
        if (tResult.isError()){
            onError(tResult.error());
        }else{
            boolean isError=true;

            Response<T> response = tResult.response();
            if (response!=null){
               if(response.isSuccessful()){
                    T body=response.body();
                    if (body!=null){
                        isError=false;
                        onResponse(body,response.headers());
                    }
                }
            }

            if (isError){
                onError(new Throwable("网络请求错误！"));
            }
        }
    }

    @Override
    public abstract void onError(Throwable e);

    @Override
    public void onComplete() {

    }

    public abstract void onResponse( T data,Headers headers);

    public void  unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
