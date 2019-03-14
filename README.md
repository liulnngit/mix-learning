## 前后端分离框架
tony-architecture<br/>
tony-front<br/>
<https://blog.csdn.net/justloveyou_/article/details/74379479/><br/>
 在外包期间所做的CDH 就是采用了前后端分离技术<br/>
 注意：修改C:\Windows\System32\drivers\etc\hosts <br/>
 127.0.0.1 www.tony.com<br/>
 访问 http://www.tony.com:8080/tony-front/front.html 即可并做了权限控制<br/>
 
 tony-loadbalance 负载均衡测试，存在疑问？？？<br/>
 
---
## 缓存雪崩
tony-cache-stampeding <br/>
1. TicketService 一般的通过缓存去取数据，然后设置失效时间
2. TicketService2 通过主备缓存，以及ConcurrentHashMap进行缓存降级
3. TicketServiceLock 通过使用Lock机制


---
## 分布式调用追踪
tony-web-b --- tony-web-c<br/>
先安装Zipkin 参考了解 https://blog.csdn.net/upsuperman/article/details/78048972<br/>
然后启动访问  http://localhost:8080/tony-web-b/index<br/>
可以查询调用次数，以及调用依赖  http://localhost:9411/<br/>
具体了解<http://daixiaoyu.com/distributed-tracing.html/><br/>

---
## 秒杀
tony-miaoshao 实现购物的秒杀，提供秒杀解决方案

---
## dubbo-rpc原理

### 简单的dubbo调用
- demo-service-define   dubbo服务API
- demo-service-provider dubbo服务provider
- demo-service-consumer dubbo服务consumer

### 自定义dubbo服务消费者
- tony-dubbo-consumer/client 自己写一个dubbo消费者，去调用服务提供者    前提启动好dubbo的服务

### 模拟两个服务调用远程服务
- user-base-system 服务提供者
- demo-a 服务消费者A
- demo-b 服务消费者B

---
## 手写连接池
** tony-jdbc-pool **<br/>
手写一个数据库连接池

---
## 页面静态化
** tony-mall-details**<br/>
 实现页面静态化

---
## nio编程实战
**tony-nio**<br/>
测试NIO，和BIO

---
## 手写redis客户端
- jedis-test redis的哨兵机制
- tony-redis 手写一个redis的客户端

---
## 手写能部署war包的tomcat

- servlet-demo 原始版servlet<br/>
<http://localhost:8080/servlet-demo/index/>
- springmvc-demo springmvc<br/>
<http://localhost:8080/springmvc-demo/index/>
- tony-servlet-container Servlet容器

---
## springcloud-eureka
- eureka(lession-1-eureka) 服务注册和发现
- sms-interface(lession-1-sms-interface) 注册服务到eureka
- sms-webmvc(lession-1-sms-webmvc) webmvc使用eureka
- website(lession-1-website) springboot使用eureka

---
## 多线程Future模式原理剖析
- **Tony-thread-future** 使用多线程异步加载Callable方式
- **Tony-test** SpringBoot测试项目

---
## JVM类加载机制-tomcat热部署原理分析

---

