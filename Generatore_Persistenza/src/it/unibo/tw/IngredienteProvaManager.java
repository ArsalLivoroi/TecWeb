package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class IngredienteProvaManager{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "ingrediente_prova";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_ingredienteprova INT NOT NULL, "+
				"nome_ingrediente VARCHAR(100) NOT NULL, "+
				"quantita INT NOT NULL, "+
				"FOREING KEY(id_piattoprova) REFERENCES piatto_prova(id_piattoprova) "+
				"UNIQUE(nome_ingrediente), "+
				"PRIMARY KEY(id_ingredienteprova) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_idIngredienteProva = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_ingredienteprova = ?";

	static String delete_by_nomeIngrediente = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE nome_ingrediente = ?";
				
		
	// QUERY ----		
	static final String find_ingrediente_prova_by_quantita = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE quantita = ? ";
			
	static final String find_piatto_prova_by_idIngredienteProva = 
			" SELECT * "+
			" FROM "+ TABLE + ", piatto_prova "+
			" WHERE ingrediente_prova.id_piattoprova = piatto_prova.id_piattoprova"+
			" AND id_ingredienteprova = ? ";
			

 
	public  IngredienteProvaManager(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

	
	//Create
	public boolean insert(IngredienteProva ingredienteProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.save(ingredienteProva);
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
	public boolean update(IngredienteProva ingredienteProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.update(ingredienteProva);
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
	public boolean delete(IngredienteProva ingredienteProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.delete(ingredienteProva);
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
	
	public boolean deleteByIdIngredienteProva(int idIngredienteProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_idIngredienteProva);
			query.setInteger(0,idIngredienteProva);
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
	
	public boolean deleteByNomeIngrediente(String nomeIngrediente){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_nomeIngrediente);
			query.setString(0,nomeIngrediente);
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
	public Set<IngredienteProva> find_ingrediente_prova_by_quantita(int quantita){
		Set<IngredienteProva> result = new HashSet<IngredienteProva>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_ingrediente_prova_by_quantita);
			query.setInteger(0,quantita);
			result=(HashSet<IngredienteProva>) query.list();
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
	public Set<PiattoProva> find_piatto_prova_by_idIngredienteProva(int idIngredienteProva){
		Set<PiattoProva> result = new HashSet<PiattoProva>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_piatto_prova_by_idIngredienteProva);
			query.setInteger(0,idIngredienteProva);
			result=(HashSet<PiattoProva>) query.list();
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