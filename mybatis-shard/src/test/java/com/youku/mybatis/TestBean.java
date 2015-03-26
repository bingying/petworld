package com.youku.mybatis;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Xun
 * Date: 13-6-7
 * Time: 上午11:50
 */
public class TestBean {
    private static final Logger logger = LoggerFactory.getLogger(TestBean.class);

    @Autowired
    private TestDao testDao;

    public TestDao getTestDao() {
        return testDao;
    }

    public void setTestDao(TestDao testDao) {
        this.testDao = testDao;
    }

    public void test(){

        testWrite();

        testRead();;

    }

    public void testWrite() {
        com.youku.mybatis.Test test = new com.youku.mybatis.Test(100, "100");
        testDao.save(test);
        int testId = test.getId();
        logger.info("=== Saved test id is: {}. ===", testId);
    }

    public void testRead(){
        Date current = testDao.current(100, "100");
        logger.info("=== Selected current is: {}. ===", current);
    }

    public void testReadMaster(){
        Date current = testDao.current$Master(100, "100");
        logger.info("=== Selected current from master is: {}. ===", current);
    }
}
