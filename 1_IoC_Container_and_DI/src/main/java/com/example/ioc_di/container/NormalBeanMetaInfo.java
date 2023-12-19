package com.example.ioc_di.container;

import com.example.ioc_di.container.pojo.ConsolePrinter;
import com.example.ioc_di.container.pojo.Hello;
import com.example.ioc_di.container.pojo.Printer;
import com.example.ioc_di.container.pojo.StringPrinter;
import org.springframework.context.annotation.Bean;

public class NormalBeanMetaInfo {
    @Bean
    public Printer printer() {
        return new ConsolePrinter();
    }

    @Bean
    public Hello hello() {
        Hello hello = new Hello();
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setPrinter(printer());
        return hello;
    }
}
