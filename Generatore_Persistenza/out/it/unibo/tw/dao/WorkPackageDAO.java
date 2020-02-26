package it.unibo.tw.dao;

import java.util.*;


public interface WorkPackageDAO{
	
	// --- CRUD -------------------
	
	public boolean create(WorkPackageDTO workPackage);   
	
	
	public WorkPackageDTO readByIdWorkPackage(int idWorkPackage);   
	
	public WorkPackageDTO readByNomeWP(String nomeWP);   
	

	public boolean update(WorkPackageDTO workPackage);   
	
	
	public boolean deleteByIdWorkPackage(int idWorkPackage);   
	
	public boolean deleteByNomeWP(String nomeWP);   
	

	// ---Table -------------------	
	public boolean createTable();   
	

	public boolean drop();   
	

	// ----Find -------------------
	public Set<WorkPackageDTO> findWorkPackagesByTitolo(String titolo);   
	
	public Set<WorkPackageDTO> findWorkPackagesByDescrizione(String descrizione);   
	
	public Set<WorkPackageDTO> findWorkPackagesByIdProgetto(int idProgetto);   
	

}