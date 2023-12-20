package com.example.ioc_di.scope;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class ScopeTest {
    static class SingletonBean {
    }

    static class SingletonClientBean {
        @Autowired
        SingletonBean bean1;
        @Autowired
        SingletonBean bean2;
    }

    @Test
    void singletonScope() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
        Set<SingletonBean> beans = new HashSet<>();

        beans.add(context.getBean(SingletonClientBean.class).bean1);
        beans.add(context.getBean(SingletonClientBean.class).bean2);

        assertThat(beans.size()).isOne();
    }

    @Scope("prototype")
    static class PrototypeBean {
    }

    static class PrototypeClientBean {
        @Autowired
        PrototypeBean bean1;
        @Autowired
        PrototypeBean bean2;
    }

    @Test
    void prototypeScope() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
        Set<PrototypeBean> beans = new HashSet<>();

        beans.add(context.getBean(PrototypeBean.class));
        assertThat(beans.size()).isOne();

        beans.add(context.getBean(PrototypeBean.class));
        assertThat(beans.size()).isEqualTo(2);

        beans.add(context.getBean(PrototypeClientBean.class).bean1);
        assertThat(beans.size()).isEqualTo(3);

        beans.add(context.getBean(PrototypeClientBean.class).bean2);
        assertThat(beans.size()).isEqualTo(4);
    }
}
