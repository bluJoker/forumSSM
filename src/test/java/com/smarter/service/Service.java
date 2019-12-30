package com.smarter.service;

import com.smarter.domain.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class Service {
    @Test
    public void test(){
        ApplicationContext context =
                new ClassPathXmlApplicationContext("smart-context.xml");
        UserService userService =
                context.getBean("userService", UserService.class);
        boolean b1 = userService.hasMatchUser("admin", "123456");
        boolean b2 = userService.hasMatchUser("admin", "1111");

        System.out.println(b1);
        System.out.println(b2);

        User user = userService.findUserByUserName("admin");
        System.out.println(user);


        user.setUserId(1);
        user.setUserName("admin");
        user.setLastIp("192.168.12.7");
        user.setLastvisit(new Date());
        userService.loginSuccess(user);
    }
}
