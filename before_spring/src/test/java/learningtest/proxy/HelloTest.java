package learningtest.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class HelloTest {
    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        String toby = "Toby";
        assertThat(hello.sayHello(toby)).isEqualTo("Hello Toby");
        assertThat(hello.sayHi(toby)).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou(toby)).isEqualTo("Thank You Toby");
    }
}
