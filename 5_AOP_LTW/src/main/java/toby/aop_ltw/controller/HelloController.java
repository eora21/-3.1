package toby.aop_ltw.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController extends SuperController implements Hello {
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("greeting", "hello");
    }
}
