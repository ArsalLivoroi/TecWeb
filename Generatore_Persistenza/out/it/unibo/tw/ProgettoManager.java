package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class ProgettoManager{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "progetto";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_progetto INT NOT NULL, "+
				"codice_progetto VARCHAR(100) NOT NULL, "+
				"nome_progetto VARCHAR(100) NOT NULL, "+
				"anno_inizio INT NOT NULL, "+
				"durata INT NOT NULL, "+
				"FOREING KEY(id_workpackage) REFERENCES work_package(id_workpackage) "+
				"UNIQUE(codice_progetto), "+
				"PRIMARY KEY(id_progetto) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_idProgetto = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_progetto = ?";

	static String delete_by_codiceProgetto = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE codice_progetto = ?";
				
		
	// QUERY ----		
	static final String find_progetto_by_nomeProgetto = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE nome_progetto = ? ";
			
	static final String find_progetto_by_annoInizio = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE anno_inizio = ? ";
			
	static final String find_progetto_by_durata = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE durata = ? ";
			

 
	public  ProgettoManager(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

	
	//Create
	public boolean insert(Progetto progetto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.save(progetto);
			tx.commit();
			result = true;
		}        
		catch (Exception e) {      
			if (tx!=null)
				tx.rollback();
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	

	//Update
	public boolean update(Progetto progetto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.update(progetto);
			tx.commit();
			result = true;
		}        
		catch (Exception e) {      
			if (tx!=null)
				tx.rollback();
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	

	//Create Table
	public boolean createTable(){
		boolean result = false;
		try { 
			Statement statement = connection.createStatement();
			statement.executeUpdate(create);
			result = true;
			statement.close();
		}        
		catch (Exception e) {      
         	e.printStackTrace();
		}        
		finally {  
		}         
		return result;
	}
	

	//Drop Table
	public boolean drop(){
		boolean result = false;
		try { 
			Statement statement = connection.createStatement();
			statement.executeUpdate(drop);
			result = true;
			statement.close();
		}        
		catch (Exception e) {      
         	e.printStackTrace();
		}        
		finally {  
		}         
		return result;
	}
	
	 
	//Delete
	public boolean delete(Progetto progetto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.delete(progetto);
			tx.commit();
			result = true;
		}        
		catch (Exception e) {      
			if (tx!=null)
				tx.rollback();
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	
	public boolean deleteByIdProgetto(int idProgetto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_idProgetto);
			query.setInteger(0,idProgetto);
			query.executeUpdate();
			tx.commit();
			result = true;
		}        
		catch (Exception e) {      
			if (tx!=null)
				tx.rollback();
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	
	public boolean deleteByCodiceProgetto(String codiceProgetto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_codiceProgetto);
			query.setString(0,codiceProgetto);
			query.executeUpdate();
			tx.commit();
			result = true;
		}        
		catch (Exception e) {      
			if (tx!=null)
				tx.rollback();
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	


	//Find By
	@SuppressWarnings("unchecked")
	public Set<Progetto> find_progetto_by_nomeProgetto(String nomeProgetto){
		Set<Progetto> result = new HashSet<Progetto>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_progetto_by_nomeProgetto);
			query.setString(0,nomeProgetto);
			result=(HashSet<Progetto>) query.list();
		}        
		catch (Exception e) {      
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Progetto> find_progetto_by_annoInizio(int annoInizio){
		Set<Progetto> result = new HashSet<Progetto>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_progetto_by_annoInizio);
			query.setInteger(0,annoInizio);
			result=(HashSet<Progetto>) query.list();
		}        
		catch (Exception e) {      
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Progetto> find_progetto_by_durata(int durata){
		Set<Progetto> result = new HashSet<Progetto>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_progetto_by_durata);
			query.setInteger(0,durata);
			result=(HashSet<Progetto>) query.list();
		}        
		catch (Exception e) {      
         	e.printStackTrace();
		}        
		finally {  
			session.close();
		}         
		return result;
	}
	
}