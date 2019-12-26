package com.smarter.service;

import com.smarter.dao.LoginLogDao;
import com.smarter.dao.UserDao;
import com.smarter.domain.LoginLog;
import com.smarter.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserDao userDao;
    private LoginLogDao loginLogDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setLoginLogDao(LoginLogDao loginLogDao) {
        this.loginLogDao = loginLogDao;
    }

    public boolean hasMatchUser(String username, String password){
        int matchCount = userDao.getMatchCount(username, password);
        return matchCount > 0;
    }

    public User findUserByUserName(String username){
        return userDao.findUserbyUsername(username);
    }

    public void loginSuccess(User user){
        user.setCredits(5 + user.getCredits());
        userDao.updateLoginInfo(user);
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(user.getLastvisit());
        loginLogDao.insertLoginLog(loginLog);
    }
}
