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
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.konoha.votacao.response.Response;

@RestControllerAdvice
public class RestExceptionHandler {

	private final MessageSource messageSource;
	
	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response<List<ErroDeFormularioForm>> handle(MethodArgumentNotValidException exception) {
		Locale locale = LocaleContextHolder.getLocale();
		List<ErroDeFormularioForm> erros = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(e -> 
				new ErroDeFormularioForm(e.getField(), messageSource.getMessage(e, locale))
			).collect(Collectors.toList());
		Response<List<ErroDeFormularioForm>> response = new Response<>();
		response.setErrors(erros);
		return response;
	}
	
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public Response<List<String>> handle(NotFoundException exception) {
		Response<List<String>> response = new Response<>();
		response.setErrors(Arrays.asList(exception.getMessage()));
		return response;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(VotoException.class)
	public Response<List<String>> handle(VotoException exception) {
		Response<List<String>> response = new Response<>();
		response.setErrors(Arrays.asList(exception.getMessage()));
		return response;
	}
	
}
