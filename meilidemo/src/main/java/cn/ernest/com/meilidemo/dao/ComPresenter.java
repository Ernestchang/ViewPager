package cn.ernest.com.meilidemo.dao;


import java.util.HashMap;
import java.util.Map;

import cn.ernest.com.meilidemo.bean.MeiShowBean;
import cn.ernest.com.meilidemo.util.net.RetrofitUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 2016/6/27 10:55
 * author: ernest
 */
public class ComPresenter {

    private static ComModel model = RetrofitUtil.createApi(ComModel.class);

    public static Observable<MeiShowBean> getMeiShow() {
//        limit=20&_version=905&_channel=Naltx&_res=720*1232&imei=214208402632814&mac=17%3A0E%3A76%3AC3%3AF6%3A70&st=1493346274&_did=7dc5be862ef032e54714ebc1d03a
        Map map = new HashMap();
        map.put("limit", "20");
        map.put("_version", "905");
        map.put("_channel", "Naltx");
        map.put("_res", "720*1232");
        map.put("imei", "214208402632814");
        map.put("mac", "17%3A0E%3A76%3AC3%3AF6%3A70");
        map.put("st", "1493346274");
        map.put("_did", "7dc5be862ef032e54714ebc1d03a");
        return model.getMeiShow(map).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
//        return model.getMeiShow2("20", "905", "Naltx", "720*1232", "214208402632814", "17%3A0E%3A76%3AC3%3AF6%3A70", "1493346274", "7dc5be862ef032e54714ebc1d03a").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }


}
