package com.awesome.pro.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.awesome.pro.db.mysql.references.SQLConfigurations;
import com.awesome.pro.pool.AcquireResource;

/**
 * @author siddharth.s
 *
 */
public class AcquireJDBCConnection implements AcquireResource<WrappedConnection> {

	/**
	 * Database connection configurations object.
	 */
	private final SQLConfigurations config;

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			AcquireJDBCConnection.class);

	/**
	 * @param configFile Path and name of configuration file.
	 */
	public AcquireJDBCConnection(final String configFile) {
		config = new SQLConfigurations(configFile);
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.AcquireResource#acquireResource()
	 */
	@Override
	public WrappedConnection acquireResource() {
		try {
			Class.forName(config.driverClass);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Unable to load driver.", e);
			System.exit(1);
		}
		try {
			final Connection connection = DriverManager.getConnection(
					config.jdbcUrl, config.username, config.password);
			return new WrappedConnection(connection);
		} catch (SQLException e) {
			LOGGER.error("Unable to create a connection.", e);
			System.exit(1);
		}
		return null;
	}

}
