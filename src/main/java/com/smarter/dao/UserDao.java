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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getMatchCount(String username, String password){
        String sql = "SELECT count(*) FROM t_user WHERE user_name = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username, password}, Integer.class);
    }

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

    public void updateLoginInfo(User user){
        String sql = "update t_user set credits = ?, last_visit = ?, last_ip = ? where user_id = ?";
        jdbcTemplate.update(sql, new Object[]{user.getCredits(), user.getLastvisit(),
                user.getLastIp(), user.getUserId()});
    }
}
