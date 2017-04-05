package br.univel.jshare.comum;

public enum TipoFiltro {

	NOME("NOME"), TAMANHO_MIN("TAMANO M�nimo"), TAMANHO_MAX("Tamanho M�ximo"), EXTENSAO("Extens�o");

	private String descricao;

	private TipoFiltro(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
