SQLDatabaseQuery
================

SQL database query tools backed by connection pooling.

Sample code for initialization:
````java
// Initialize connection pool
IObjectPool<WrappedConnection> jdbcPool =
  new ObjectPool<>("sql.properties",
    new AcquireJDBCConnection("sql.properties")
  );

// Create a new instance of querying class which will take care
// of checking out a connection, creating a statement, executing
// the query and returning the connection to the pool.
IDatabaseQuery databaseQuery = new DatabaseQueryV2(jdbcPool);

// Execute the query
CachedRowSet crs = databaseQuery.runQuery("SELECT COUNT(*) AS COUNT "
  + "FROM world.city WHERE CountryCode = \"IND\";");

// Return connection
databaseQuery.closeConnection();

// ...
// Iterate over cached row set.
// You can iterate over it even after connection is closed.
// ...

// Shut down the connection pool
jdbcPool.closePool(true);
````

Sample configuration file: ![sql.properties](http://github.opslab.sv2.tellme.com/OnlineAutomation/SQLDatabaseQuery/raw/master/conf/sql.properties)
<br/>It doesn't have to be named redis.properties. As long as the parameter names are same, any file could be specified during initialization.
