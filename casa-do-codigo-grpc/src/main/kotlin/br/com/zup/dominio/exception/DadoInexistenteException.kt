package br.com.zup.dominio.exception

open abstract class DadoInexistenteException(message: String?): Exception(message)

class LivroInexistenteException(message: String?): DadoInexistenteException(message)