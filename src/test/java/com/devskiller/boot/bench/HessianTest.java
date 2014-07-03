package com.devskiller.boot.bench;

import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import com.devskiller.boot.bench.api.FooService;
import com.devskiller.boot.bench.config.ProxyFactoryBeanHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HessianTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private FooService fooClient;

    @Bean
    public HessianProxyFactoryBean hessianProxyFactoryBean() {
        return ProxyFactoryBeanHelper.hessianProxyFactoryBean(port);
    }

    @Test
    public void shouldReceiveFoo() {
        //given
        FooRequest fooRequest = new FooRequest("foo", 0, BigDecimal.ZERO);

        //when
        String id = fooClient.create(fooRequest);

        then(id).isNotEmpty();

        Foo foo = fooClient.get(id);

        then(foo).isNotNull();

        fooClient.delete(id);
    }
}
