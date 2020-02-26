package it.unibo.tw;

import java.io.Serializable;
import java.util.*;

public class Partner implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idPartner;
	private String siglaPartner;
	private String nome;


	private Set<WorkPackage> workPackages; 
	
	// --- costruttore ----------

	public Partner(){
		this.workPackages = new HashSet<WorkPackage>();
	}

	public Partner(int idPartner, String siglaPartner, String nome){
		this();
		this.idPartner = idPartner;
		this.siglaPartner = siglaPartner;
		this.nome = nome;
	}

	// --- getters and setters --------------

	public int getIdPartner(){
		return idPartner;
	}
	public void setIdPartner(int idPartner){
		this.idPartner = idPartner;
	}
	public String getSiglaPartner(){
		return siglaPartner;
	}
	public void setSiglaPartner(String siglaPartner){
		this.siglaPartner = siglaPartner;
	}
	public String getNome(){
		return nome;
	}
	public void setNome(String nome){
		this.nome = nome;
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
		Partner other = (Partner) obj;
		if (this.idPartner != other.idPartner)
			return false;
		return true;
	}
	
	@Override
	public String toString() {		
		return "Partner {"+
			"idPartner="+ idPartner +","+
			"siglaPartner="+ siglaPartner +","+
			"nome="+ nome +
			"}";
	}
}