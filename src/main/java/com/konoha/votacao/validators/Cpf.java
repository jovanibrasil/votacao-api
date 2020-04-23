package com.konoha.votacao.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD } )
@Constraint(validatedBy = CpfValidatorImpl.class)
public @interface Cpf {
	String message() default "Cpf inválido.";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
