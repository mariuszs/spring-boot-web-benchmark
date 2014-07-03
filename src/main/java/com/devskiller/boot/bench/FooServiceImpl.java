package com.devskiller.boot.bench;

import com.devskiller.boot.bench.api.Bar;
import com.devskiller.boot.bench.api.Foo;
import com.devskiller.boot.bench.api.FooRequest;
import com.devskiller.boot.bench.api.FooService;
import com.devskiller.boot.bench.config.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController("fooService")
@RequestMapping("/foo")
@Local
public class FooServiceImpl implements FooService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FooServiceImpl.class);

    private final Map<String, Foo> fooMap = new HashMap<>(10000);

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody FooRequest fooRequest) {
        LOGGER.debug("IN {}", fooRequest);
        Foo foo = new Foo(fooRequest.getI(), fooRequest.getName(), new Bar(BigDecimal.ONE, Arrays.asList(1L, 2L, 3L)));
        String id = UUID.randomUUID().toString();
        fooMap.put(id, foo);
        return id;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Foo get(@PathVariable String id) {

        checkIfExist(id);

        return fooMap.get(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {

        checkIfExist(id);

        fooMap.remove(id);

    }

    private void checkIfExist(String id) {
        if (!fooMap.containsKey(id))
            throw new IllegalArgumentException("Not existing id " + id);
    }

    @Override
    public int mapSize(){
        return fooMap.size();
    }
}
