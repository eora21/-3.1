package com.example.ioc_di.container;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.ioc_di.container.pojo.Hello;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;

class ContainerTest {
    @Test
    void containerTest() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello", Hello.class);

        Hello hello = context.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();
    }
}