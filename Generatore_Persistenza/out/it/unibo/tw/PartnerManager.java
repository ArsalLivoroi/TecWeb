package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class PartnerManager{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "partner";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_partner INT NOT NULL, "+
				"sigla_partner VARCHAR(100) NOT NULL, "+
				"nome VARCHAR(100) NOT NULL, "+
				"FOREING KEY(id_workpackage) REFERENCES work_package(id_workpackage) "+
				"UNIQUE(sigla_partner), "+
				"PRIMARY KEY(id_partner) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_idPartner = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_partner = ?";

	static String delete_by_siglaPartner = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE sigla_partner = ?";
				
		
	// QUERY ----		
	static final String find_partner_by_nome = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE nome = ? ";
			
	static final String find_partner_by_idWorkPackage = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE id_workpackage = ? ";
			

 
	public  PartnerManager(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

	
	//Create
	public boolean insert(Partner partner){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.save(partner);
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
	public boolean update(Partner partner){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.update(partner);
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
	public boolean delete(Partner partner){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.delete(partner);
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
	
	public boolean deleteByIdPartner(int idPartner){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_idPartner);
			query.setInteger(0,idPartner);
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
	
	public boolean deleteBySiglaPartner(String siglaPartner){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_siglaPartner);
			query.setString(0,siglaPartner);
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
	public Set<Partner> find_partner_by_nome(String nome){
		Set<Partner> result = new HashSet<Partner>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_partner_by_nome);
			query.setString(0,nome);
			result=(HashSet<Partner>) query.list();
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
	public Set<Partner> find_partner_by_idWorkPackage(int idWorkPackage){
		Set<Partner> result = new HashSet<Partner>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_partner_by_idWorkPackage);
			query.setInteger(0,idWorkPackage);
			result=(HashSet<Partner>) query.list();
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