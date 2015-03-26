package com.youku.mybatis;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author 戴洵 <daixun@youku.com>
 *         Created on 2015/1/26.
 */
public class TestReflectionUtils {
    public static void main(String[] args) {
        Test t1 = new Test(1, "1");
        Field valueField1 = ReflectionUtils.findField(Test.class, "value");
        ReflectionUtils.makeAccessible(valueField1);
        String value1 = (String) ReflectionUtils.getField(valueField1, t1);

        Test t2 = new Test(2, "2");
        Field valueField2 = ReflectionUtils.findField(t2.getClass(), "value");

        System.out.println("valueField1.equals(valueField2): " + valueField1.equals(valueField2));
        System.out.println("valueField1 == valueField2: " + (valueField1 == valueField2));

        String value2 = (String) ReflectionUtils.getField(valueField1, t2);

        System.out.println(String.format("value1 = %s, value2 = %s", value1, value2));

        ReflectionUtils.setField(valueField1, t1, "11");
        ReflectionUtils.setField(valueField1, t2, "22");

        System.out.println(String.format("t1 = %s, t2 = %s", t1.toString(), t2.toString()));


        TestSub ts1 = new TestSub(111, "111", "111");
        String values1 = (String) ReflectionUtils.getField(valueField1, ts1);
        System.out.println("values1: " + values1);


    }
}
