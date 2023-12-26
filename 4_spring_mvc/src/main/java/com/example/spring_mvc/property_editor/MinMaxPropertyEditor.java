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
        setValue(value);
    }

    @Override
    public void setValue(Object value) {
        int intValue = (int) value;

        if (intValue < min) {
            intValue = min;
        }

        if (max < intValue) {
            intValue = max;
        }

        super.setValue(intValue);
    }
}
