# 介绍
## 设计
### 缓存
1. 秒杀

### 权限 登录
### 风控
### 支付
### 限流
### 商品管理
### 订单管理
### 优惠券
### 对账
### 幂等设计
查询/删除 天然幂等

更新
1. update set x = y where xxx 幂等
2. update set x = x - 1 where xxx 对where条件进行限制
   创建
   创建 分布式锁 数据库锁
   业务方mq 或者 前端 每次请求的数据 都生成一个唯一值 可以解决网络的重复发送。
   insert into xxx
   insert into xxx

其他：
前端做防重

接口打击：
业务限制。比如流水 一正一负。不能多个负。

## 公共
### esBoot