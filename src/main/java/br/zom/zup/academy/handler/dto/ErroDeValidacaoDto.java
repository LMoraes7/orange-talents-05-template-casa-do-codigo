package br.zom.zup.academy.handler.dto;

public class ErroDeValidacaoDto {

	private String campo;
	private String mensagem;

	public ErroDeValidacaoDto(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}

	public String getCampo() {
		return campo;
	}

	public String getMensagem() {
		return mensagem;
	}
}