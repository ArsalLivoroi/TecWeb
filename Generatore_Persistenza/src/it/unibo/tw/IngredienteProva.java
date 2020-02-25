package it.unibo.tw;

import java.io.Serializable;
import java.util.*;

public class IngredienteProva implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idIngredienteProva;
	private String nomeIngrediente;
	private int quantita;


	private Set<PiattoProva> piattiProva; 
	
	// --- costruttore ----------

	public IngredienteProva(){
		this.piattiProva = new HashSet<PiattoProva>();
	}

	public IngredienteProva(int idIngredienteProva, String nomeIngrediente, int quantita){
		this();
		this.idIngredienteProva = idIngredienteProva;
		this.nomeIngrediente = nomeIngrediente;
		this.quantita = quantita;
	}

	// --- getters and setters --------------

	public int getIdIngredienteProva(){
		return idIngredienteProva;
	}
	public void setIdIngredienteProva(int idIngredienteProva){
		this.idIngredienteProva = idIngredienteProva;
	}
	public String getNomeIngrediente(){
		return nomeIngrediente;
	}
	public void setNomeIngrediente(String nomeIngrediente){
		this.nomeIngrediente = nomeIngrediente;
	}
	public int getQuantita(){
		return quantita;
	}
	public void setQuantita(int quantita){
		this.quantita = quantita;
	}

	public Set<PiattoProva> getPiattiProva(){
		return piattiProva;
	}
	public void setPiattiProva(Set<PiattoProva> piattiProva){
		this.piattiProva = piattiProva;
	}

	// --- utilities ----------------------------
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredienteProva other = (IngredienteProva) obj;
		if (this.idIngredienteProva != other.idIngredienteProva)
			return false;
		return true;
	}
	
	@Override
	public String toString() {		
		return "IngredienteProva {"+
			"idIngredienteProva="+ idIngredienteProva +","+
			"nomeIngrediente="+ nomeIngrediente +","+
			"quantita="+ quantita +
			"}";
	}
}