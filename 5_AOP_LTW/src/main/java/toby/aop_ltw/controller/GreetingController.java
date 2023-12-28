package toby.aop_ltw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import toby.aop_ltw.dto.HelloDto;

@RestController
public class GreetingController {
    @GetMapping
    public String greeting(@ModelAttribute HelloDto helloDto) {
        return "Hi";
    }
}
