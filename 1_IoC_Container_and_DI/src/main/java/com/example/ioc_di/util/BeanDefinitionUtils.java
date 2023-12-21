package com.example.ioc_di.util;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.support.GenericApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanDefinitionUtils {
    public static void printBeanDefinitions(GenericApplicationContext context) {
        List<List<String>> roleBeanInfos = new ArrayList<>();
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());

        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            int role = context.getBeanDefinition(beanDefinitionName).getRole();
            List<String> beanInfos = roleBeanInfos.get(role);
            beanInfos.add(role + "\t" + beanDefinitionName + "\t" +
                    context.getBean(beanDefinitionName).getClass().getName());
        }

        for (List<String> roleBeanInfo : roleBeanInfos) {
            roleBeanInfo.forEach(System.out::println);
        }
    }
}
