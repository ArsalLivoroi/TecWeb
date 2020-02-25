package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class PiattoProvaManager{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "piatto_prova";

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_piattoprova INT NOT NULL, "+
				"nome_piatto VARCHAR(100) NOT NULL, "+
				"classificazione_piatto VARCHAR(100) NOT NULL, "+
				"calorie INT NOT NULL, "+
				"FOREING KEY(id_ingredienteprova) REFERENCES ingrediente_prova(id_ingredienteprova) "+
				"UNIQUE(nome_piatto), "+
				"PRIMARY KEY(id_piattoprova) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_idPiattoProva = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_piattoprova = ?";

	static String delete_by_nomePiatto = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE nome_piatto = ?";
				
		
	// QUERY ----		
	static final String find_piatto_prova_by_classificazionePiatto = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE classificazione_piatto = ? ";
			
	static final String find_piatto_prova_by_calorie = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE calorie = ? ";
			
	static final String find_ingrediente_prova_by_idPiattoProva = 
			" SELECT * "+
			" FROM "+ TABLE + ", ingrediente_prova "+
			" WHERE piatto_prova.id_ingredienteprova = ingrediente_prova.id_ingredienteprova"+
			" AND id_piattoprova = ? ";
			

 
	public  PiattoProvaManager(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

	
	//Create
	public boolean insert(PiattoProva piattoProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.save(piattoProva);
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
	public boolean update(PiattoProva piattoProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.update(piattoProva);
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
	public boolean delete(PiattoProva piattoProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			session.delete(piattoProva);
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
	
	public boolean deleteByIdPiattoProva(int idPiattoProva){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_idPiattoProva);
			query.setInteger(0,idPiattoProva);
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
	
	public boolean deleteByNomePiatto(String nomePiatto){
		boolean result = false;
		Session session = factory.openSession();
		Transaction tx = null;
		try { 
			tx = session.beginTransaction();
			Query query= session.createSQLQuery(delete_by_nomePiatto);
			query.setString(0,nomePiatto);
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
	public Set<PiattoProva> find_piatto_prova_by_classificazionePiatto(String classificazionePiatto){
		Set<PiattoProva> result = new HashSet<PiattoProva>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_piatto_prova_by_classificazionePiatto);
			query.setString(0,classificazionePiatto);
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
	
	@SuppressWarnings("unchecked")
	public Set<PiattoProva> find_piatto_prova_by_calorie(int calorie){
		Set<PiattoProva> result = new HashSet<PiattoProva>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_piatto_prova_by_calorie);
			query.setInteger(0,calorie);
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
	
	@SuppressWarnings("unchecked")
	public Set<IngredienteProva> find_ingrediente_prova_by_idPiattoProva(int idPiattoProva){
		Set<IngredienteProva> result = new HashSet<IngredienteProva>();
		Session session = factory.openSession();
		try { 
			Query query= session.createSQLQuery(find_ingrediente_prova_by_idPiattoProva);
			query.setInteger(0,idPiattoProva);
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
	
}