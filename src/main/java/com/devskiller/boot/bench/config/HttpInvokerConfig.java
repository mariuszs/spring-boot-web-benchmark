package com.devskiller.boot.bench.config;

import com.devskiller.boot.bench.api.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@Configuration
class HttpInvokerConfig {

    @Autowired
    private FooService fooService;

    @Bean(name = "/fooHttpInvokerService")
    public HttpInvokerServiceExporter fooServiceHttpInvoker() {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(fooService);
        exporter.setServiceInterface(FooService.class);
        return exporter;
    }

}
