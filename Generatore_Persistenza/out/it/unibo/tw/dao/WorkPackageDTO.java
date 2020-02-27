package it.unibo.tw.dao;

import java.io.Serializable;
import java.util.*;

public class WorkPackageDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idWorkPackage;
	private String nomeWP;
	private String titolo;
	private String descrizione;


	private ProgettoDTO progetto;
	private Set<PartnerDTO> partners; 
	private boolean listaPartnersIsAlreadyLoaded;
	
	// --- costruttore ----------

	public WorkPackageDTO(){
		this.progetto = new ProgettoDTO();
		this.partners = new HashSet<PartnerDTO>();
		this.listaPartnersIsAlreadyLoaded=false;
	}

	public WorkPackageDTO(int idWorkPackage, String nomeWP, String titolo, String descrizione){
		this();
		this.idWorkPackage = idWorkPackage;
		this.nomeWP = nomeWP;
		this.titolo = titolo;
		this.descrizione = descrizione;
	}

	// --- getters and setters --------------

	public int getIdWorkPackage(){
		return idWorkPackage;
	}
	public void setIdWorkPackage(int idWorkPackage){
		this.idWorkPackage = idWorkPackage;
	}
	public String getNomeWP(){
		return nomeWP;
	}
	public void setNomeWP(String nomeWP){
		this.nomeWP = nomeWP;
	}
	public String getTitolo(){
		return titolo;
	}
	public void setTitolo(String titolo){
		this.titolo = titolo;
	}
	public String getDescrizione(){
		return descrizione;
	}
	public void setDescrizione(String descrizione){
		this.descrizione = descrizione;
	}

	public ProgettoDTO getProgetto(){
		return progetto;
	}
	public void setProgetto(ProgettoDTO progetto){
		this.progetto = progetto;
	}	
	public Set<PartnerDTO> getPartners(){
		return partners;
	}
	public void setPartners(Set<PartnerDTO> partners){
		this.partners = partners;
	}
	public boolean listaPartnersIsAlreadyLoaded(){
		return this.listaPartnersIsAlreadyLoaded;
	}
	public void listaPartnersIsAlreadyLoaded(boolean loaded){
		this.listaPartnersIsAlreadyLoaded = loaded;
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
		WorkPackageDTO other = (WorkPackageDTO) obj;
		if (this.idWorkPackage != other.idWorkPackage)
			return false;
		return true;
	}
	
	@Override
	public String toString() {		
		return "WorkPackage {"+
			"idWorkPackage="+ idWorkPackage +","+
			"nomeWP="+ nomeWP +","+
			"titolo="+ titolo +","+
			"descrizione="+ descrizione +
			"}";
	}
}