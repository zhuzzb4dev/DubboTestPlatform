package com.example.dubbotestplatform;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @PostMapping("invoke")
    public Object invokeDubbo(@RequestBody @Validated DubboInfoReq req) {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("generic-consumer");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(req.getAddress());

        // 引用远程服务
        // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        // 弱类型接口名
        reference.setInterface(req.getInterFaceName());
        reference.setVersion(req.getVersion());
        // 声明为泛化接口
        reference.setGeneric("true");
        reference.setApplication(application);
        reference.setRegistry(registry);

        // 用org.apache.dubbo.rpc.service.GenericService可以替代所有接口引用
        GenericService genericService = reference.get();

        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke(req.getMethod(), req.getParameterTypes(), req.getArgs());

        // 用Map表示POJO参数，如果返回值为POJO也将自动转成Map
//        Map<String, Object> person = new HashMap<String, Object>();
//        person.put("name", "xxx");
//        person.put("password", "yyy");
//        // 如果返回POJO将自动转成Map
//        Object result = genericService.$invoke("findPerson", new String[]
//                {"com.xxx.Person"}, new Object[]{person});

        return result;
    }
}
