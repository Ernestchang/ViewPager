package cn.ernest.com.meilidemo.util.net;

/**
 * Created by liukun on 16/3/10.
 */
public interface DefSubListener<T> {


    void onNext(T t);

    void onError(String code, String msg);

}
