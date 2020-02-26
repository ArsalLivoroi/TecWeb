package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class WorkPackageManager{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "work_package";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_workpackage INT NOT NULL, "+
				"nome_wp VARCHAR(100) NOT NULL, "+
				"titolo VARCHAR(100) NOT NULL, "+
				"descrizione VARCHAR(100) NOT NULL, "+
				"FOREING KEY(id_progetto) REFERENCES progetto(id_progetto) "+
				"FOREING KEY(id_partner) REFERENCES partner(id_partner) "+
				"UNIQUE(nome_wp), "+
				"PRIMARY KEY(id_workpackage) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_idWorkPackage = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_workpackage = ?";

	static String delete_by_nomeWP = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE nome_wp = ?";
				
		
	// QUERY ----		
	static final String find_work_package_by_titolo = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE titolo = ? ";
			
	static final String find_work_package_by_descrizione = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE descrizione = ? ";
			
	static final String find_work_package_by_idProgetto = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE id_progetto = ? ";
			
	static final String find_work_package_by_idPartner = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE id_partner = ? ";
			

 
	public  WorkPackageManager(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

	
	//Create
	public boolean insert(WorkPackage workPackage){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.save(workPackage);
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
	public boolean update(WorkPackage workPackage){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.update(workPackage);
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
	public boolean delete(WorkPackage workPackage){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.delete(workPackage);
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
	
	public boolean deleteByIdWorkPackage(int idWorkPackage){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_idWorkPackage);
			query.setInteger(0,idWorkPackage);
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
	
	public boolean deleteByNomeWP(String nomeWP){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_nomeWP);
			query.setString(0,nomeWP);
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
	public Set<WorkPackage> find_work_package_by_titolo(String titolo){
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_work_package_by_titolo);
			query.setString(0,titolo);
			result=(HashSet<WorkPackage>) query.list();
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
	public Set<WorkPackage> find_work_package_by_descrizione(String descrizione){
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_work_package_by_descrizione);
			query.setString(0,descrizione);
			result=(HashSet<WorkPackage>) query.list();
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
	public Set<WorkPackage> find_work_package_by_idProgetto(int idProgetto){
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_work_package_by_idProgetto);
			query.setInteger(0,idProgetto);
			result=(HashSet<WorkPackage>) query.list();
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
	public Set<WorkPackage> find_work_package_by_idPartner(int idPartner){
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_work_package_by_idPartner);
			query.setInteger(0,idPartner);
			result=(HashSet<WorkPackage>) query.list();
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