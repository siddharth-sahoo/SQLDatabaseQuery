package com.awesome.pro.db.mysql;

import java.sql.PreparedStatement;

import javax.sql.rowset.CachedRowSet;

/**
 * Interface for database query utility.
 * @author siddharth.s
 */
public interface IDatabaseQuery extends AutoCloseable {

	/**
	 * Runs a SELECT query and stores the returned row set
	 * as a class variable.
	 * @param query String query to run.
	 */
	CachedRowSet runQuery(String query);

	/**
	 * Runs an UPDATE query on the database.
	 * @param query String query to run.
	 */
	void runUpdate(String query);

	/**
	 * Runs multiple UPDATE queries on the database.
	 * @param query String queries to run.
	 */
	void runUpdate(String query[]);

	/**
	 * @param query SQL query to be used to prepare a statement.
	 * @return Prepared statement from the query.
	 */
	PreparedStatement getPreparedStatement(String query);

	/**
	 * @param preparedStatement Query to execute.
	 * @return Cached row set from query execution.
	 */
	CachedRowSet runQuery(PreparedStatement preparedStatement);

	/**
	 * @param preparedStatement Prepared statement to be executed.
	 */
	void runUpdate(PreparedStatement preparedStatement);

}
