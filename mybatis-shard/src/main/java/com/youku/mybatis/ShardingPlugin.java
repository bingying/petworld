package com.youku.mybatis;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
//import org.perf4j.LoggingStopWatch;
//import org.perf4j.StopWatch;
//import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;

/**
 * User: Xun
 * Date: 13-6-3
 * Time: 下午2:49
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class ShardingPlugin implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(ShardingPlugin.class);

    private static final Field DELEGATE_FIELD;
    private static final Field MAPPEDSTATEMENT_FIELD;

    static {
        DELEGATE_FIELD = ReflectionUtils.findField(RoutingStatementHandler.class, "delegate");
        ReflectionUtils.makeAccessible(DELEGATE_FIELD);
        MAPPEDSTATEMENT_FIELD = ReflectionUtils.findField(BaseStatementHandler.class, "mappedStatement");
        ReflectionUtils.makeAccessible(MAPPEDSTATEMENT_FIELD);
    }

    /**
     * MyBatis wraps the JDBC Connection with a logging proxy but Spring registers the original connection so it should
     * be unwrapped before calling {@code DataSourceUtils.isConnectionTransactional(Connection, DataSource)}
     *
     * @param connection May be a {@code ConnectionLogger} proxy
     * @return the original JDBC {@code Connection}
     */
    private Connection unwrapMybatisConnection(Connection connection) {
        if (Proxy.isProxyClass(connection.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(connection);
            if (handler instanceof ConnectionLogger) {
                return ((ConnectionLogger) handler).getConnection();
            }
        }
        return connection;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        StopWatch stopWatch = new Slf4JStopWatch(LoggerFactory.getLogger(LoggingStopWatch.DEFAULT_LOGGER_NAME));
//        String prefix = this.getClass().getName() + ".";
        Connection conn = (Connection) invocation.getArgs()[0];
        Connection realConn = unwrapMybatisConnection(conn);
//        stopWatch.lap(prefix + "unwrapMybatisConnection");
        if (Proxy.isProxyClass(realConn.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(realConn);
            if (handler instanceof ShardingMybatisConnectionInvocationHandler) {
                StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
                ParameterHandler parameterHandler = statementHandler.getParameterHandler();

                MappedStatement mappedStatement;
                if (statementHandler instanceof RoutingStatementHandler) {
                    StatementHandler delegate = (StatementHandler) ReflectionUtils.getField(DELEGATE_FIELD, statementHandler);
                    mappedStatement = (MappedStatement) ReflectionUtils.getField(MAPPEDSTATEMENT_FIELD, delegate);
                } else {
                    mappedStatement = (MappedStatement) ReflectionUtils.getField(MAPPEDSTATEMENT_FIELD, statementHandler);
                }
                if(mappedStatement == null) {
                    throw new IllegalStateException();
                }
                Object parameter = parameterHandler.getParameterObject();
                ShardingMybatisConnectionInvocationHandler shardingMybatisHandler = (ShardingMybatisConnectionInvocationHandler) handler;
                shardingMybatisHandler.setMappedStatement(mappedStatement);
                shardingMybatisHandler.setParameter(parameter);
            } else {
                logger.info("InocationHandler is not RoutingStatementHandler.");
            }
        } else {
            logger.info("Connection is a real Connection , not a Proxy.");
        }
//        stopWatch.lap(prefix + "prepareShardingMybatisConnection");
//        Object result = invocation.proceed();
//        stopWatch.stop(prefix + "proceed");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
