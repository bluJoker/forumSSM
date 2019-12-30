package com.smarter.dao.mybatis;

import com.smarter.domain.LoginLog;

public interface LoginLogMybatisDao {
    //保存登陆日志SQL
    public void insertLoginLog(LoginLog loginLog);
}
