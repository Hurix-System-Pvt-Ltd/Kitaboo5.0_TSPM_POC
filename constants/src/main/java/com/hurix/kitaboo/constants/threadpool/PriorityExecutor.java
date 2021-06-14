package com.hurix.kitaboo.constants.threadpool;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityExecutor extends ThreadPoolExecutor {

	public static final int CPU_COUNT = Runtime.getRuntime()
			.availableProcessors();
	public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	public static final int KEEP_ALIVE = 1;
	public static final int WORK_QUEUE_SIZE = 128;

	public PriorityExecutor(final PriorityBlockingQueue<Runnable> queue) {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
				TimeUnit.SECONDS, queue);
	}

	public PriorityExecutor(final ThreadFactory threadFactory,final PriorityBlockingQueue<Runnable> queue) {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
				TimeUnit.SECONDS, queue, threadFactory);
	}

	public PriorityExecutor(final RejectedExecutionHandler handler,final PriorityBlockingQueue<Runnable> queue) {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
				TimeUnit.SECONDS, queue, handler);
	}

	public PriorityExecutor(final ThreadFactory threadFactory,
			final RejectedExecutionHandler handler,final PriorityBlockingQueue<Runnable> queue) {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
				TimeUnit.SECONDS, queue, threadFactory, handler);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
		if (callable instanceof Important)
			return new PriorityTask<T>(
					((Important) callable).getPriority(), callable);
		else
			return new PriorityTask<T>(0, callable);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable,
			final T value) {
		if (runnable instanceof Important)
			return new PriorityTask<T>(
					((Important) runnable).getPriority(), runnable, value);
		else
			return new PriorityTask<T>(0, runnable, value);
	}

	@Override
	public void execute(Runnable command) {
		// TODO Auto-generated method stub
		super.execute(command);
	}

	public interface Important {
		int getPriority();
	}

	private static final class PriorityTask<T> extends FutureTask<T> implements
	Comparable<PriorityTask<T>> {
		private final int priority;

		public PriorityTask(final int priority, final Callable<T> tCallable) {
			super(tCallable);

			this.priority = priority;
		}

		public PriorityTask(final int priority, final Runnable runnable,
				final T result) {
			super(runnable, result);

			this.priority = priority;
		}

		@Override
		public int compareTo(final PriorityTask<T> o) {
			final long diff = o.priority - priority;
			return 0 == diff ? 0 : 0 > diff ? -1 : 1;
		}
	}

	public static class PriorityTaskComparator implements Comparator<Runnable> {
		@Override
		public int compare(final Runnable left, final Runnable right) {
			if(left instanceof PriorityTask && right instanceof PriorityTask)
			{
				return ((PriorityTask) left).compareTo((PriorityTask) right);
			}
			return 0;
		}
	}
}