package it.unibo.tw.dao;

import java.io.Serializable;
import java.util.*;

public class ProgettoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idProgetto;
	private String codiceProgetto;
	private String nomeProgetto;
	private int annoInizio;
	private int durata;


	private Set<WorkPackageDTO> workPackages; 
	private boolean listaWorkPackagesIsAlreadyLoaded;
	
	// --- costruttore ----------

	public ProgettoDTO(){
		this.workPackages = new HashSet<WorkPackageDTO>();
		this.listaWorkPackagesIsAlreadyLoaded=false;
	}

	public ProgettoDTO(int idProgetto, String codiceProgetto, String nomeProgetto, int annoInizio, int durata){
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

	public Set<WorkPackageDTO> getWorkPackages(){
		return workPackages;
	}
	public void setWorkPackages(Set<WorkPackageDTO> workPackages){
		this.workPackages = workPackages;
	}
	public boolean listaWorkPackagesIsAlreadyLoaded(){
		return this.listaWorkPackagesIsAlreadyLoaded;
	}
	public void listaWorkPackagesIsAlreadyLoaded(boolean loaded){
		this.listaWorkPackagesIsAlreadyLoaded = loaded;
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
		ProgettoDTO other = (ProgettoDTO) obj;
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