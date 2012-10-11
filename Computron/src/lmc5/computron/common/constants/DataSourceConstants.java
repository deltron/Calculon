package lmc5.computron.common.constants;

/**
 * Property keys for database setup. These are here for convenience and
 * consistency only.
 * 
 * In reality you can use anything you want as long as you implement
 * DataSourceConfig properly.
 * 
 * 
 */
public class DataSourceConstants {
	public static final String DB_POOLMIN = "pool.min";
	public static final String DB_POOLMAX = "pool.max";

	public static final String DB_URL = "db.url";
	public static final String DB_DRIVER = "db.driver";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_USER = "db.user";
}
