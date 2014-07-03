package com.devskiller.boot.bench.api;

public interface FooService {

    String create(FooRequest fooRequest);

    Foo get(String id);

    void delete(String id);

    int mapSize();
}
