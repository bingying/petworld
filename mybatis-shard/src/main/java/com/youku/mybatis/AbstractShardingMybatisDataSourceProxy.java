package com.youku.mybatis;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

/**
 * User: Xun
 * Date: 13-6-3
 * Time: 下午3:22
 */
public abstract class AbstractShardingMybatisDataSourceProxy extends AbstractDataSource implements InitializingBean {

    /** Constants instance for TransactionDefinition */
    private static final Constants constants = new Constants(Connection.class);

    private static final Log logger = LogFactory.getLog(AbstractShardingMybatisDataSourceProxy.class);

    private Boolean defaultAutoCommit = true;

    private Integer defaultTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;

    /**
     * 根据mybatis的MappedStatement mappedStatement, Object parameter路由到正确的DataSource
     * @param mappedStatement
     * @param parameter
     * @return
     */
    abstract protected DataSource routeDataSource(MappedStatement mappedStatement, Object parameter);

    /**
     * 获取Master库的数据源
     * @return
     */
    abstract protected DataSource getMasterDataSource();

    /**
     * 获取Slave的数据源
     * @return
     */
    abstract protected List<DataSource> getSlaveDataSources();

    /**
     * Set the default auto-commit mode to expose when no target Connection
     * has been fetched yet (-> actual JDBC Connection default not known yet).
     * <p>If not specified, the default gets determined by checking a target
     * Connection on startup. If that check fails, the default will be determined
     * lazily on first access of a Connection.
     * @see java.sql.Connection#setAutoCommit
     */
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    /**
     * Set the default transaction isolation level to expose when no target Connection
     * has been fetched yet (-> actual JDBC Connection default not known yet).
     * <p>This property accepts the int constant value (e.g. 8) as defined in the
     * {@link java.sql.Connection} interface; it is mainly intended for programmatic
     * use. Consider using the "defaultTransactionIsolationName" property for setting
     * the value by name (e.g. "TRANSACTION_SERIALIZABLE").
     * <p>If not specified, the default gets determined by checking a target
     * Connection on startup. If that check fails, the default will be determined
     * lazily on first access of a Connection.
     * @see #setDefaultTransactionIsolationName
     * @see java.sql.Connection#setTransactionIsolation
     */
    public void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }

    /**
     * Set the default transaction isolation level by the name of the corresponding
     * constant in {@link java.sql.Connection}, e.g. "TRANSACTION_SERIALIZABLE".
     * @param constantName name of the constant
     * @see #setDefaultTransactionIsolation
     * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
     * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
     * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
     * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
     */
    public void setDefaultTransactionIsolationName(String constantName) {
        setDefaultTransactionIsolation(constants.asNumber(constantName).intValue());
    }


    @Override
    public void afterPropertiesSet() {

        // Determine default auto-commit and transaction isolation
        // via a Connection from the target DataSource, if possible.
        if (this.defaultAutoCommit == null || this.defaultTransactionIsolation == null) {
            try {
                Connection con = getMasterDataSource().getConnection();
                try {
                    checkDefaultConnectionProperties(con);
                }
                finally {
                    con.close();
                }
            }
            catch (SQLException ex) {
                logger.warn("Could not retrieve default auto-commit and transaction isolation settings", ex);
            }
        }
    }

    /**
     * Check the default connection properties (auto-commit, transaction isolation),
     * keeping them to be able to expose them correctly without fetching an actual
     * JDBC Connection from the target DataSource.
     * <p>This will be invoked once on startup, but also for each retrieval of a
     * target Connection. If the check failed on startup (because the database was
     * down), we'll lazily retrieve those settings.
     * @param con the Connection to use for checking
     * @throws java.sql.SQLException if thrown by Connection methods
     */
    protected synchronized void checkDefaultConnectionProperties(Connection con) throws SQLException {
        if (this.defaultAutoCommit == null) {
            this.defaultAutoCommit = con.getAutoCommit();
        }
        if (this.defaultTransactionIsolation == null) {
            this.defaultTransactionIsolation = con.getTransactionIsolation();
        }
    }

    /**
     * Expose the default auto-commit value.
     */
    protected Boolean defaultAutoCommit() {
        return this.defaultAutoCommit;
    }

    /**
     * Expose the default transaction isolation value.
     */
    protected Integer defaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }


    /**
     * Return a Connection handle that lazily fetches an actual JDBC Connection
     * when asked for a Statement (or PreparedStatement or CallableStatement).
     * <p>The returned Connection handle implements the ConnectionProxy interface,
     * allowing to retrieve the underlying target Connection.
     * @return a lazy Connection handle
     * @see org.springframework.jdbc.datasource.ConnectionProxy#getTargetConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class[]{ConnectionProxy.class},
                new ShardingMybatisConnectionInvocationHandler(this, this.defaultAutoCommit(), this.defaultTransactionIsolation()));
    }

    /**
     * Return a Connection handle that lazily fetches an actual JDBC Connection
     * when asked for a Statement (or PreparedStatement or CallableStatement).
     * <p>The returned Connection handle implements the ConnectionProxy interface,
     * allowing to retrieve the underlying target Connection.
     * @param username the per-Connection username
     * @param password the per-Connection password
     * @return a lazy Connection handle
     * @see org.springframework.jdbc.datasource.ConnectionProxy#getTargetConnection()
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class[] {ConnectionProxy.class},
                new ShardingMybatisConnectionInvocationHandler(this, username, password, this.defaultAutoCommit(), this.defaultTransactionIsolation()));
    }

}
