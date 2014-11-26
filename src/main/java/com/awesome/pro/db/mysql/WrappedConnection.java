package com.awesome.pro.db.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.awesome.pro.pool.WrappedResource;

/**
 * Wrapper class for JDBC MySQL connection.
 * @author siddharth.s
 *
 */
public class WrappedConnection implements WrappedResource<Connection> {

	/**
	 * JDBC connection.
	 */
	private final Connection connection;

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(WrappedConnection.class);

	/**
	 * @param connection JDBC connection to be wrapped.
	 */
	public WrappedConnection(final Connection connection) {
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.IResource#close()
	 */
	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to close connection.", e);
			System.exit(1);
		}
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.IResource#isClosed()
	 */
	@Override
	public boolean isClosed() {
		if (connection == null) {
			return true;
		}

		try {
			return connection.isClosed();
		} catch (SQLException e) {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.WrappedResource#getResource()
	 */
	@Override
	public Connection getResource() {
		return connection;
	}

}
