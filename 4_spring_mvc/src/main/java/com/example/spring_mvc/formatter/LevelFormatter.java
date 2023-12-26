package com.example.spring_mvc.formatter;

import com.example.spring_mvc.level.Level;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class LevelFormatter implements Formatter<Level> {
    @Override
    public Level parse(String text, Locale locale) throws ParseException {
        return Level.valueOf(Integer.parseInt(text));
    }

    @Override
    public String print(Level level, Locale locale) {
        return String.valueOf(level.getValue());
    }
}
