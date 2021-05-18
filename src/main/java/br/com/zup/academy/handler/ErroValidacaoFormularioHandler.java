package br.com.zup.academy.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.zup.academy.handler.dto.ErroDeValidacaoDto;

@RestControllerAdvice
public class ErroValidacaoFormularioHandler {

	private MessageSource messageSource;

	public ErroValidacaoFormularioHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeValidacaoDto> validacaoFormHandler(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ErroDeValidacaoDto> errosDto = new ArrayList<ErroDeValidacaoDto>();
		fieldErrors.forEach(erro -> {
			errosDto.add(new ErroDeValidacaoDto(erro.getField(), getMensagem(erro)));
		});
		return errosDto;
	}

	private String getMensagem(FieldError erro) {
		return this.messageSource.getMessage(erro, LocaleContextHolder.getLocale());
	}
}