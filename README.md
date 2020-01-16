## 论坛登录系统

This is a simple forum system built by SSM[简易论坛登录]

### 项目说明
|   持久层   |   业务层   |  展现层  |  Web服务器  |
|   :----:     |    :----:    |   :----:   |   :--------------:  |
|  MyBatis  |  Spring声明式事务  |  Spring MVC  |  Jetty  |

### Change log
- 2019/11/26 Spring4.x-不带事务aop及SpringMVC的初级版本
- 2019/11/28 对业务类中loginSuccess方法添加事务管理(基于aop/tx命名空间配置)
- 2019/11/30 JdbcTemplate整合为MyBatis
- 2019/11/31 整合前端SpringMVC

### 运行截图
- 初始登录页面：

![](./images/main.png)
- 登录失败页面：

![](./images/loginFailed.png)

- 登录成功页面：

![](./images/loginSuccess.png)

- 注册成功页面：

![](./images/register.png)

