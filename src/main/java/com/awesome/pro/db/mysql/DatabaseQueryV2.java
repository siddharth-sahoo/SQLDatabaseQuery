package com.awesome.pro.db.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.awesome.pro.pool.IObjectPool;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Database query utility using connection pooling.
 * @author siddharth.s
 */
public class DatabaseQueryV2 implements IDatabaseQuery {

	/**
	 * JDBC Connection.
	 */
	private final WrappedConnection connection;

	/**
	 * Connection pool instance to which the connection id to be returned to.
	 */
	private final IObjectPool<WrappedConnection> pool;
	
	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			DatabaseQueryV2.class);

	/**
	 * Database query utility constructor.
	 */
	public DatabaseQueryV2(final IObjectPool<WrappedConnection>
		connectionPool) {
		pool = connectionPool;
		connection = connectionPool.checkOutResource();
	}

	/**
	 * Closes all the active SSH and MySQL connections.
	 */
	@Override
	public void close() {
		pool.checkInResource(connection);
	}

	/**
	 * Runs a SELECT query and stores the row set as a class variable which can be retrieved using getRowSet().
	 * @param query SQL SELECT query to execute.
	 */
	@Override
	public CachedRowSet runQuery(final String query) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Running query: " + query);
		}

		try {
			Statement statement = connection.getResource().createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			CachedRowSet crs = new CachedRowSetImpl();
			crs.populate(resultSet);
			resultSet.close();
			statement.close();
			return crs;
		} catch (SQLException e) {
			LOGGER.error("Unable to execute query.", e);
			System.exit(1);
		}
		return null;
	}

	/**
	 * Executes SQL DML queries.
	 * @param query SQL UPDATE/INSERT query to execute.
	 */
	@Override
	public void runUpdate(String query) {
		try {
			Statement statement = connection.getResource().createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to create statement.", e);
			System.exit(1);
		}
	}

	/**
	 * Executes SQL DML queries.
	 * @param query SQL UPDATE/INSERT query to execute.
	 */
	@Override
	public void runUpdate(String query[]) {
		try {
			Statement statement = connection.getResource().createStatement();
			for(String s : query) {
				statement.executeUpdate(s);
			}
			statement.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to create statement.", e);
			System.exit(1);
		}
	}

	/**
	 * @param query SQL query to be used to prepare a statement.
	 * @return Prepared statement from the query.
	 */
	@Override
	public PreparedStatement getPreparedStatement(final String query) {
		try {
			return connection.getResource().prepareStatement(query);
		} catch (SQLException e) {
			LOGGER.error("Unable to create a prepared statement.", e);
			System.exit(1);
		}
		return null;
	}

	/**
	 * @param preparedStatement Query to execute.
	 * @return Cached row set from query execution.
	 */
	@Override
	public CachedRowSet runQuery(final PreparedStatement preparedStatement) {
		try {
			CachedRowSet crs = new CachedRowSetImpl();
			ResultSet rs = preparedStatement.executeQuery();
			crs.populate(rs);
			rs.close();
			preparedStatement.close();
			return crs;
		} catch (SQLException e) {
			LOGGER.error("Unable to execute query.", e);
			System.exit(1);
		}
		return null;
	}

	/**
	 * @param preparedStatement Prepared statement to be executed.
	 */
	@Override
	public void runUpdate(final PreparedStatement preparedStatement) {
		try {
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			LOGGER.error("Unable to execute query.", e);
			System.exit(1);
		}
	}

}
