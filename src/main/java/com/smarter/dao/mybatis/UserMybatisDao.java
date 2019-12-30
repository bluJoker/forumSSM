package com.smarter.dao.mybatis;
import com.smarter.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMybatisDao {

    // 1. 函数作用：根据用户名和密码获取匹配的用户数
    // 2. 因为对应的select语句有多个入参，故不能使用parameterType属性
    // 解决办法：https://www.jianshu.com/p/d977eaadd1ed
    // 方法1：改用#｛index｝是第几个就用第几个的索引，索引从0开始，见user.xml文件
    // public int getMatchCount(String username, String password);

    // 方法2：基于注解，用@Param来指定哪一个
     public int getMatchCount(@Param("username") String username,
                              @Param("password") String password);


    // 函数作用：根据用户名获取User对象
    public User findUserbyUsername(String username);

    // 函数作用：更新用户积分、最后登录IP及最后登录时间
    public void updateLoginInfo(User user);
}
