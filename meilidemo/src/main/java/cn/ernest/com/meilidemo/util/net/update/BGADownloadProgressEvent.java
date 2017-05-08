package cn.ernest.com.meilidemo.util.net.update;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/12/16 上午11:41
 * 描述:下载进度事件对象
 */
public class BGADownloadProgressEvent {
    private long mTotal;
    private long mProgress;

    public BGADownloadProgressEvent(long total, long progress) {
        mTotal = total;
        mProgress = progress;
    }

    public long getTotal() {
        return mTotal;
    }

    public long getProgress() {
        return mProgress;
    }

    public boolean isNotDownloadFinished() {
        return mTotal != mProgress;
    }

    public void setProgress(long progress) {
        mProgress = progress;
    }
}
