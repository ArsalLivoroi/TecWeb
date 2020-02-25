package generatore.problema;

import generatore.global.Utils;

public class Attributo {
	
	private String tipo;
	private String nome;
	private String tipoDB;
	private String nomeColumn;
	
		

	
	public Attributo(String tipo, String nome) {
		nome=Utils.capitalizeWord(nome);
		this.tipo = tipo;
		this.nome = Utils.uncapFirst(nome).replaceAll("\\s", "");
		this.nomeColumn = nome.replaceAll("\\s", "_").toLowerCase();
		tipoDB=mapTipo(tipo);
	}
	
	private static String mapTipo(String tipo) {
		String tipoDB = "";
		switch (tipo.toUpperCase()) {
		case "STRING": tipoDB="VARCHAR(100)"; break;
		case "LONG": tipoDB="VARCHAR(100)"; break;
		case "FLOAT": tipoDB="REAL"; break;
		default: tipoDB=tipo.toUpperCase();
		}
		return tipoDB;
	}



	public String getTipoDB() {
		return tipoDB;
	}
	public String getTipo() {
		return tipo;
	}
	public String getNome() {
		return nome;
	}
	public String getNomeColumn() {
		return nomeColumn;
	}
	
	public String toString() {
		return this.nome + ":" +this.tipo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attributo other = (Attributo) obj;
		if (nome == other.nome)
			return true;
		return false;
	}
/*
	public String getNomeUp() {
		// TODO Auto-generated method stub
		return this.getNome().substring(0, 1).toUpperCase() + this.getNome().substring(1);
	}
	public String getTipoUp() {
		// TODO Auto-generated method stub
		return this.getTipo().substring(0, 1).toUpperCase() + this.getTipo().substring(1);
	}
	*/

	public String getNomeUp() {
		// TODO Auto-generated method stub
		return Utils.capFirst(nome);
	}

	public String getTipoUp() {
		// TODO Auto-generated method stub
		return Utils.capFirst(tipo);
	}
}

