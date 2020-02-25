package it.unibo.tw;

import java.io.Serializable;
import java.util.*;

public class PiattoProva implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idPiattoProva;
	private String nomePiatto;
	private String classificazionePiatto;
	private int calorie;


	private Set<IngredienteProva> ingredientiprova; 
	
	// --- costruttore ----------

	public PiattoProva(){
		this.ingredientiprova = new HashSet<IngredienteProva>();
	}

	public PiattoProva(int idPiattoProva, String nomePiatto, String classificazionePiatto, int calorie){
		this();
		this.idPiattoProva = idPiattoProva;
		this.nomePiatto = nomePiatto;
		this.classificazionePiatto = classificazionePiatto;
		this.calorie = calorie;
	}

	// --- getters and setters --------------

	public int getIdPiattoProva(){
		return idPiattoProva;
	}
	public void setIdPiattoProva(int idPiattoProva){
		this.idPiattoProva = idPiattoProva;
	}
	public String getNomePiatto(){
		return nomePiatto;
	}
	public void setNomePiatto(String nomePiatto){
		this.nomePiatto = nomePiatto;
	}
	public String getClassificazionePiatto(){
		return classificazionePiatto;
	}
	public void setClassificazionePiatto(String classificazionePiatto){
		this.classificazionePiatto = classificazionePiatto;
	}
	public int getCalorie(){
		return calorie;
	}
	public void setCalorie(int calorie){
		this.calorie = calorie;
	}

	public Set<IngredienteProva> getIngredientiprova(){
		return ingredientiprova;
	}
	public void setIngredientiprova(Set<IngredienteProva> ingredientiprova){
		this.ingredientiprova = ingredientiprova;
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
		PiattoProva other = (PiattoProva) obj;
		if (this.idPiattoProva != other.idPiattoProva)
			return false;
		return true;
	}
	
	@Override
	public String toString() {		
		return "PiattoProva {"+
			"idPiattoProva="+ idPiattoProva +","+
			"nomePiatto="+ nomePiatto +","+
			"classificazionePiatto="+ classificazionePiatto +","+
			"calorie="+ calorie +
			"}";
	}
}