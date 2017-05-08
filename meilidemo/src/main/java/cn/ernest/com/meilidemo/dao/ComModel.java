package cn.ernest.com.meilidemo.dao;


import java.util.Map;

import cn.ernest.com.meilidemo.bean.MeiShowBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 2016/6/27 10:37
 * author: ernest
 */
public interface ComModel {


    @GET("/hera/daily/v1/list/android?limit=20&_version=905&_channel=Naltx&_res=720*1232&imei=214208402632814&mac=17%3A0E%3A76%3AC3%3AF6%3A70&st=1493346274&_did=7dc5be862ef032e54714ebc1d03a")
    Observable<MeiShowBean> getMeiShows();

    @GET("/hera/daily/v1/list/android")
    Observable<MeiShowBean> getMeiShow(@QueryMap Map<String, String> map);

    @GET("/hera/daily/v1/list/android")
    Observable<MeiShowBean> getMeiShow2(@Query("limit") String limit, @Query("_version") String _version, @Query("_channel") String _channel, @Query("_res") String _res, @Query("imei") String imei, @Query("mac") String mac, @Query("st") String st, @Query("_did") String _did);


//	@POST("api/1.0/qiniu/fetchToken.json")
//	Observable<ResultBean<FileTokenInfo>> getToken(@Field("t") String t); // t =a   isAudio

//
//	@POST("http://logback.51jinke.com/api/1.0/events/android")
//	Observable<ResultBean> postError(@Body ErrorBean bean);


//	@FormUrlEncoded
//	@POST("api/1.0/system/checkUpdate.json")
//	Observable<ResultBean<AppUpdateInfo>> updateApp(@Field("v") String visionCode);


}
