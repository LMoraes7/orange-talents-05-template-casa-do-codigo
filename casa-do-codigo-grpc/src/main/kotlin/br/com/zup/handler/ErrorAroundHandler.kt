package br.com.zup.handler

import io.micronaut.aop.Around
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Target(CLASS)
@Retention(RUNTIME)
@Around
annotation class ErrorAroundHandler
