package com.devskiller.boot.bench;

import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import com.devskiller.boot.bench.api.FooService;
import com.devskiller.boot.bench.config.HttpInvoker;
import com.devskiller.boot.bench.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;

@IntegrationTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestConfig.class})
public class HttpInvokerTest {

    @Autowired
    @HttpInvoker
    private FooService fooClient;

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
