package com.youku.mybatis;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@ContextConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
public class TestReadWriteShardingMybatisDataSourceProxy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TestBean testBean;

//    @org.junit.Test
    public void test() {
        long begin = System.currentTimeMillis();
        int loop = 100;
        for (int i = 0; i < loop; i++) {
            testBean.testReadMaster();
        }
        for (int i = 0; i < loop; i++) {
            testBean.testRead();
        }
        for (int i = 0; i < loop; i++) {
            testBean.testWrite();
        }
        logger.info("Cost {} ms.", System.currentTimeMillis() - begin);
    }
}
