package cn.ernest.com.meilidemo;

import android.app.Application;
import android.util.Log;

import com.orhanobut.logger.LogPrintStyle;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

/**
 * Created by Ernest on 2017/4/28.
 */

public class DApplication extends Application {


    private static DApplication instance;

    public static DApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
////
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        instance = this;

        Logger.initialize(
                new Settings()
                        .setStyle(new LogPrintStyle())
                        .isShowMethodLink(true)
                        .isShowThreadInfo(true)
                        .setMethodOffset(3)
                        .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
        );

    }
}
