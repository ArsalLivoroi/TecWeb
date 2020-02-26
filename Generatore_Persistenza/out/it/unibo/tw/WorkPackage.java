package it.unibo.tw;

import java.io.Serializable;
import java.util.*;

public class WorkPackage implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idWorkPackage;
	private String nomeWP;
	private String titolo;
	private String descrizione;


	private Progetto progetto;
	private Set<Partner> partners; 
	
	// --- costruttore ----------

	public WorkPackage(){
		this.progetto = new Progetto();
		this.partners = new HashSet<Partner>();
	}

	public WorkPackage(int idWorkPackage, String nomeWP, String titolo, String descrizione){
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

	public Progetto getProgetto(){
		return progetto;
	}
	public void setProgetto(Progetto progetto){
		this.progetto = progetto;
	}	
	public Set<Partner> getPartners(){
		return partners;
	}
	public void setPartners(Set<Partner> partners){
		this.partners = partners;
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
		WorkPackage other = (WorkPackage) obj;
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