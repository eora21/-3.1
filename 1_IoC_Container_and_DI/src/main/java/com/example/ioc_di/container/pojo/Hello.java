package com.example.ioc_di.container.pojo;

import lombok.Setter;

@Setter
public class Hello {
    private String name;
    private Printer printer;

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        printer.print(sayHello());
    }
}
