package toby.aop_ltw.dto;

import toby.aop_ltw.annotation.HelloAnnotation;

@HelloAnnotation
public record HelloDto(String name) {
}
