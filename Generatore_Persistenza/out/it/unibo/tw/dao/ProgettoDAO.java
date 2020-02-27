package it.unibo.tw.dao;

import java.util.*;


public interface ProgettoDAO{
	
	// --- CRUD -------------------
	
	public boolean create(ProgettoDTO progetto);   
	
	
	public ProgettoDTO readByIdProgetto(int idProgetto);   
	
	public ProgettoDTO readByCodiceProgetto(String codiceProgetto);   
	

	public boolean update(ProgettoDTO progetto);   
	
	
	public boolean deleteByIdProgetto(int idProgetto);   
	
	public boolean deleteByCodiceProgetto(String codiceProgetto);   
	

	// ---Table -------------------	
	public boolean createTable();   
	

	public boolean drop();   
	

	// ----Find -------------------
	public Set<ProgettoDTO> findProgettiByNomeProgetto(String nomeProgetto);   
	
	public Set<ProgettoDTO> findProgettiByAnnoInizio(int annoInizio);   
	
	public Set<ProgettoDTO> findProgettiByDurata(int durata);   
	

}