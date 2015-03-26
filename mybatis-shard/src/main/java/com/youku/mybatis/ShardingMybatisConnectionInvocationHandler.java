package com.youku.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
//import org.perf4j.LoggingStopWatch;
//import org.perf4j.StopWatch;
//import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Invocation handler that defers fetching an actual JDBC Connection
 * until first creation of a Statement.
 * User: Xun
 * Date: 13-6-7
 * Time: 上午10:43
 */
public class ShardingMybatisConnectionInvocationHandler implements InvocationHandler {

    private Logger logger = LoggerFactory.getLogger(ShardingMybatisConnectionInvocationHandler.class);

    private final String STASTISTIC_LOG_PREFIX = this.getClass().getName() + ".";

    private String username;

    private String password;

    private Boolean readOnly = Boolean.FALSE;

    private Integer transactionIsolation;

    private Boolean autoCommit;

    private boolean closed = false;

    private Connection target;

    private MappedStatement mappedStatement;

    private Object parameter;

    private AbstractShardingMybatisDataSourceProxy dataSource;

    public ShardingMybatisConnectionInvocationHandler(AbstractShardingMybatisDataSourceProxy dataSource, Boolean autoCommit, Integer transactionIsolation) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
        this.transactionIsolation = transactionIsolation;
    }

    public ShardingMybatisConnectionInvocationHandler(AbstractShardingMybatisDataSourceProxy dataSource, String username, String password, Boolean autoCommit, Integer transactionIsolation) {
        this(dataSource, autoCommit, transactionIsolation);
        this.username = username;
        this.password = password;
    }

    public MappedStatement getMappedStatement() {
        return mappedStatement;
    }

    public void setMappedStatement(MappedStatement mappedStatement) {
        this.mappedStatement = mappedStatement;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public AbstractShardingMybatisDataSourceProxy getDataSource() {
        return dataSource;
    }

    public void setDataSource(AbstractShardingMybatisDataSourceProxy dataSource) {
        this.dataSource = dataSource;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        StopWatch stopWatch = new Slf4JStopWatch(LoggerFactory.getLogger(LoggingStopWatch.DEFAULT_LOGGER_NAME));

        // Invocation on ConnectionProxy interface coming in...
        if (method.getName().equals("equals")) {
            // We must avoid fetching a target Connection for "equals".
            // Only consider equal when proxies are identical.
            return (proxy == args[0]);
        }
        else if (method.getName().equals("hashCode")) {
            // We must avoid fetching a target Connection for "hashCode",
            // and we must return the same hash code even when the target
            // Connection has been fetched: use hashCode of Connection proxy.
            return System.identityHashCode(proxy);
        }
        else if (method.getName().equals("unwrap")) {
            if (((Class) args[0]).isInstance(proxy)) {
                return proxy;
            }
        }
        else if (method.getName().equals("isWrapperFor")) {
            if (((Class) args[0]).isInstance(proxy)) {
                return true;
            }
        }
        else if (method.getName().equals("getTargetConnection")) {
            // Handle getTargetConnection method: return underlying connection.
            return getTargetConnection(method);
        }

        if (!hasTargetConnection()) {
            // No physical target Connection kept yet ->
            // resolve transaction demarcation methods without fetching
            // a physical JDBC Connection until absolutely necessary.

            if (method.getName().equals("toString")) {
                return "Connection proxy for sharding mybatis data source [" + getDataSource() + "]";
            }
            else if (method.getName().equals("isReadOnly")) {
                return this.readOnly;
            }
            else if (method.getName().equals("setReadOnly")) {
                this.readOnly = (Boolean) args[0];
                return null;
            }
            else if (method.getName().equals("getTransactionIsolation")) {
                if (this.transactionIsolation != null) {
                    return this.transactionIsolation;
                }
                // Else fetch actual Connection and check there,
                // because we didn't have a default specified.
            }
            else if (method.getName().equals("setTransactionIsolation")) {
                this.transactionIsolation = (Integer) args[0];
                return null;
            }
            else if (method.getName().equals("getAutoCommit")) {
                if (this.autoCommit != null) {
                    return this.autoCommit;
                }
                // Else fetch actual Connection and check there,
                // because we didn't have a default specified.
            }
            else if (method.getName().equals("setAutoCommit")) {
                this.autoCommit = (Boolean) args[0];
                return null;
            }
            else if (method.getName().equals("commit")) {
                // Ignore: no statements created yet.
                return null;
            }
            else if (method.getName().equals("rollback")) {
                // Ignore: no statements created yet.
                return null;
            }
            else if (method.getName().equals("getWarnings")) {
                return null;
            }
            else if (method.getName().equals("clearWarnings")) {
                return null;
            }
            else if (method.getName().equals("close")) {
                // Ignore: no target connection yet.
                this.closed = true;
                return null;
            }
            else if (method.getName().equals("isClosed")) {
                return this.closed;
            }
            else if (this.closed) {
                // Connection proxy closed, without ever having fetched a
                // physical JDBC Connection: throw corresponding SQLException.
                throw new SQLException("Illegal operation: connection is closed");
            }
        }
        // Target Connection already fetched,
        // or target Connection necessary for current operation ->
        // invoke method on target connection.
//        StopWatch connStopWatch = new Slf4JStopWatch(LoggerFactory.getLogger(LoggingStopWatch.DEFAULT_LOGGER_NAME));
        Connection connToUse = getTargetConnection(method);
//        connStopWatch.stop(STASTISTIC_LOG_PREFIX + method.getName() + ".getTargetConnection");

//        StopWatch targetStopWatch = new Slf4JStopWatch(LoggerFactory.getLogger(LoggingStopWatch.DEFAULT_LOGGER_NAME));
        try {
            return method.invoke(connToUse, args);
        }
        catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
//        finally {
//            stopWatch.stop(STASTISTIC_LOG_PREFIX + method.getName());
//            targetStopWatch.stop(STASTISTIC_LOG_PREFIX + method.getName() + ".target");
//        }
    }

    /**
     * Return whether the proxy currently holds a target Connection.
     */
    private boolean hasTargetConnection() {
        return (this.target != null);
    }

    /**
     * Return the target Connection, fetching it and initializing it if necessary.
     */
    private Connection getTargetConnection(Method operation) throws SQLException {
        if (this.target == null) {
            // No target Connection held -> fetch one.
            if (logger.isDebugEnabled()) {
                logger.debug("Connecting to database for operation '{}'", operation.getName());
            }

            DataSource dataSource = getDataSource().routeDataSource(getMappedStatement(), getParameter());
            if (logger.isDebugEnabled()) {
                logger.debug("Routed DataSource is: {}", dataSource);
            }

            // Fetch physical Connection from DataSource.
            this.target = (this.username != null) ?
                    dataSource.getConnection(this.username, this.password) :
                    dataSource.getConnection();

            // If we still lack default connection properties, check them now.
            getDataSource().checkDefaultConnectionProperties(this.target);

            // Apply kept transaction settings, if any.
            if (this.readOnly) {
                try {
                    this.target.setReadOnly(this.readOnly);
                }
                catch (Exception ex) {
                    // "read-only not supported" -> ignore, it's just a hint anyway
                    logger.debug("Could not set JDBC Connection read-only", ex);
                }
            }
            if (this.transactionIsolation != null &&
                    !this.transactionIsolation.equals(getDataSource().defaultTransactionIsolation())) {
                this.target.setTransactionIsolation(this.transactionIsolation);
            }
            if (this.autoCommit != null && this.autoCommit != this.target.getAutoCommit()) {
                this.target.setAutoCommit(this.autoCommit);
            }
        }

        else {
            // Target Connection already held -> return it.
            if (logger.isDebugEnabled()) {
                logger.debug("Using existing database connection for operation '" + operation.getName() + "'");
            }
        }
        return this.target;
    }
}