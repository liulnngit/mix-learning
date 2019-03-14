# RESP协议内容
https://redis.io/topics/protocol
  **简单、快、可读性强**

" + "    单行信息
   
 " - "    错误信息
    
 " : "    整形数字
      
 " $ "    多行字符串
    
 " * "    数组

# 掌握通信协议，可以应用在哪些场景？
> 1、 客户端开发

> 2、实现redis代理（分布式redis解决方案，通过分片存储内存无限大）

# 著名的redis相关开源项目：
> Jedis		https://github.com/xetorthio/jedis

> Redisson 		https://github.com/redisson/redisson

> 推特开源Twemproxy  https://github.com/twitter/twemproxy

> 豌豆荚团队开源codis https://github.com/CodisLabs/codis

> 官方高可用方案：sentinel（哨兵）


# 代理
场景：根据key的不同，将数据存储在不同的redis服务上
启动三个redis实例
./redis-server --port 6380

./redis-server --port 6381

./redis-server --port 6382
