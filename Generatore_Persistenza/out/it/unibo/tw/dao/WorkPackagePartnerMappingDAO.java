package it.unibo.tw.dao;

import java.util.*;


public interface WorkPackagePartnerMappingDAO{
	
	// --- CRUD -------------------
	
	public boolean create(int idPartner, int idWorkPackage);   
	
	

	
	public boolean deleteByIdPartnerIdWorkPackage(int idPartner, int idWorkPackage);   
	

	// ---Table -------------------	
	public boolean createTable();   
	

	public boolean drop();   
	

	// ----Find -------------------
	public Set<WorkPackageDTO> findWorkPackagesByIdPartner(int idPartner);   
	
	public Set<PartnerDTO> findPartnersByIdWorkPackage(int idWorkPackage);   
	

}