package com.youku.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface TestDao {
    public Date current(@Param("integer") int integer, @Param("string") String string);

    public Date current$Master(@Param("integer") int integer, @Param("string") String string);

    public void save(Test test);
}
