package com.devskiller.boot.bench.config;

import com.devskiller.boot.bench.api.FooService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
public class TestConfig {

    @Bean
    public HessianProxyFactoryBean fooClient() {
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        factory.setServiceUrl("http://localhost:8080/fooService");
        factory.setServiceInterface(FooService.class);
        return factory;
    }
}
