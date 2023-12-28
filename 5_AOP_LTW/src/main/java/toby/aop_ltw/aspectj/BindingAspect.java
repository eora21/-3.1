package toby.aop_ltw.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import toby.aop_ltw.annotation.HelloAnnotation;

@Aspect
@Component
public class BindingAspect {
    @Before("@within(helloAnnotation)")
    public void before(HelloAnnotation helloAnnotation) {
        System.out.println("before");
    }
}
