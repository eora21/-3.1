package com.example.spring_mvc.converter;

import com.example.spring_mvc.level.Level;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLevelConverter implements Converter<String, Level> {
    @Override
    public Level convert(String text) {
        return Level.valueOf(Integer.parseInt(text));
    }
}
