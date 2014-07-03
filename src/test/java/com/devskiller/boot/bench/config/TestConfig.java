package com.devskiller.boot.bench.config;

import com.devskiller.boot.bench.api.FooService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

@Configuration
public class TestConfig {

    @Bean
    @Hessian
    public HessianProxyFactoryBean fooHessianClient() {
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        factory.setServiceUrl("http://localhost:8080/fooService");
        factory.setServiceInterface(FooService.class);
        return factory;
    }
    @Bean
    @HttpInvoker
    public HttpInvokerProxyFactoryBean fooHttpInvokerClient() {
        HttpInvokerProxyFactoryBean factory = new HttpInvokerProxyFactoryBean();
        factory.setServiceUrl("http://localhost:8080/fooHttpInvokerService");
        factory.setServiceInterface(FooService.class);
        return factory;
    }
}
