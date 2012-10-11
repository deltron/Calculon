package lmc5.computron.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lmc5.computron.common.config.ExecutorConfig;

public class ExecutorUtil {
	public static ExecutorService setupExecutor(ExecutorConfig config) {

		ThreadPoolExecutor executor = new ThreadPoolExecutor(config.getMaxActiveThreads(), config.getMaxActiveThreads(), 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(config.getWaitQueueSize()), new ThreadPoolExecutor.CallerRunsPolicy());

		return executor;
	}
}
