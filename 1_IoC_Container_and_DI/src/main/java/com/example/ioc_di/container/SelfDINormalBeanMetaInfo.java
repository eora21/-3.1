package com.example.ioc_di.container;

import com.example.ioc_di.container.pojo.ConsolePrinter;
import com.example.ioc_di.container.pojo.Hello;
import com.example.ioc_di.container.pojo.Printer;
import org.springframework.context.annotation.Bean;

public class SelfDINormalBeanMetaInfo {
    private Printer printer;

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    // 외부에서 메서드를 동작시킬 경우 새 객체가 생성되므로 모두 private로 변경

    @Bean
    private Printer printer() {
        return new ConsolePrinter();
    }

    @Bean
    private Hello hello(Printer printer) {
        Hello hello = new Hello();
        hello.setPrinter(printer);
        return hello;
    }

    @Bean
    private Hello hello2(Printer printer) {
        Hello hello = new Hello();
        hello.setPrinter(printer);
        return hello;
    }
}
