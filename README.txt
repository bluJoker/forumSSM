2019-12-30  JdbcTemplate整合为MyBatis时遇到的问题：

1、spring配置mybatis时，报了这个错误：org.apache.ibatis.session.Configuration.setVfsImpl(Ljava/lang/Class;)V
问题原因：mybatis的版本过低

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.3.0</version>
</dependency>

(1) 在DAO的mapper类名上需要加上@Mapper注解解决方案：
(2) 使用更高版本的mybatis（推荐方案），我使用的是<version.mybatis>3.4.1</version.mybatis>
大家只要记住一点如果在搭建spring框架时总是报各种莫名其妙的错误，那一定是引用的jar包版本问题！！！
当让如果用的是springboot搭建的，则不会出现莫须有的问题，因为springboot引用的jar包的依赖都是兼容好的，不需要我们分开引用。

2、UserMapper.xml中getMatchCount的SQL语句含多个参数，此时不能使用parameterType属性指定(只适用于单个参数)
解决方法：
    <select id="getMatchCount" resultType="java.lang.Integer">
        <!--方法1：-->
        <!--SELECT count(*) FROM t_user WHERE user_name = #{0} and password = #{1}-->

        <!--方法2：-->
        SELECT count(*) FROM t_user WHERE user_name = #{username} and password = #{password}
    </select>

// 方法2：基于注解，用@Param来指定哪一个
public int getMatchCount(@Param("username") String username,
                         @Param("password") String password);
___https://www.jianshu.com/p/d977eaadd1ed

3、LoginLogMapper.xml中insertLoginLog的SQL语句含三个参数，但在其对应的LoginLogMybatisDao.insertLoginLog(LoginLog loginLog)方法
参数为LoginLog类。此时，在LoginLogMapper.xml中可以通过#{属性名}取值，此属性名必须为LoginLog中的属性名。
___https://www.cnblogs.com/keyi/p/8509155.html

========================================================================================================================
2019-12-31  整合SpringMVC时遇到的问题：
1、配置Jetty插件后，打开Jetty配置的对应url：localhost:8000/bbs/index.html出错：

HTTP ERROR 500
Problem accessing /bbs/index.html. Reason:
    PWC6033: Unable to compile class for JSP

PWC6199: Generated servlet error:
The type java.lang.Class cannot be resolved. It is indirectly referenced from required .class files

PWC6199: Generated servlet error:
java.util.Vector cannot be resolved to a type

PWC6199: Generated servlet error:
_jspx_dependants cannot be resolvedCaused by:
org.apache.jasper.JasperException: PWC6033: Unable to compile class for JSP

PWC6199: Generated servlet error:
The type java.lang.Class cannot be resolved. It is indirectly referenced from required .class files

PWC6199: Generated servlet error:
java.util.Vector cannot be resolved to a type

PWC6199: Generated servlet error:
_jspx_dependants cannot be resolved

at org.apache.jasper.compiler.DefaultErrorHandler.javacError(DefaultErrorHandler.java:123)
at org.apache.jasper.compiler.ErrorDispatcher.javacError(ErrorDispatcher.java:296)
at org.apache.jasper.compiler.Compiler.generateClass(Compiler.java:376)
at org.apache.jasper.compiler.Compiler.compile(Compiler.java:437)
at org.apache.jasper.JspCompilationContext.compile(JspCompilationContext.java:608)
at org.apache.jasper.servlet.JspServletWrapper.service(JspServletWrapper.java:360)
at org.apache.jasper.servlet.JspServlet.serviceJspFile(JspServlet.java:486)
at org.apache.jasper.servlet.JspServlet.service(JspServlet.java:380)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:820)
at org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:511)
at org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:390)
at org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)
at org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:182)
at org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:765)
at org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:440)
at org.mortbay.jetty.servlet.Dispatcher.forward(Dispatcher.java:327)
at org.mortbay.jetty.servlet.Dispatcher.forward(Dispatcher.java:126)
at org.springframework.web.servlet.view.InternalResourceView.renderMergedOutputModel(InternalResourceView.java:168)
at org.springframework.web.servlet.view.AbstractView.render(AbstractView.java:303)
at org.springframework.web.servlet.DispatcherServlet.render(DispatcherServlet.java:1244)
at org.springframework.web.servlet.DispatcherServlet.processDispatchResult(DispatcherServlet.java:1027)
at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:971)
at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:893)
at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:707)
at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:820)
at org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:511)
at org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:390)
at org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)
at org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:182)
at org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:765)
at org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:440)
at org.mortbay.jetty.handler.ContextHandlerCollection.handle(ContextHandlerCollection.java:230)
at org.mortbay.jetty.handler.HandlerCollection.handle(HandlerCollection.java:114)
at org.mortbay.jetty.handler.HandlerWrapper.handle(HandlerWrapper.java:152)
at org.mortbay.jetty.Server.handle(Server.java:326)
at org.mortbay.jetty.HttpConnection.handleRequest(HttpConnection.java:542)
at org.mortbay.jetty.HttpConnection$RequestHandler.headerComplete(HttpConnection.java:926)
at org.mortbay.jetty.HttpParser.parseNext(HttpParser.java:549)
at org.mortbay.jetty.HttpParser.parseAvailable(HttpParser.java:212)
at org.mortbay.jetty.HttpConnection.handle(HttpConnection.java:404)
at org.mortbay.io.nio.SelectChannelEndPoint.run(SelectChannelEndPoint.java:410)
at org.mortbay.thread.QueuedThreadPool$PoolThread.run(QueuedThreadPool.java:582)
------------------------------------------------------------------------------------------------------------------------
Powered by Jetty://

原因可能为jdk版本(11)和jetty版本(6.1.25)不兼容，将jdk版本从11换为1.8即可运行成功。

2、login.jsp页面有两个用处，既作为登录页面，又作为登陆失败后的相应页面。所以在第9行，使用JSTL标签对登录错误返回的信息进行处理。
在JSTL标签中引用了error变量，这个变量正是LoginController中返回的ModelAndView("login", "error", "用户名和密码错误")对象所声明的error参数。