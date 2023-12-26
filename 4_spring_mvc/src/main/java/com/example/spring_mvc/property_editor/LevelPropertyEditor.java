package com.example.spring_mvc.property_editor;

import com.example.spring_mvc.level.Level;
import java.beans.PropertyEditorSupport;

public class LevelPropertyEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        return String.valueOf(((Level)this.getValue()).getValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
    }
}
