package learningtest.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class CalcSumTest {
    Calculator calculator = new Calculator();
    URL resource = getClass().getResource("/numbers.txt");
    String path = URLDecoder.decode(Objects.requireNonNull(resource).getPath(), StandardCharsets.UTF_8);

    @Test
    void sumOfNumbers() throws IOException {
        assertThat(calculator.calcSum(path)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(path)).isEqualTo(24);
    }

    @Test
    void concatenateStrings() throws IOException {
        assertThat(calculator.concatenate(path)).isEqualTo("1234");
    }
}
