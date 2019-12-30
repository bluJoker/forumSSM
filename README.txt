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