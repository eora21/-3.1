package com.example.spring_mvc.controller;

import com.example.spring_mvc.level.Level;
import com.example.spring_mvc.property_editor.LevelPropertyEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConvertController {
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
    }

    @GetMapping("/search")
    public String search(@RequestParam("level") Level level) {
        return level.name();
    }
}
