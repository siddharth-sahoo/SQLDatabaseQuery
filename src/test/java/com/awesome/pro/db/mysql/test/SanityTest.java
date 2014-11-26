package com.awesome.pro.db.mysql.test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.awesome.pro.db.mysql.AcquireJDBCConnection;
import com.awesome.pro.db.mysql.DatabaseQueryV2;
import com.awesome.pro.db.mysql.IDatabaseQuery;
import com.awesome.pro.db.mysql.WrappedConnection;
import com.awesome.pro.pool.IObjectPool;
import com.awesome.pro.pool.ObjectPool;

/**
 * Sanity test for MySQL connection pooling.
 * @author siddharth.s
 */
public class SanityTest {

	/**
	 * JDBC connection pool.
	 */
	private IObjectPool<WrappedConnection> jdbcPool;

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(SanityTest.class);

	/**
	 * Database query utility.
	 */
	private IDatabaseQuery databaseQuery;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		jdbcPool = new ObjectPool<>(
				"sql.properties",
				new AcquireJDBCConnection("sql.properties"));
		databaseQuery = new DatabaseQueryV2(jdbcPool);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		databaseQuery.close();
		jdbcPool.closePool(true);
	}

	@Test
	public void testSqlQuery() throws SQLException {
		final long start = System.currentTimeMillis();
		CachedRowSet crs = databaseQuery.runQuery("SELECT COUNT(*) AS COUNT "
				+ "FROM world.city WHERE CountryCode = \"IND\";");
		LOGGER.info("Time taken for a normal statement: "
				+ (System.currentTimeMillis() - start));
		Assert.assertNotEquals(crs, null);
		Assert.assertEquals(crs.size(), 1);
		Assert.assertEquals(crs.next(), true);
		Assert.assertEquals(crs.getInt(1), 341);
	}

	@Test
	public void testPreparedStatement() throws SQLException {
		final long start = System.currentTimeMillis();
		PreparedStatement statement = databaseQuery.getPreparedStatement(
				"SELECT COUNT(*) AS COUNT "
						+ "FROM world.city WHERE CountryCode = ?");
		statement.setString(1, "IND");
		CachedRowSet crs = databaseQuery.runQuery(statement);
		LOGGER.info("Time taken for a prepared statement: "
				+ (System.currentTimeMillis() - start));
		Assert.assertNotEquals(crs, null);
		Assert.assertEquals(crs.size(), 1);
		Assert.assertEquals(crs.next(), true);
		Assert.assertEquals(crs.getInt(1), 341);
	}

}
