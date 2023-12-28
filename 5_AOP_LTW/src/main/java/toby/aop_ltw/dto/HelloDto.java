package toby.aop_ltw.dto;

import toby.aop_ltw.controller.HelloAnnotation;

@HelloAnnotation
public record HelloDto(String name) {
}
