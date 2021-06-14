package com.hurix.kitaboo.constants.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class GlobalThreadPool {

	public static final PriorityBlockingQueue<Runnable> mainQueue = 
			new PriorityBlockingQueue<Runnable>(PriorityExecutor.WORK_QUEUE_SIZE, new PriorityExecutor.PriorityTaskComparator());
	
	public static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
			t.setPriority(Thread.MIN_PRIORITY);
			return t;
		}
	};

	private static final ThreadFactory sThreadFactoryUrgent = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
			t.setPriority(Thread.MAX_PRIORITY);
			return t;
		}
	};

	public static final Executor THREAD_POOL_EXECUTOR = new PriorityExecutor(sThreadFactory,mainQueue);

	public static final Executor THREAD_POOL_EXECUTOR_URGENT = new PriorityExecutor(sThreadFactoryUrgent,mainQueue);

}
