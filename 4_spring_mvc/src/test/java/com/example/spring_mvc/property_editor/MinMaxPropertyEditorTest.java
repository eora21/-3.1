package com.example.spring_mvc.property_editor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinMaxPropertyEditorTest {
    MinMaxPropertyEditor minMaxPropertyEditor = new MinMaxPropertyEditor(0, 200);

    @Test
    void minMaxPropertyEditor() {
        minMaxPropertyEditor.setAsText("100");
        assertThat(minMaxPropertyEditor.getValue()).isEqualTo(100);
        minMaxPropertyEditor.setAsText("-1");
        assertThat(minMaxPropertyEditor.getValue()).isEqualTo(0);
        minMaxPropertyEditor.setAsText("201");
        assertThat(minMaxPropertyEditor.getValue()).isEqualTo(200);

        minMaxPropertyEditor.setValue(-1);
        assertThat(minMaxPropertyEditor.getValue()).isEqualTo(0);
        minMaxPropertyEditor.setValue(201);
        assertThat(minMaxPropertyEditor.getValue()).isEqualTo(200);
    }
}