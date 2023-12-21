package com.example.ioc_di.property_source;

import java.util.Map;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class MyContextInitializer implements ApplicationContextInitializer<AnnotationConfigApplicationContext> {
    @Override
    public void initialize(AnnotationConfigApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> propertySource = Map.of("db.username", "spring");

        environment.getPropertySources().addFirst(new MapPropertySource("myPropertySource", propertySource));
    }
}
