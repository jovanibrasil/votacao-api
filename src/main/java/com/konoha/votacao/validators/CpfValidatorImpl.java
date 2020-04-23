package com.konoha.votacao.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.Validator;

public class CpfValidatorImpl implements ConstraintValidator<Cpf, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			Validator<String> validator = new CPFValidator();
			validator.assertValid(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
