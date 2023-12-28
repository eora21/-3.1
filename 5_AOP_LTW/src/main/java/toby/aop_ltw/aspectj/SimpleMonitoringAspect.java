package toby.aop_ltw.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SimpleMonitoringAspect {
    @Pointcut("within(toby.aop_ltw.controller..*)")
    private void controllerLayer() {
    }

    @Pointcut("this(toby.aop_ltw.controller.HelloController)")
    private void CGLibProxy() {
    }

    @Around("CGLibProxy()")
    public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("before");
        Object ret = pjp.proceed();
        System.out.println("after");
        return ret;
    }
}
