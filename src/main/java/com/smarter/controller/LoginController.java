package com.smarter.controller;

import com.smarter.domain.User;
import com.smarter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class LoginController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/index.html")
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = "/loginCheck.html")
    // SpringMVC自动将login.jsp表单中的数据按参数名和LoginCommand属性名匹配的方式进行绑定，将参数值填充到LoginCommand的相应属性中
    public ModelAndView loginCheck(HttpServletRequest request,
                                   LoginCommand loginCommand){
        boolean isValidUser = userService.hasMatchUser(loginCommand.getUserName(),
                loginCommand.getPassword());

        if (!isValidUser){
            //public ModelAndView(String viewName, String modelName, Object modelObject) {
                //this.view = viewName;
                //this.addObject(modelName, modelObject);
            //}
            return new ModelAndView("login", "error", "用户名和密码错误");
        }else {
            User user = userService.findUserByUserName(loginCommand.getUserName());
            user.setLastIp(request.getLocalAddr());
            user.setLastvisit(new Date());
            userService.loginSuccess(user);
            request.getSession().setAttribute("user", user);
            return new ModelAndView("main");
        }
    }
}
