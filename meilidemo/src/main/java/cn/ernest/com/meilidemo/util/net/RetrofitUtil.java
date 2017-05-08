package cn.ernest.com.meilidemo.util.net;


import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.ernest.com.meilidemo.DApplication;
import cn.ernest.com.meilidemo.util.LocalCache;
import cn.ernest.com.meilidemo.util.net.cookie.PersistentCookieJar;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtil {


//    //短缓存有效期为1分钟
//    public static final int CACHE_STALE_SHORT = 60;
//    //长缓存有效期为7天
//    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    private static Retrofit mRetrofit = null;
    private static OkHttpClient mOkHttp = null;

//    private static Map<String, Retrofit> mCacheList = new HashMap<String, Retrofit>();


    public RetrofitUtil() {
        initOkHttp();
        initRetrofit();
    }

    public static <T> T createApi(Class<T> cls) {
        if (mRetrofit == null) {
            mRetrofit = initRetrofit();
        }

        return mRetrofit.create(cls);
    }

    private static Retrofit initRetrofit() {

        if (mOkHttp == null) {
            initOkHttp();
        }
//        Retrofit retrofit = mCacheList.get(url);
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
//                    .baseUrl("http://simba-api.meilishuo.com/")
                    .baseUrl("http://simba-api.meilishuo.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(mOkHttp)
//					.validateEagerly()
                    .build();
//            mCacheList.put(url, retrofit);
            return mRetrofit;
        } else {
            return mRetrofit;
        }
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (BuildConfig.DEBUG) {
////         https://drakeet.me/retrofit-2-0-okhttp-3-0-config
//			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//			builder.addInterceptor(loggingInterceptor);
//        }

        // http://www.jianshu.com/p/93153b34310e
        File cacheFile = LocalCache.getDataCachePath();
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
//        Interceptor cacheInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
////                if (!DeviceUtils.isNetworkConnected()) {
////                    request = request.newBuilder()
////                            .cacheControl(CacheControl.FORCE_CACHE)
////                            .build();
////                }
//                Response response = chain.proceed(request);
//                if (DeviceUtils.isNetworkConnected()) {
////                    int maxAge = 0;
//                    // 有网络时, 不缓存, 最大保存时长为0
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .removeHeader("Cache-Control")
//                            .header("Cache-Control", "public, max-age=" + CACHE_STALE_SHORT)
//                            .build();
//                } else {
//                    // 无网络时，设置超时为4周
////                    int maxStale = 60 * 60 * 24 * 28;
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .removeHeader("Cache-Control")
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
//                            .build();
//                }
//                return response;
//            }
//        };
//        Interceptor headerInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .addHeader(Global.CLIENT_TYPE, Global.CLIENT_TYPE_VALUE)
//                        .addHeader(Global.USER_TOKEN, DataManager.getUserToken())
//                        .build();
//                return chain.proceed(request);
//            }
//        };

//         one way to keep cookies
//        PersistentCookieJar cookieJar = PersistentCookieJar.getInstance(DApplication.getInstance(), "cookiers");
        builder.cookieJar(PersistentCookieJar.getInstance(DApplication.getInstance(), "cookie"));

        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory(null, null, null);
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        //设置统一的请求头部参数
//        builder.addInterceptor(headerInterceptor);

//        if (BuildConfig.DEBUG) {
//
//        }
        builder.addInterceptor(new UserInterceptor());

        //设置缓存 okhttp only cache get, cannnot cache post ,get 504 error
//        builder.addNetworkInterceptor(cacheInterceptor);
//        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        mOkHttp = builder.build();
    }
}
