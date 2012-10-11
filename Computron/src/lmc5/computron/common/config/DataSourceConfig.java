package lmc5.computron.common.config;


public interface DataSourceConfig {
	public Integer getPoolMin();

	public Integer getPoolMax();

	public String getDriver();

	public String getURL();

	public String getUser();

	public String getPassword();
}
