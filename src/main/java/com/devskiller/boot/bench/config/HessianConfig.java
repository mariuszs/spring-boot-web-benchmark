package com.devskiller.boot.bench.config;

import com.devskiller.boot.bench.api.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

@Configuration
class HessianConfig {

    @Autowired
    private FooService fooService;

    @Bean(name = "/fooService")
    public HessianServiceExporter fooServiceHessian() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(fooService);
        exporter.setServiceInterface(FooService.class);
        return exporter;
    }

}
