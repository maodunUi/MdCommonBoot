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