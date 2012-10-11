package lmc5.computron.common.util;

import javax.sql.DataSource;

import lmc5.computron.common.config.DataSourceConfig;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class DataSourceUtil {
	private static Logger LOGGER = Logger.getLogger(DataSourceUtil.class);
	private static GenericObjectPool pool = null;

	public static DataSource setupDataSource(DataSourceConfig config) {
		try {
			DbUtils.loadDriver(config.getDriver());

			String url = config.getURL();
			String user = config.getUser();
			String password = config.getPassword();

			pool = new GenericObjectPool();
			pool.setMinIdle(config.getPoolMin());
			pool.setMaxActive(config.getPoolMax());

			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, user, password);
			new PoolableConnectionFactory(connectionFactory, pool, null, null, true, true);

			DataSource ds = new PoolingDataSource(pool);

			return ds;

		} catch (Exception e) {
			LOGGER.error(e);
		}

		return null;
	}
}
