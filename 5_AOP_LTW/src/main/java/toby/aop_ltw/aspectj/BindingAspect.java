package toby.aop_ltw.aspectj;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import toby.aop_ltw.annotation.HelloAnnotation;

@Aspect
@Component
public class BindingAspect {
    @Before("@within(helloAnnotation)")
    public void before(HelloAnnotation helloAnnotation) {
        System.out.println("before");
    }

    @Pointcut("@within(helloAnnotation)")
    private void withinHelloAnnotation(HelloAnnotation helloAnnotation) {
    }

    @After(value = "withinHelloAnnotation(helloAnnotation)", argNames = "helloAnnotation")
    public void after(HelloAnnotation helloAnnotation) {
        System.out.println("after");
    }
}
