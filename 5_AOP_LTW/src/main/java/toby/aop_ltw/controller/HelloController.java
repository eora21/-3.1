package toby.aop_ltw.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import toby.aop_ltw.annotation.MethodAnnotation;
import toby.aop_ltw.dto.HelloDto;

@RestController
public class HelloController extends SuperController implements Hello {
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("greeting", "hello");
    }

    @GetMapping("/hello/dto")
    @MethodAnnotation
    public HelloDto helloWithDto(@ModelAttribute HelloDto helloDto) {
        return helloDto;
    }
}
