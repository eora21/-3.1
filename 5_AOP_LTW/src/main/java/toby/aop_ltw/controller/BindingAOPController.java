package toby.aop_ltw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import toby.aop_ltw.annotation.HelloAnnotation;

@RestController
@HelloAnnotation
public class BindingAOPController {
    @GetMapping("/binding")
    public String binding() {
        return "binding";
    }
}
