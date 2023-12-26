package com.example.spring_mvc.property_editor;

import java.beans.PropertyEditorSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MinMaxPropertyEditor extends PropertyEditorSupport {
    private final int min;
    private final int max;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        int value = Integer.parseInt(text);

        if (value < min) {
            value = min;
        }

        if (max < value) {
            value = max;
        }

        setValue(value);
    }
}
