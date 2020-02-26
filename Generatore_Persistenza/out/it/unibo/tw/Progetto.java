package it.unibo.tw;

import java.io.Serializable;
import java.util.*;

public class Progetto implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idProgetto;
	private String codiceProgetto;
	private String nomeProgetto;
	private int annoInizio;
	private int durata;


	private Set<WorkPackage> workPackages; 
	
	// --- costruttore ----------

	public Progetto(){
		this.workPackages = new HashSet<WorkPackage>();
	}

	public Progetto(int idProgetto, String codiceProgetto, String nomeProgetto, int annoInizio, int durata){
		this();
		this.idProgetto = idProgetto;
		this.codiceProgetto = codiceProgetto;
		this.nomeProgetto = nomeProgetto;
		this.annoInizio = annoInizio;
		this.durata = durata;
	}

	// --- getters and setters --------------

	public int getIdProgetto(){
		return idProgetto;
	}
	public void setIdProgetto(int idProgetto){
		this.idProgetto = idProgetto;
	}
	public String getCodiceProgetto(){
		return codiceProgetto;
	}
	public void setCodiceProgetto(String codiceProgetto){
		this.codiceProgetto = codiceProgetto;
	}
	public String getNomeProgetto(){
		return nomeProgetto;
	}
	public void setNomeProgetto(String nomeProgetto){
		this.nomeProgetto = nomeProgetto;
	}
	public int getAnnoInizio(){
		return annoInizio;
	}
	public void setAnnoInizio(int annoInizio){
		this.annoInizio = annoInizio;
	}
	public int getDurata(){
		return durata;
	}
	public void setDurata(int durata){
		this.durata = durata;
	}

	public Set<WorkPackage> getWorkPackages(){
		return workPackages;
	}
	public void setWorkPackages(Set<WorkPackage> workPackages){
		this.workPackages = workPackages;
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
		Progetto other = (Progetto) obj;
		if (this.idProgetto != other.idProgetto)
			return false;
		return true;
	}
	
	@Override
	public String toString() {		
		return "Progetto {"+
			"idProgetto="+ idProgetto +","+
			"codiceProgetto="+ codiceProgetto +","+
			"nomeProgetto="+ nomeProgetto +","+
			"annoInizio="+ annoInizio +","+
			"durata="+ durata +
			"}";
	}
}