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
    // 在使用Servlet API的类作为入参时，SpringMVC会自动将Web层对应的Servlet对象传递给处理方法的入参
    public ModelAndView loginCheck(HttpServletRequest request,
                                   LoginCommand loginCommand,
                                   String action){
        boolean isValidUser = userService.hasMatchUser(loginCommand.getUserName(),
                loginCommand.getPassword());

        if (action.equals("登录")){
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

                // Spring4.x_p587
                // 在准备对视图进行渲染前，SpringMVC还会进一步将模型中的数据转储到视图的上下文中并暴露给视图对象。对于JSP视图来说，SpringMVC
                // 会将模型数据转储到ServletRequest的属性列表中(通过ServletRequest#setAttribute(String name, Object o)方法保存)。
                // 这样WEB-INF/jsp/main.jsp视图就可以使用${user.username}等方式顺利地访问到模型中的数据了。
                request.getSession().setAttribute("user", user);
                return new ModelAndView("main");
            }
        }else if (action.equals("注册")){
            if (!isValidUser){
                //public ModelAndView(String viewName, String modelName, Object modelObject) {
                //this.view = viewName;
                //this.addObject(modelName, modelObject);
                //}
                User user = new User();
                user.setUserName(loginCommand.getUserName());
                user.setPassword(loginCommand.getPassword());
                user.setLastIp(request.getLocalAddr());
                user.setLastvisit(new Date());
                userService.loginSuccess(user);
                userService.register(user);

                // Spring4.x_p587
                // 在准备对视图进行渲染前，SpringMVC还会进一步将模型中的数据转储到视图的上下文中并暴露给视图对象。对于JSP视图来说，SpringMVC
                // 会将模型数据转储到ServletRequest的属性列表中(通过ServletRequest#setAttribute(String name, Object o)方法保存)。
                // 这样WEB-INF/jsp/main.jsp视图就可以使用${user.username}等方式顺利地访问到模型中的数据了。
                request.getSession().setAttribute("user", user);
                return new ModelAndView("main");
            }else {
                return new ModelAndView("login", "error", "用户名已存在");
            }
        }else {
            return null;
        }
    }
}
