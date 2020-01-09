## 一、Spring整合MyBatis步骤
### 1、 写SQL映射文件 XXMapper.xml

UserMapper.xml中getMatchCount的SQL语句含多个参数，此时不能使用parameterType属性指定(只适用于单个参数)
解决方法：
```xml
<select id="getMatchCount" resultType="java.lang.Integer">

        <!--方法1：-->
        <!--SELECT count(*) FROM t_user WHERE user_name = #{0}
and password = #{1}-->

        <!--方法2：-->
        SELECT count(*) FROM t_user WHERE user_name =
#{username} and password = #{password}

</select>
```
// 方法2：基于注解，用@Param来指定哪一个

```java
public int getMatchCount(@Param("username") String username,

                         @Param("password") String password);

```
详见：[参考I](https://www.jianshu.com/p/d977eaadd1ed)


LoginLogMapper.xml中insertLoginLog的SQL语句含三个参数，但在其对应的

LoginLogMybatisDao.insertLoginLog(LoginLog loginLog)方法参数为LoginLog类。
此时，在LoginLogMapper.xml中可以通过#{属性名}取值，此属性名必须为LoginLog中的属性名。
详见：[参考II](https://www.cnblogs.com/keyi/p/8509155.html)
### 2、 写映射文件的对应接口

- 此接口所在包路径要与对应SQL映射文件的mapper的namespace命名空间一致
- 此接口中的方法要与映射文件中对应的SQL语句的id一致

### 3、 写MyBatis配置文件
此文件中可使用typeAliases属性配置类或包的别名，用于简化SQL映射文件中的sql语句的返回值类型(resultType)或参数类型（parameterType）
### 4、Spring对应xml中写MyBatis相关配置
```xml
<!--以下为Mybatis配置-->

<!--通过Spring风格创建MyBatis的SqlSessionFactory-->
<bean id="sqlSessionFactory"
class="org.mybatis.spring.SqlSessionFactoryBean">

    <!--注入数据源-->
    <property name="dataSource" ref="datasource"></property>

    <!--指定Mybatis的总装配置文件-->
    <property name="configLocation"
value="classpath:mybatis-config.xml"></property>

    <!--扫描式加载SQL映射文件，不需要在mybatis-config.xml中分别组装所有映射文件-->
    <!--SqlSessionFactoryBean将扫描tk/mybatis/simple/mapper类路径并加载所有以.xml为后缀的映射文件-->
    <property name="mapperLocations"
value="classpath:com/smarter/mybatis/mapper/*.xml"></property>

</bean>

<!--mybatis-spring提供一个转换器MapperScannerConfigurer-->
<!--它可以将映射接口直接转换为Spring容器中的Bean，这样就可以在Service中注入映射接口的Bean了-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="sqlSessionFactory"
ref="sqlSessionFactory"></property>

    <!--MapperScannerConfigurer将扫描basePackage所指定的包下的所有接口类(包括子包)-->
    <!--如果它们在SQL映射文件中定义过，则将为它们动态定义为一个Spring Bean-->
    <!--这样就可以在Service中直接注入映射接口的Bean了-->
    <property name="basePackage"
value="com.smarter.dao.mybatis"></property>

</bean>

```
（5） Service层注入(2)的接口实例，直接调用接口方法即可访问数据库


* * *
## 二、整合SpringMVC

> 本篇博文将搭建前端SpringMVC框架，通过网页访问本论坛登录系统。

### 前期准备工作

 - 配置SpringMVC相关依赖
 ```xml
 <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>4.2.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>3.0-alpha-1</version>
            <scope>provided</scope>
        </dependency>
 ```
- 配置运行Web应用所需的Jetty应用服务器插件：
 ```xml
 <build>
        <plugins>
            <!-- jetty插件 -->
            <plugin>
                <groupId>org.mortbay.jetty</groupId>
                <artifactId>maven-jetty-plugin</artifactId>
                <version>6.1.25</version>
                <configuration>
                    <connectors>
                        <connector implementation="org.mortbay.jetty.nio.SelectChannelConnector">
                            <port>8000</port>
                            <maxIdleTime>60000</maxIdleTime>
                        </connector>
                    </connectors>
                    <contextPath>/bbs</contextPath>
                    <scanIntervalSeconds>0</scanIntervalSeconds>
                </configuration>
            </plugin>
        </plugins>
    </build>
 ```

### 整合SpringMVC步骤如下：
##### 1、配置Web层的配置文件Web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

    <!--Web容器上下文参数contextConfigLocation-->
    <!--从类路径下加载Spring配置文件，这样位于Web层的Spring容器(WebApplicationContext)与位于业务层的Spring容器(ApplicationContext)-->
    <!--建立了关联，以使Web层的Bean可以调用业务层的Bean-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <!--此处不需要持久层的配置文件(classpath:mybatis-config.xml)，因为此项目Web层的Bean只调用了业务层的Bean-->
        <!--<param-value>classpath:smart-context.xml, classpath:mybatis-config.xml</param-value>-->
        <param-value>classpath:smart-context.xml</param-value>
    </context-param>

    <!--1、用于启动WebApplicationContext的Web容器监听器：org.springframework.web.context.ContextLoaderListener-->
    <!--内部实现了启动WebApplicationContext实例的逻辑-->
    <!--2、指定Spring所提供的的ContextLoaderListener的Web容器监听器，该监听器在Web容器启动时自动运行，-->
    <!--它会根据contextConfigLocation Web容器上下文参数获取Spring配置文件，并启动Spring容器。-->
    <listener>
        <listener-class>
            org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>

    <!--配置DispatcherServlet为SpringMVC的前端Servlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>
            org.springframework.web.servlet.DispatcherServlet
        </servlet-class>
        <load-on-startup>3</load-on-startup> //启动顺序
    </servlet>

    <!--指定DispatcherServlet截获特定的URL请求-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>*.html</url-pattern>
    </servlet-mapping>
</web-app>

```

##### 2、编写处理请求的控制器（处理器）
：@Controller注解可将一个POJO转化为处理请求的控制器

LoginController.java：
```java
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

            // Spring4.x_p587
            // 在准备对视图进行渲染前，SpringMVC还会进一步将模型中的数据转储到视图的上下文中并暴露给视图对象。对于JSP视图来说，SpringMVC
            // 会将模型数据转储到ServletRequest的属性列表中(通过ServletRequest#setAttribute(String name, Object o)方法保存)。
            // 这样WEB-INF/jsp/main.jsp视图就可以使用${user.username}等方式顺利地访问到模型中的数据了。
            request.getSession().setAttribute("user", user);
            return new ModelAndView("main");
        }
    }
}
```

LoginCommand.java：
```java
package com.smarter.controller;

public class LoginCommand {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

```

##### 3、编写视图对象
：这里使用JSP作为视图

- 登录初始页面：login.jsp
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<title>forum register</title>
	</head>
	<body>
		<c:if test="${!empty error}">
	        <font color="red"><c:out value="${error}" /></font>
		</c:if>
		<form action="<c:url value="loginCheck.html"/>" method="post">
			用户名：
			<input type="text" name="userName">
			<br>
			密 码：
			<input type="password" name="password">
			<br>
			<input type="submit" value="登录" />
			<input type="reset" value="重置" />
		</form>
	</body>
</html>
```

- 登录成功后页面：main.jsp
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>forum wangw</title>
</head>
<body>
    ${user.userName},welcome to forum wangw，your credits now ${user.credits};
</body>
</html>
```

##### 4、配置SpringMVC的配置文件
：（WEB-INF/springmvc-servlet.xml），使控制器、视图解析器等生效

springmvc-servlet.xml：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd">
    <!-- 扫描web包，应用Spring的注解 -->
    <context:component-scan base-package="com.smarter.controller"/>

    <!-- 配置视图解析器，将ModelAndView及字符串解析为具体的页面 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <!--如果JSP文件使用了JSTL的国际化功能，确切地说，当JSP页面使用JSTL的<fmt:message/>标签时，用户需要使用JstlView替换默认的-->
        <!--视图实现类(InternalResourceViewResolver)-->
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"></property>

        <!--前缀-->
        <property name="prefix" value="/WEB-INF/jsp/"></property>

        <!--后缀-->
        <property name="suffix" value=".jsp"></property>
    </bean>

</beans>
```

>以上就已经将SpringMVC框架搭建好了。<font size=3.5 color=red>现在从配置的网址登录论坛系统开始，详细讲解从HTTP请求发出到web服务器收到请求进行业务处理、获取持久层数据等到最终返回对应页面的整个流程。</front>

### 访问网址SpringMVC端处理流程
网址配置为：localhost:8000/bbs/index.html

##### 1、其中localhost:8000/bbs...
由pom.xml中的Jetty插件指定

##### 2、访问/index.html...
此时客户端发出了一个HTTP请求，Web应用服务器接收到这个请求，如果匹配DispatcherServlet的请求映射路径（在web.xml的servlet-mapping标签中指定了DispatcherServlet处理所有以.html为后缀的HTTP请求），则Web容器将该请求转交给该DispatcherServlet处理，也可以说所有带.html后缀的HTTP请求都会被该DispatcherServlet截获并处理

##### 3、DispatcherServlet接受到请求后...
将根据请求的信息（包括URL、HTTP方法、请求报文头、请求参数、Cookie等）及HandlerMapping的配置找到处理请求的处理器（Handler）。此时找到
```java
@RequestMapping(value = "/index.html")
    public String loginPage(){
    return "login";
}
```
##### 4、返回逻辑视图名"login"后...
将由springMVC配置文件配置的视图解析器中的规则将其解析为一个具体的视图对象：
/WEB-INF/jsp/login.jsp，SpringMVC会解析之并转向目标响应页面：
![登录页面](https://img-blog.csdnimg.cn/20200109112638836.png)
##### 5、login.jsp包括了一个表单...
输入用户名和密码后点击“登录”后，表单将提交到/loginCheck.html，重复步骤（3），找到处理器：
```java
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
```

- 如果用户名/密码不匹配，返回视图ModelAndView("login", "error", "用户名和密码错误");


<font color=red>login.jsp页面有两个用处，既作为登录页面，又作为登陆失败后的相应页面。
所以在第9行，使用JSTL标签对登录错误返回的信息进行处理。在JSTL标签中引用了error变量，这个变量正是LoginController中返回的ModelAndView("login", "error", "用户名和密码错误")对象所声明的error参数。</font>

登录失败后的响应页面：

![登陆失败](https://img-blog.csdnimg.cn/20200109112856486.png)
- 如果用户名/密码匹配

I、SpringMVC自动将login.jsp表单中的数据按参数名和LoginCommand属性名匹配的方式进行绑定，将参数值填充到LoginCommand的相应属性中。详见Spring4.x_p572

II、接着调用业务类（UserService）的方法进行登录成功的业务处理

III、返回逻辑视图名 return new ModelAndView("main")；视图解析器将其解析为视图对象：/WEB-INF/jsp/main.jsp
登陆成功后的响应页面：

![登录成功](https://img-blog.csdnimg.cn/2020010911295568.png)

参考
- 《精通Spring4.x——企业应用开发实战》

>本项目github地址：[https://github.com/bluJoker/forumSSM.git](https://github.com/bluJoker/forumSSM.git)
>版本：v4.1
