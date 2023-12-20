package com.example.ioc_di.scope;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    @Component("prototypeBean")
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

    static class ObjectFactoryConfig {
        @Bean
        public ObjectFactoryCreatingFactoryBean prototypeBeanFactory() {
            ObjectFactoryCreatingFactoryBean factoryBean = new ObjectFactoryCreatingFactoryBean();
            factoryBean.setTargetBeanName("prototypeBean");
            return factoryBean;
        }
    }

    @Test
    void prototypeScopeWithObjectFactory() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, ObjectFactoryConfig.class);
        ObjectFactory<?> prototypeBeanFactory = context.getBean("prototypeBeanFactory", ObjectFactory.class);

        PrototypeBean prototypeBean1 = (PrototypeBean) prototypeBeanFactory.getObject();
        PrototypeBean prototypeBean2 = (PrototypeBean) prototypeBeanFactory.getObject();

        assertThat(prototypeBean1).isNotEqualTo(prototypeBean2);
    }

    interface PrototypeBeanFactory {
        PrototypeBean newInstance();  // 메서드명은 어떻게 지어도 상관 없음
    }

    static class ServiceLocatorFactoryBeanConfig {
        @Bean
        public ServiceLocatorFactoryBean prototypeBeanFactory() {
            ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
            serviceLocatorFactoryBean.setServiceLocatorInterface(PrototypeBeanFactory.class);
            return serviceLocatorFactoryBean;
        }
    }

    @Test
    void prototypeScopeWithServiceLocatorFactoryBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, ServiceLocatorFactoryBeanConfig.class);
        PrototypeBeanFactory prototypeBeanFactory = context.getBean("prototypeBeanFactory", PrototypeBeanFactory.class);

        PrototypeBean prototypeBean1 = prototypeBeanFactory.newInstance();
        PrototypeBean prototypeBean2 = prototypeBeanFactory.newInstance();

        assertThat(prototypeBean1).isNotEqualTo(prototypeBean2);
    }
}
