package com.konoha.votacao.exceptions;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	private final MessageSource messageSource;
	
	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioForm> handle(MethodArgumentNotValidException exception) {
		Locale locale = LocaleContextHolder.getLocale();
		return exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(e -> 
				new ErroDeFormularioForm(e.getField(), messageSource.getMessage(e, locale))
			).collect(Collectors.toList());
	}
	
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public List<String> handle(NotFoundException exception) {
		String message = exception.getMessage();
		log.info(message);
		return Arrays.asList(message);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(VotoException.class)
	public List<String> handle(VotoException exception) {
		String message = exception.getMessage();
		log.info(message);
		return Arrays.asList(message);
	}
	
}
