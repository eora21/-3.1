package com.example.ioc_di.container;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.ioc_di.container.pojo.Hello;
import com.example.ioc_di.container.pojo.StringPrinter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

class ContainerTest {
    @Test
    void containerTest() {
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello", Hello.class);

        Hello hello = context.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        RootBeanDefinition beanDefinition = new RootBeanDefinition(Hello.class);
        beanDefinition.getPropertyValues().addPropertyValue("name", "Spring");  // 빈의 name 프로퍼티에 들어갈 값을 지정
        context.registerBeanDefinition("hello2", beanDefinition);

        Hello hello2 = context.getBean("hello2", Hello.class);
        assertThat(hello2).isNotNull();

        assertThat(context.getBeanFactory().getBeanDefinitionCount()).isEqualTo(2);
    }

    @Test
    void registerBeanWithDependency() {
        StaticApplicationContext context = new StaticApplicationContext();

        context.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        RootBeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        context.registerBeanDefinition("hello", helloDef);

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring");
    }
}