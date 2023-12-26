package com.example.spring_mvc.controller;

import com.example.spring_mvc.level.Level;
import com.example.spring_mvc.member.Member;
import com.example.spring_mvc.property_editor.LevelPropertyEditor;
import com.example.spring_mvc.property_editor.MinMaxPropertyEditor;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConvertController {
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0, 200));
    }

    @GetMapping("/search")
    public String search(@RequestParam("level") Level level) {
        return level.name();
    }

    @GetMapping("/member")
    public Member user(@ModelAttribute Member member) {
        return member;
    }
}
