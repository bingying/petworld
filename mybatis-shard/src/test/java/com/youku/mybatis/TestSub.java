package com.youku.mybatis;

/**
 * @author 戴洵 <daixun@youku.com>
 *         Created on 2015/1/26.
 */
public class TestSub extends Test {

    String subValue;


    public TestSub(String subValue) {
        this.subValue = subValue;
    }

    public TestSub(int id, String value, String subValue) {
        super(id, value);
        this.subValue = subValue;
    }
}
