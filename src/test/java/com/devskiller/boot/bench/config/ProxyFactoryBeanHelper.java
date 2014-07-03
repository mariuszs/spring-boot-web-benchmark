package com.devskiller.boot.bench.config;

import com.devskiller.boot.bench.api.FooService;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class ProxyFactoryBeanHelper {

    public static HessianProxyFactoryBean hessianProxyFactoryBean(int port) {
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        factory.setServiceUrl(String.format("http://localhost:%s/fooService", port));
        factory.setServiceInterface(FooService.class);
        return factory;
    }

    public static HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean(int port) {
        HttpInvokerProxyFactoryBean factory = new HttpInvokerProxyFactoryBean();
        factory.setServiceUrl(String.format("http://localhost:%s/fooHttpInvokerService", port));
        factory.setServiceInterface(FooService.class);
        return factory;
    }
}
