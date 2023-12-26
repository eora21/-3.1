package com.example.spring_mvc.level;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Level {
    BRONZE(1),
    SILVER(2),
    GOLD(3);

    private final int value;

    public static Level valueOf(int value) {
        return Arrays.stream(Level.values())
                .filter(level -> level.getValue() == value)
                .findAny()
                .orElseThrow(() -> new AssertionError("일치하는 id가 없습니다."));
    }
}
