package it.unibo.tw.dao;

import java.io.Serializable;
import java.util.*;

public class PartnerDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int idPartner;
	private String siglaPartner;
	private String nome;


	private Set<WorkPackageDTO> workPackages; 
	private boolean listaWorkPackagesIsAlreadyLoaded;
	
	// --- costruttore ----------

	public PartnerDTO(){
		this.workPackages = new HashSet<WorkPackageDTO>();
		this.listaWorkPackagesIsAlreadyLoaded=false;
	}

	public PartnerDTO(int idPartner, String siglaPartner, String nome){
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
		PartnerDTO other = (PartnerDTO) obj;
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