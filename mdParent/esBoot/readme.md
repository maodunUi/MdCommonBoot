springboot官网
https://spring.io/projects/spring-boot#learn
我这里使用了版本2.5.14
找版本--》Dependency Versions --》 https://docs.spring.io/spring-boot/docs/2.5.14/reference/html/dependency-versions.html#appendix.dependency-versions
autoconfiguration --> https://docs.spring.io/spring-boot/docs/2.5.14/reference/html/features.html#features.developing-auto-configuration


## condition原理
1. 入口spring中ConfigurationClassPostProcessor
2. org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass中当condition的match为true时会把配置类注入spring中。当返回false就不会。
3. springboot中SpringBootCondition类实现了condition 去负责解析springboot的condition类
   1. org\springframework\boot\autoconfigure\condition


## es
### 索引分片
1. 索引配置

   ```json
   "settings": {
               "index": {
                   "creation_date": "1663137677647",
                   "number_of_shards": "1", // 分片数量
                   "number_of_replicas": "1", // 副本数量
                   "uuid": "r4e7LDbSR2G8I0VHSOFpxw",
                   "version": {
                       "created": "7080099"
                   },
                   "provided_name": "shopping"
               }
           }
   ```

2. 分片数量一开始就定义好后不能修改。可以修改副本数量 来增加查询速度。另外 随着节点的增加和删除 分片会重新分配给各个节点。
3. 路由计算 share=hash(routing)%number_of_primary_shards。因此分片数量确定好了 就不能修改。因为会导致之前的数据路由错误了。由于有路由计算 所以所有的节点都可以有能力知道数据在哪个分片上面。
   1. 我们发送请求最好应该轮询所有的节点。
4. 