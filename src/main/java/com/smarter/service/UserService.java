package com.smarter.service;

import com.smarter.dao.mybatis.LoginLogMybatisDao;
import com.smarter.dao.mybatis.UserMybatisDao;
import com.smarter.domain.LoginLog;
import com.smarter.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserMybatisDao userMybatisDao;
    private LoginLogMybatisDao loginLogMybatisDao;

    @Autowired
    public void setUserMybatisDao(UserMybatisDao userMybatisDao) {
        this.userMybatisDao = userMybatisDao;
    }

    @Autowired
    public void setLoginLogMybatisDao(LoginLogMybatisDao loginLogMybatisDao) {
        this.loginLogMybatisDao = loginLogMybatisDao;
    }

    public boolean hasMatchUser(String username, String password){
        int matchCount = userMybatisDao.getMatchCount(username, password);
        return matchCount > 0;
    }

    public User findUserByUserName(String username){
        return userMybatisDao.findUserbyUsername(username);
    }

    @Transactional
    public void loginSuccess(User user){
        user.setCredits(5 + user.getCredits());
        userMybatisDao.updateLoginInfo(user);

        // 手动抛出运行期异常，用于测试事务
//        if (true){
//            throw new RuntimeException("loginLog update failed!");
//        }

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(user.getLastvisit());
        loginLogMybatisDao.insertLoginLog(loginLog);
    }
}
