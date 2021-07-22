package br.com.zup.edu.endpoint.handler

import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import java.lang.Exception
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorAroundHandler::class)
class HandlerInterceptor : MethodInterceptor<Any, Any> {

    private val logger = LoggerFactory.getLogger(this::class.java)

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

                else -> {
                    this.logger.error(ex.stackTraceToString())
                    this.logger.error(ex.message)
                    Status.UNKNOWN
                        .withCause(ex)
                        .withDescription(ex.message)
                }
            }

            responseObserver.onError(status.asRuntimeException())

            return null
        }
    }
}