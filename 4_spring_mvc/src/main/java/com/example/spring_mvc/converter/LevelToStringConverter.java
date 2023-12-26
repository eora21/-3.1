package com.example.spring_mvc.converter;

import com.example.spring_mvc.level.Level;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//@Component
public class LevelToStringConverter implements Converter<Level, String> {
    @Override
    public String convert(Level level) {
        return String.valueOf(level.getValue());
    }
}
