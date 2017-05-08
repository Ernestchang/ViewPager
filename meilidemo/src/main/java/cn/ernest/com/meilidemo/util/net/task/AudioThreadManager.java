package cn.ernest.com.meilidemo.util.net.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 临时线程专用线程池，最多10个线程并发
 * @author linghu
 *
 */
public class AudioThreadManager {
	private static final int MAX_RUNNING_THREAD = 3;

	private ExecutorService executor;

	private static AudioThreadManager instance;
	public synchronized static AudioThreadManager getInstance(){
		if(instance == null){
			instance = new AudioThreadManager();
		}
		return instance;
	}

	private AudioThreadManager(){
		try {
			executor = Executors.newFixedThreadPool(MAX_RUNNING_THREAD,	new NormalThreadFactory());
		} catch (Throwable t) {
			executor = Executors.newCachedThreadPool();
		}
	}

	public void start(Runnable runnable){
		executor.submit(runnable);
	}
	
	public <T> Future<T> start(Callable<T> callable){
		return executor.submit(callable);
	}

	/**
	 * 一般优先级的线程工厂
	 */
	private static class NormalThreadFactory implements ThreadFactory {
	    private static final AtomicInteger poolNumber = new AtomicInteger(1);
	    private final ThreadGroup group;
	    private final AtomicInteger threadNumber = new AtomicInteger(1);
	    private final String namePrefix;

		NormalThreadFactory() {
	        SecurityManager s = System.getSecurityManager();
	        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
	        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-audio-thread-";
	    }

	    public Thread newThread(Runnable r) {
	        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
	        if (t.isDaemon()) {
				t.setDaemon(false);
			}
	        if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
	        return t;
	    }
	}


}