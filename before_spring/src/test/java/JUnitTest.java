import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class JUnitTest {
    static Set<JUnitTest> testObjects = new HashSet<>();

    @Test
    void test1() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);
    }

    @Test
    void test2() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);
    }

    @Test
    void test3() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);
    }
}
