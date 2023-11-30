package learningtest.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import learningteset.template.Calculator;
import org.junit.jupiter.api.Test;

public class CalcSumTest {
    @Test
    void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        URL resource = getClass().getResource("/numbers.txt");
        String path = URLDecoder.decode(Objects.requireNonNull(resource).getPath(), StandardCharsets.UTF_8);
        int calcSum = calculator.calcSum(path);
        assertThat(calcSum).isEqualTo(10);
    }
}
