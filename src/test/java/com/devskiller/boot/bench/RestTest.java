package com.devskiller.boot.bench;

import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class RestTest {

    private String url;
    private RestTemplate restTemplate;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        url = "http://localhost:" + port + "/foo";
    }

    @Test
    public void shouldProcessFoo() {
        //given
        FooRequest fooRequest = new FooRequest("foo", 1, BigDecimal.ZERO);

        String id = restTemplate.postForObject(url,
            fooRequest,
            String.class);

        then(id).isNotEmpty();

        Foo foo = restTemplate.getForObject(url + "/{id}", Foo.class, id);

        then(foo).isNotNull();

        restTemplate.delete(url + "/{id}", id);

    }


}
