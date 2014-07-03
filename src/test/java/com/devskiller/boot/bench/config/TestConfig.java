package com.devskiller.boot.bench.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

@Configuration
public class TestConfig {

    @Hessian
    @Bean
    public HessianProxyFactoryBean hessianClient() {
        return ProxyFactoryBeanHelper.hessianProxyFactoryBean(8080);
    }

    @HttpInvoker
    @Bean
    public HttpInvokerProxyFactoryBean httpInvokerClient() {
        return ProxyFactoryBeanHelper.httpInvokerProxyFactoryBean(8080);
    }

}
