package com.devskiller.boot.bench;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import com.devskiller.boot.bench.api.FooService;
import com.devskiller.boot.bench.config.Hessian;
import com.devskiller.boot.bench.config.HttpInvoker;
import com.devskiller.boot.bench.config.TestConfig;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

@IntegrationTest({"management.port=0"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestConfig.class})
public class TransportBenchmark extends AbstractBenchmark {

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Autowired
    private FooService fooService;

    @Autowired
    @Hessian
    private FooService hessianClient;

    @Autowired
    @HttpInvoker
    private FooService httpInvokerClient;

    private static final int COUNT = 500;
    private static RestTemplate restTemplate = new RestTemplate();
    private FooRequest fooRequest = new FooRequest("foo", 1, BigDecimal.ZERO);
    private final HttpHeaders headers = new HttpHeaders();

    @After
    public void shutdown(){
        System.out.println(fooService.mapSize());
    }

    @Test
    public void HTTP_Invoker() {

        for (int i = 0; i < COUNT; i++) {
            String id = httpInvokerClient.create(fooRequest);
            httpInvokerClient.get(id);
            httpInvokerClient.delete(id);
        }
    }

    @Test
    public void Hessian() {

        for (int i = 0; i < COUNT; i++) {
            String id = hessianClient.create(fooRequest);
            hessianClient.get(id);
            hessianClient.delete(id);
        }
    }

    @Test
    public void Internal() {

        for (int i = 0; i < COUNT; i++) {
            String id = fooService.create(fooRequest);
            fooService.get(id);
            fooService.delete(id);
        }
    }

    @Test
    public void REST_JSON() {

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (int i = 0; i < COUNT; i++) {
            runCreateGetAndDelete();
        }

    }

    @Test
    public void REST_XML() throws Exception {

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        headers.setContentType(MediaType.APPLICATION_XML);

        for (int i = 0; i < COUNT; i++) {
            runCreateGetAndDelete();
        }
    }

    private void runCreateGetAndDelete() {
        String id = restTemplate.exchange("http://localhost:8080/foo", HttpMethod.POST, new HttpEntity<>(new FooRequest(), headers), String.class).getBody();
        restTemplate.exchange("http://localhost:8080/foo/{id}", HttpMethod.GET, new HttpEntity(headers), Foo.class, id).getBody();
        restTemplate.exchange("http://localhost:8080/foo/{id}", HttpMethod.DELETE, new HttpEntity<FooRequest>(headers), Void.class, id);
    }


}

