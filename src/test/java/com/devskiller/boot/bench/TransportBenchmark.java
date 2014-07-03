package com.devskiller.boot.bench;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import com.devskiller.boot.bench.api.FooService;
import com.devskiller.boot.bench.config.Local;
import com.devskiller.boot.bench.config.ProxyFactoryBeanHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class TransportBenchmark extends AbstractBenchmark {

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Value("${local.server.port}")
    private int port;

    @Autowired
    @Local
    private FooService fooService;

    @Autowired
    private FooService httpInvokerClient;

    @Autowired
    private FooService hessianClient;

    private static final int COUNT = 100;
    private static RestTemplate restTemplate = new RestTemplate();
    private FooRequest fooRequest = new FooRequest("foo", 1, BigDecimal.ZERO);
    private final HttpHeaders headers = new HttpHeaders();

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
        String url = String.format("http://localhost:%s/foo", port);
        String id = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new FooRequest(), headers), String.class).getBody();
        restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity(headers), Foo.class, id).getBody();
        restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<FooRequest>(headers), Void.class, id);
    }

    @Bean
    public HttpInvokerProxyFactoryBean hessianClient() {
        return ProxyFactoryBeanHelper.httpInvokerProxyFactoryBean(port);
    }

    @Bean
    public HessianProxyFactoryBean httpInvokerClient() {
        return ProxyFactoryBeanHelper.hessianProxyFactoryBean(port);
    }

}

