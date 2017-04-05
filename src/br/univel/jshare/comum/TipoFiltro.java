package br.univel.jshare.comum;

public enum TipoFiltro {

	NOME("NOME"), TAMANHO_MIN("TAMANO Mínimo"), TAMANHO_MAX("Tamanho Máximo"), EXTENSAO("Extensão");

	private String descricao;

	private TipoFiltro(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
