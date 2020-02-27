package it.unibo.tw.dao;

import java.util.*;


public interface PartnerDAO{
	
	// --- CRUD -------------------
	
	public boolean create(PartnerDTO partner);   
	
	
	public PartnerDTO readByIdPartner(int idPartner);   
	
	public PartnerDTO readBySiglaPartner(String siglaPartner);   
	

	public boolean update(PartnerDTO partner);   
	
	
	public boolean deleteByIdPartner(int idPartner);   
	
	public boolean deleteBySiglaPartner(String siglaPartner);   
	

	// ---Table -------------------	
	public boolean createTable();   
	

	public boolean drop();   
	

	// ----Find -------------------
	public Set<PartnerDTO> findPartnersByNome(String nome);   
	

}