# 介绍
## 设计
### 缓存
1. 秒杀

### 权限 登录
### 风控
### 支付
#### 加密：
对称加密：aes
公钥加密 公钥解密
非对称加密：rsa
私钥加密 公钥解密

摘要算法：md5 sha1 sha2
散列函数 哈希函数。不可逆

做法1：
   先使用摘要算法 生成摘要信息后用私钥进行加密生成签名。
做法2：
   先使用摘要算法 生成摘要信息后用私钥进行加密生成签名。后原文再进行对称加密。

公钥的颁发容易伪造：
   利用证书。证书来获取公钥。
   ca的公钥一般默认在操作系统中。利用ca的公钥进行解密。

#### 微信支付：
https://pay.weixin.qq.com/wiki/doc/apiv3_partner/wechatpay/wechatpay3_1.shtml
商户api证书
由证书请求串提交到商户平台后才能获得商户API证书文件，私钥文件
商户api密钥
商户申请商户API证书时，会生成商户私钥
平台证书
微信支付平台证书是指由微信支付 负责申请的，包含微信支付平台标识、公钥信息的证书。商户可以使用平台证书中的公钥进行验签。
不同的商户，对应的微信支付平台证书是不一样的
平台证书会周期性更换。商户应定时通过API下载新的证书。请参考我们的更新指引 ，不要依赖人工更新证书。

API v3密钥
为了保证安全性，微信支付在 回调通知和平台证书下载接口中，对关键信息进行了AES-256-GCM加密。API v3密钥是加密时使用的对称密钥。
### 限流
### 商品管理
### 订单管理
### 优惠券
### 对账
### 幂等设计
https://mp.weixin.qq.com/s/QufXfnJj5kPX8K3M5gICqw
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


### 接口设计
转转：https://mp.weixin.qq.com/s/_BrTELBzV_syX-lIktjPRQ


### 分库分表
https://tech.meituan.com/2016/11/18/dianping-order-db-sharding.html

分库分表后数据迁移：
不停机：在线双写


### 命名
方法命名：参考：ExtendDAOImpl
增(create insert save)
save(T t)
save(List<T> t)
删(delete)
deleteById
deleteAll
deleteByIds
deleteByCondition
改(update)
update(T t)
update(List<T> t)


查(get find select search)
getById
getAll
getByIds
getByCondition
getOneByCondition

getDetailById
getDetailAll
getDetailByIds
getDetailByCondition
getOneDetailByCondition

其他：
saveOrUpdate
existsByCondition
countByCondition

### business
用户
渠道：100 （bs_channel）
店员：200+ (bs_saleman)

前端会员：5500+ （通过微信引入的） bs_member
大客户：100 bs_client nb_customer
app端：100 sl_union_account

订单：
回收单 0.2bill
付款单：0.05bill
闪优优ml_order:1000
实时竞拍：4000
app:5000
售后：100
出库：3500

服务：
业务服务：
shanhs-fast-gateway 40
shs-open-platform-gateway 15
shs-fast-bid-api 40
offline-sapi 40
sales-app-api	15
shs-pay-api	15
bi-center 25

redis
es
rocktemq
mysql

接口：

架构：
整体技术：
springCloud nacos rocketmq xxx-job es redis mysql jooq
网关设计：
k8s设计：
环境：
dev test pre pro
云环境：
华为云
阿里云


