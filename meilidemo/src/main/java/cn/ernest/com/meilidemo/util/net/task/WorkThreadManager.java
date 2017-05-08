package cn.ernest.com.meilidemo.util.net.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 临时线程专用线程池，最多10个线程并发
 * @author linghu
 *
 */
public class WorkThreadManager {
	private static final int MAX_RUNNING_THREAD = 5;

	private ScheduledExecutorService executor;

	private static WorkThreadManager instance;
	public synchronized static WorkThreadManager getInstance(){
		if(instance == null){
			instance = new WorkThreadManager();
		}
		return instance;
	}

	private WorkThreadManager(){
		try {
			executor = Executors.newScheduledThreadPool(MAX_RUNNING_THREAD, new NormalThreadFactory());
		} catch (Throwable t) {
			executor = Executors.newScheduledThreadPool(MAX_RUNNING_THREAD, new NormalThreadFactory());
		}
	}

	public void start(Runnable runnable){
		executor.schedule(runnable, 0, TimeUnit.MILLISECONDS);
	}
	
	public <T> Future<T> start(Callable<T> callable){
		return executor.schedule(callable, 0, TimeUnit.MILLISECONDS);
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
	        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-task-thread-";
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