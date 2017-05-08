package cn.ernest.com.meilidemo.util.net.update;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tamic on 2016-07-11.
 */
public class FileDownLoader {

    public static final String APK = "application/vnd.android.package-archive";
    public static final String PDF = "application/pdf";
    public static final String ZIP = "application/zip, application/x-compressed-zip";
    public static final String PNG = "image/png";
    public static final String JPG = "image/jpg";

    private OkHttpClient mClient;

    private static String fileSuffix = "";

    public FileDownLoader() {
        mClient = getDownOkClient();
    }

    private static FileDownLoader sInstance;


    public static synchronized FileDownLoader getInstance() {
        if (sInstance == null) {
            sInstance = new FileDownLoader();
        }
        return sInstance;
    }


    public static Subscription downloadFile(final String url, final String dest, Subscriber<FileInfo> subscriber) {
        return Observable.create(new Observable.OnSubscribe<FileInfo>() {
            @Override
            public void call(Subscriber<? super FileInfo> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        FileDownLoader.getInstance().download(url, dest, subscriber);
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()// 一定要调用onBackpressureBuffer()方法,防止下载过快,导致MissingBackpressureException异常
                .subscribe(subscriber);
    }


    public void download(@NonNull String url, @Nullable String dest,
                         Subscriber<? super FileInfo> subscriber) throws IOException {
        if (!URLUtil.isNetworkUrl(url)) {
            throw new RuntimeException("invalid url");
        }
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();// get fileName
        File root;
        if (!TextUtils.isEmpty(dest)) {
            root = new File(dest);
        } else {
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        if (!root.exists()) {
            root.mkdirs();
        }
        File output = new File(root, filename);
        if (output.exists()) {
            output.delete();
        }
        Request request = new Request.Builder().url(uri.toString()).build();
        Response response = mClient.newCall(request).execute();

        long contentLength = response.body().contentLength();

        // anthother way to name file
//        String type = response.body().contentType().toString();
//        if (type.equals(APK)) {
//            fileSuffix = ".apk";
//        } else if (type.equals(PNG)) {
//            fileSuffix = ".png";
//        } else if (type.equals(JPG)) {
//            fileSuffix = ".jpg";
//        }
//        final String name = System.currentTimeMillis() + fileSuffix;
//        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + name;

        // file fileInfo
        FileInfo currentFile = new FileInfo();
        currentFile.fileSize = contentLength;
        currentFile.fileName = output.getAbsolutePath();
        currentFile.fileUrl = url;
        currentFile.fileType = response.body().contentType().type();

        BufferedSink sink = Okio.buffer(Okio.sink(output));
        Buffer buffer = sink.buffer();

        long total = 0;
        long len;
        int bufferSize = 200 * 1024; //200kb

        BufferedSource source = response.body().source();
        while ((len = source.read(buffer, bufferSize)) != -1) {
            sink.emit();
            total += len;
            currentFile.fileSize = total;
//            subscriber.onNext(currentFile);// onNext
        }
        source.close();
        sink.close();
        subscriber.onNext(currentFile);// onNext
        subscriber.onCompleted();
    }

    private static OkHttpClient getDownOkClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory).hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static final Observable.Transformer schedulersTransformer =
            new Observable.Transformer<Observable, Observable>() {
                @Override
                public Observable<Observable> call(Observable<Observable> observable) {
                    return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                }
            };

    /**
     * 应用 Schedulers .方便 compose() 简化代码
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

}


