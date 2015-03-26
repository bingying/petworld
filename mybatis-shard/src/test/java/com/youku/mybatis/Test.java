package com.youku.mybatis;

/**
 * User: Xun
 * Date: 13-6-6
 * Time: 下午7:46
 */
public class Test {
    private int id;
    private String value;

    public Test() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Test(int id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
