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
 */
public class FileThreadManager {
	private static final int MAX_RUNNING_THREAD = 5;
			
	private static FileThreadManager _instance;
	
	private ExecutorService executor;
	
	private FileThreadManager(){
		try {
			executor = Executors.newFixedThreadPool(MAX_RUNNING_THREAD,	new LowThreadFactory());
		} catch (Throwable t) {
			executor = Executors.newCachedThreadPool();
		}
	}

	public synchronized static FileThreadManager getInstance(){
		if(_instance == null){
			_instance = new FileThreadManager();
		}
		return _instance;
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
	private static class LowThreadFactory implements ThreadFactory {
	    private static final AtomicInteger poolNumber = new AtomicInteger(1);
	    private final ThreadGroup group;
	    private final AtomicInteger threadNumber = new AtomicInteger(1);
	    private final String namePrefix;

	    LowThreadFactory() {
	        SecurityManager s = System.getSecurityManager();
	        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
	        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-advertise-thread-";
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
	};


}