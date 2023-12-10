package learningtest.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;
import java.util.Objects;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
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

    @Test
    void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdvice(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {}
        checkAdvice(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {}
        checkAdvice(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean advice) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        assert Objects.nonNull(proxiedHello);
        String toby = "Toby";

        if (advice) {
            assertThat(proxiedHello.sayHello(toby)).isEqualTo("HELLO TOBY");
            assertThat(proxiedHello.sayHi(toby)).isEqualTo("HI TOBY");
            assertThat(proxiedHello.sayThankYou(toby)).isEqualTo("Thank You Toby");
            return;
        }

        assertThat(proxiedHello.sayHello(toby)).isEqualTo("Hello Toby");
        assertThat(proxiedHello.sayHi(toby)).isEqualTo("Hi Toby");
        assertThat(proxiedHello.sayThankYou(toby)).isEqualTo("Thank You Toby");
    }
}
