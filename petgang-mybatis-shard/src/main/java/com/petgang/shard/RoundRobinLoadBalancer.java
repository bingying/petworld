package com.petgang.shard;

import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Xun
 * Date: 13-6-8
 * Time: 上午11:10
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private Logger logger = LoggerFactory.getLogger(RoundRobinLoadBalancer.class);

    private final AtomicInteger sequence = new AtomicInteger();

    @Override
    public DataSource select(List<DataSource> dataSourceList, MappedStatement mappedStatement, Object parameter) {
        if (CollectionUtils.isEmpty(dataSourceList)) {
            return null;
        } else {
            // 取模轮循
            int length = dataSourceList.size();
            if(sequence.get() >= Integer.MAX_VALUE) {
                sequence.set(0);
            }
            int index = sequence.get() % length;
            DataSource dataSource = dataSourceList.get(index);
            logger.debug("Select data source index {}", index);
            return dataSource;
        }
    }
}
