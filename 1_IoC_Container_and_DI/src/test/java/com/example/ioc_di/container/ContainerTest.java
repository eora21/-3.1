package com.example.ioc_di.container;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.ioc_di.bean.AnnotatedHello;
import com.example.ioc_di.container.pojo.Hello;
import com.example.ioc_di.container.pojo.Printer;
import com.example.ioc_di.container.pojo.StringPrinter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Test
    void genericApplicationContext() {
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions("definition/beanDefinition.xml");  // 클래스패스 리소스로 인식

        context.refresh();

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void genericXmlApplicationContext() {
        GenericApplicationContext context = new GenericXmlApplicationContext("definition/beanDefinition.xml");

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void contextHierarchy() {
        GenericXmlApplicationContext parentContext = new GenericXmlApplicationContext("hierarchy/parentContext.xml");
        GenericApplicationContext childContext = new GenericApplicationContext(parentContext);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(childContext);
        reader.loadBeanDefinitions("hierarchy/childContext.xml");
        childContext.refresh();

        Printer printer = childContext.getBean("printer", Printer.class);
        assertThat(printer).isNotNull();

        Hello hello = childContext.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        hello.print();
        assertThat(printer.toString()).isEqualTo("Hello Child");
    }

    @Test
    void simpleBeanScanning() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example.ioc_di.bean");
        AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello).isNotNull();
    }

    @Test
    void classScanning() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
        AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello).isNotNull();

        AnnotatedHelloConfig config = context.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
        assertThat(config).isNotNull();

        assertThat(config.annotatedHello()).isEqualTo(hello);
    }

    @Test
    void normalBeanMetaInfoClass() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(NormalBeanMetaInfo.class);
        NormalBeanMetaInfo normalBeanMetaInfo = context.getBean("normalBeanMetaInfo", NormalBeanMetaInfo.class);
        assertThat(normalBeanMetaInfo).isNotNull();  // 일반 클래스여도 빈으로 등록된다!

        Hello hello = context.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        Hello hello2 = context.getBean("hello2", Hello.class);
        assertThat(hello2).isNotNull();

        Object printer = ReflectionTestUtils.getField(hello, "printer");
        Object printer2 = ReflectionTestUtils.getField(hello2, "printer");

        System.out.println(printer);
        System.out.println(printer2);
        assertThat(printer).isNotEqualTo(printer2);
    }

    @Test
    void selfDINormalBeanMetaInfoClass() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SelfDINormalBeanMetaInfo.class);
        SelfDINormalBeanMetaInfo selfDINormalBeanMetaInfo = context.getBean("selfDINormalBeanMetaInfo", SelfDINormalBeanMetaInfo.class);
        assertThat(selfDINormalBeanMetaInfo).isNotNull();

        Hello hello = context.getBean("hello", Hello.class);
        assertThat(hello).isNotNull();

        Hello hello2 = context.getBean("hello2", Hello.class);
        assertThat(hello2).isNotNull();

        Object printer = ReflectionTestUtils.getField(hello, "printer");
        Object printer2 = ReflectionTestUtils.getField(hello2, "printer");

        System.out.println(printer);
        System.out.println(printer2);
        assertThat(printer).isEqualTo(printer2);
    }
}