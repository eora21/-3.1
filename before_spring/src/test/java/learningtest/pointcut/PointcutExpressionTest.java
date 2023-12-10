package learningtest.pointcut;

import org.junit.jupiter.api.Test;

class PointcutExpressionTest {
    @Test
    void showMethodFullSignature() throws NoSuchMethodException {
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }
}