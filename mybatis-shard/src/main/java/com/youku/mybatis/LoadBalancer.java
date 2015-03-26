package com.youku.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import javax.sql.DataSource;
import java.util.List;

/**
 * User: Xun
 * Date: 13-6-8
 * Time: 上午11:03
 */
public interface LoadBalancer {

    public DataSource select(List<DataSource> dataSourceList, MappedStatement mappedStatement, Object parameter);
}
