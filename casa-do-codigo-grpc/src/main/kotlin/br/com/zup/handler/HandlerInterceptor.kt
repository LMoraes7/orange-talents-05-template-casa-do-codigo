package br.com.zup.handler

import br.com.zup.dominio.exception.DadoInexistenteException
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorAroundHandler::class)
class HandlerInterceptor : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        try {
            return context.proceed()
        } catch (ex: Exception) {
            val responseObserver = context.parameterValues[1] as StreamObserver<*>

            val status = when (ex) {
                is ConstraintViolationException ->
                    Status.INVALID_ARGUMENT
                        .withCause(ex)
                        .withDescription(ex.message)

                is DadoInexistenteException ->
                    Status.NOT_FOUND
                        .withCause(ex)
                        .withDescription(ex.message)

                else ->
                    Status.UNKNOWN
                        .withCause(ex)
                        .withDescription(ex.message)
            }

            responseObserver.onError(status.asRuntimeException())
            return null
        }
    }
}