package lmc5.computron.common.config;


public interface ExecutorConfig {
	public Integer getMaxActiveThreads();
	public Integer getWaitQueueSize();
}
