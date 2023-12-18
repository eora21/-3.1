package com.example.ioc_di.container.pojo;

public class StringPrinter implements Printer {
    private final StringBuffer stringBuffer = new StringBuffer();
    @Override
    public void print(String message) {
        stringBuffer.append(message);
    }
}
