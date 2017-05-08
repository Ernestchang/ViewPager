package cn.ernest.com.meilidemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class ImageUtil {

    public static final long SIZE_200_KB = 209715200l;
    public static final long SIZE_300_KB = 314572800l;
    public static final long SIZE_1_5_MB = 1572864000l;


//    public static void loadRound(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        Glide.with(context).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
//    }
//
//    public static void load(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        Glide.with(context).load(url).crossFade().into(iv);
//
////        Glide.with(context)
////                .load(url)//目标URL
////                .placeholder(defaultImageResId) //占位图片
////                .error(defaultImageResId) //图片获取失败时默认显示的图片
////                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存全尺寸图片，也缓存其他尺寸图片
////                .centerCrop()
////                .crossFade()
////                .into(this);
//
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static void load(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        if (!activity.isDestroyed()) {
//            Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
//        }
//    }

    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */

    //默认加载
    public static void load(RequestManager manager, String path, ImageView mImageView) {
//        manager.load(path).centerCrop().crossFade().into(mImageView);
        manager.load(path).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    //默认加载 drawable中图片
    public static void load(RequestManager manager, int path, ImageView mImageView) {
//        manager.load(path).centerCrop().crossFade().into(mImageView);
        manager.load(path).asBitmap().into(mImageView);
    }

    //加载指定大小
    public static void loadSize(RequestManager manager, String path, int width, int height, ImageView mImageView) {
        manager.load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadHolder(RequestManager manager, String path, ImageView mImageView, int lodingImage) {
//        manager.load(path).centerCrop().crossFade().placeholder(lodingImage).error(lodingImage).into(mImageView);
        manager.load(path).asBitmap().centerCrop().placeholder(lodingImage).error(lodingImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    public static void loadHolder2(RequestManager manager, String path, ImageView mImageView, int lodingImage) {
        manager.load(path).centerCrop().crossFade().placeholder(lodingImage).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadHolderSize(RequestManager manager, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        manager.load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(RequestManager manager, String path, ImageView mImageView) {
        manager.load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(RequestManager manager, String path, ImageView mImageView) {
        manager.load(path).priority(Priority.NORMAL).into(mImageView);
    }

    //默认加载
    public static void load(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().crossFade().into(mImageView);
    }

    //加载指定大小
    public static void loadSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadHolder(Context mContext, String path, ImageView mImageView, int lodingImage) {
        Glide.with(mContext).load(path).centerCrop().crossFade().placeholder(lodingImage).error(lodingImage).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadHolderSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    //设置静态GIF加载方式
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    // should called in io thread
    public static String getImagePathFromache(RequestManager manager, String url) {
//        FutureTarget<File> futureTarget = manager.load(url).downloadOnly(200, 200);
        FutureTarget<File> futureTarget = manager.load(url).downloadOnly(150, 150);
        try {
            File cacheFile = futureTarget.get();
            return cacheFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
//            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            BitmapFactory.decodeFile(path, options);
//            if (tmp != null) {
//                tmp.recycle();
//                tmp = null;
//            }

            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }

            Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
//            if (scale != null) {
//                bm.recycle();
//                bm = null;
//            }

            if (crop) {
                Bitmap cropped = Bitmap.createBitmap(scale, (scale.getWidth() - width) >> 1, (scale.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return scale;
                }

//                bm.recycle();
//                bm = cropped;
                return cropped;
            }
            return scale;

        } catch (final OutOfMemoryError e) {
            options = null;
        }

        return null;
    }


//    /**
//     * 压缩单张图片 Listener 方式
//     */
//    public static void compressWithLs(File file, OnCompressListener listener) {
//        Luban.get(JApplication.getInstance())
//                .load(file)
//                .putGear(Luban.THIRD_GEAR)
////                .setFilename(System.currentTimeMillis() + "")
//                .setCompressListener(listener).launch();
//    }
//
//    /**
//     * 压缩单张图片 RxJava 方式
//     */
//    public static void compressWithRx(File file, Action1<File> action1) {
//        Luban.get(JApplication.getInstance())
//                .load(file)
//                .putGear(Luban.THIRD_GEAR)
//                .asObservable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                })
//                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
//                    @Override
//                    public Observable<? extends File> call(Throwable throwable) {
//                        return Observable.empty();
//                    }
//                })
//                .subscribe(action1);
//    }


    /**
     * 按质量压缩
     *
     * @return 质量压缩压缩过的图片
     */
    public static byte[] compressByQuality(String filePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        String qualityAttr = getImageQualityAttr(filePath);

        int quality = TextUtils.isEmpty(qualityAttr) ? 80 : Integer.valueOf(qualityAttr);

        if (bitmap != null) {

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            int size;
            while ((size = baos.size()) > SIZE_200_KB) {
                baos.reset();
                if (size > SIZE_1_5_MB) {
                    quality -= 20;

                } else if (size > SIZE_300_KB) {
                    quality -= 10;
                } else {
                    quality -= 5;
                }

//                if (quality < 0) return null;

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }

            byte[] bytes = baos.toByteArray();
        }
        return baos.toByteArray();
    }


    public static String getImageQualityAttr(String filePath) {
        String qualityAttr = null;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            qualityAttr = exifInterface.getAttribute("quality");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return qualityAttr;
    }


    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        if (maxWidth == 0 || maxHeight == 0) return 1;
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }


//    /**
//     * 计算inSampleSize，用于压缩图片
//     *
//     * @param options
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    private int calculateInSampleSize(BitmapFactory.Options options,
//                                      int reqWidth, int reqHeight) {
//        // 源图片的宽度
//        int width = options.outWidth;
//        int height = options.outHeight;
//        int inSampleSize = 1;
//
//        if (width > reqWidth && height > reqHeight) {
//            // 计算出实际宽度和目标宽度的比率
//            int widthRatio = Math.round((float) width / (float) reqWidth);
//            int heightRatio = Math.round((float) height / (float) reqHeight);
//            inSampleSize = Math.max(widthRatio, heightRatio);
//        }
//        return inSampleSize;
//    }

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromResource(String pathName,
                                                   int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }
}
