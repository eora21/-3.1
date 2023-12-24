package toby.web_mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EasyHelloControllerTest {
    @Test
    void easyHelloController() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Spring");
        Map<String, Object> model = new HashMap<>();

        new EasyHelloController().control(params, model);
        assertThat(model.get("message")).isEqualTo("Hello Spring");
    }
}