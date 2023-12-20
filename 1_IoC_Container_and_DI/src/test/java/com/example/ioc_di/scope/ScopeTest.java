package com.example.ioc_di.scope;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
}
