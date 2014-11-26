package com.awesome.pro.db.mysql.references;

import com.awesome.pro.utilities.PropertyFileUtility;

/**
 * JDBC connection confiigurations object.
 * @author siddharth.s
 */
public class SQLConfigurations {

	/**
	 * JDBC driver class name.
	 */
	public final String driverClass;

	/**
	 * JDBC URL to be used to connect to host.
	 */
	public final String jdbcUrl;

	/**
	 * Database user name.
	 */
	public final String username;

	/**
	 * Database password.
	 */
	public final String password;

	/**
	 * @param configFile Path and name of configuration file to be read.
	 */
	public SQLConfigurations(final String configFile) {
		final PropertyFileUtility config = new PropertyFileUtility(configFile);
		driverClass = config.getStringValue(DatabaseQueryConfigReferences.
				PARAMETER_JDBC_DRIVER, DatabaseQueryConfigReferences.
				DEFAULT_JDBC_DRIVER);
		jdbcUrl = config.getStringValue(DatabaseQueryConfigReferences.
				PARAMETER_JDBC_URL);
		username = config.getStringValue(DatabaseQueryConfigReferences.
				PARAMETER_MYSQL_USER);
		password = config.getStringValue(DatabaseQueryConfigReferences.
				PARAMETER_MYSQL_PASSWORD);
	}
}
