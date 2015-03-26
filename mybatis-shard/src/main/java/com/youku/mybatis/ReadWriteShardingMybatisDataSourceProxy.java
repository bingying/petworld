package com.youku.mybatis;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * User: Xun
 * Date: 13-6-7
 * Time: 上午11:30
 */
public class ReadWriteShardingMybatisDataSourceProxy extends AbstractShardingMybatisDataSourceProxy {

    private Logger logger = LoggerFactory.getLogger(ReadWriteShardingMybatisDataSourceProxy.class);

    public static final String SUFFIX_USE_MASTER = "$Master";

    private DataSource writeDataSource;

    private List<DataSource> readDataSources;

    private LoadBalancer loadBalancer;

    public DataSource getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public List<DataSource> getReadDataSources() {
        return readDataSources;
    }

    public void setReadDataSources(List<DataSource> readDataSources) {
        this.readDataSources = readDataSources;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected DataSource routeDataSource(MappedStatement mappedStatement, Object parameter) {

        if(mappedStatement == null) {
            logger.debug("MappedStatement is null. Force use writeDataSource: {}", writeDataSource);
            return writeDataSource;
        }
        if(TransactionSynchronizationManager.isSynchronizationActive()){
            logger.debug("Transactional. Force use writeDataSource: {}", writeDataSource);
            return writeDataSource;
        }

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        DataSource routedDataSource;
        if (SqlCommandType.SELECT == sqlCommandType){
            if(StringUtils.endsWith(mappedStatement.getId(), SUFFIX_USE_MASTER)) {
                routedDataSource = writeDataSource;
                logger.debug("Force use writeDataSource for {}: {}", sqlCommandType, routedDataSource);
            } else {
                routedDataSource = loadBalancer.select(readDataSources, mappedStatement, parameter);
                logger.debug("Select readDataSource for {}: {}", sqlCommandType, routedDataSource);
            }
        } else {
            routedDataSource = writeDataSource;
            logger.debug("Select writeDataSource for {}: {}", sqlCommandType, routedDataSource);
        }

        return routedDataSource;
    }

    @Override
    protected DataSource getMasterDataSource() {
        return writeDataSource;
    }

    @Override
    protected List<DataSource> getSlaveDataSources() {
        return readDataSources;
    }
}
