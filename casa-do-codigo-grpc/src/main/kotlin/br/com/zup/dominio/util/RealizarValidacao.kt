package br.com.zup.dominio.util

import br.com.zup.endpoint.cadastrar.request.RequestDto
import javax.validation.ConstraintViolationException
import javax.validation.Validator

class RealizarValidacao {

    companion object {
        fun validar(
            validator: Validator,
            dto: RequestDto,
        ) {
            val errors = validator.validate(dto)
            if (errors.isNotEmpty())
                throw ConstraintViolationException(errors)
        }
    }
}