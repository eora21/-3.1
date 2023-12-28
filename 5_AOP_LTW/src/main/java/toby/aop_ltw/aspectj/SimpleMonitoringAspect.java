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

    @Pointcut("this(toby.aop_ltw.controller.HelloController) && args()")
    private void CGLibProxy() {
    }

    @Pointcut("execution(* toby..* (..)) && @target(org.springframework.web.bind.annotation.RestController)")
    public void controllerAnnotation() {
    }

    @Pointcut("execution(* toby..* (..)) && @target(toby.aop_ltw.controller.HelloAnnotation)")
    public void helloAnnotation() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerAnnotationExactly() {
    }

    @Around("controllerAnnotationExactly()")
    public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("before");
        Object ret = pjp.proceed();
        System.out.println("after");
        return ret;
    }
}
