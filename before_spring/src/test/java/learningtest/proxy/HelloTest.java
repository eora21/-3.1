package learningtest.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;
import java.util.Objects;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class HelloTest {
    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        String toby = "Toby";
        assertThat(hello.sayHello(toby)).isEqualTo("Hello Toby");
        assertThat(hello.sayHi(toby)).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou(toby)).isEqualTo("Thank You Toby");
    }

    @Test
    void uppercaseProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));
        String toby = "Toby";
        assertThat(proxiedHello.sayHello(toby)).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi(toby)).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou(toby)).isEqualTo("THANK YOU TOBY");
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        assert Objects.nonNull(proxiedHello);

        String toby = "Toby";
        assertThat(proxiedHello.sayHello(toby)).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi(toby)).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou(toby)).isEqualTo("THANK YOU TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return ((String) Objects.requireNonNull(invocation.proceed())).toUpperCase();
        }
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        assert Objects.nonNull(proxiedHello);

        String toby = "Toby";
        assertThat(proxiedHello.sayHello(toby)).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi(toby)).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou(toby)).isEqualTo("Thank You Toby");
    }
}
