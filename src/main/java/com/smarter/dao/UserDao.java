package com.smarter.dao;

import com.smarter.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao {
    // SpringJDBC通过一个模板类org.springframework.jdbc.core.JdbcTemplate
    // 封装了样板式的代码，用户通过模板类就可以轻松地完成大部分数据访问操作
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 函数作用：根据用户名和密码获取匹配的用户数
    public int getMatchCount(String username, String password){
        String sql = "SELECT count(*) FROM t_user WHERE user_name = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username, password}, Integer.class);
    }

    // 1. 函数作用：根据用户名获取User对象
    // 2. jdbcTemplate.query：The following query finds and populates a number of domain objects
    // 即将从数据库获取的数据填入领域对象
    // 3. 通过匿名内部类的方式定义了一个RowCallbackHandler回调接口实例，将ResultSet转换为User对象
    public User findUserbyUsername(final String username){
        final User user = new User();

        String sql = "SELECT user_id,user_name,credits,last_ip,last_visit FROM t_user WHERE " +
                " user_name = ?";
        jdbcTemplate.query(sql, new Object[]{username},
                new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUserName(username);
                        user.setCredits(resultSet.getInt("credits"));
                        user.setLastIp(resultSet.getString("last_ip"));
                        user.setLastvisit(resultSet.getDate("last_visit"));
                    }
                });
        return user;
    }

    // 函数作用：更新用户积分、最后登录IP及最后登录时间
    public void updateLoginInfo(User user){
        String sql = "update t_user set credits = ?, last_visit = ?, last_ip = ? where user_id = ?";
        jdbcTemplate.update(sql, new Object[]{user.getCredits(), user.getLastvisit(),
                user.getLastIp(), user.getUserId()});
    }
}
