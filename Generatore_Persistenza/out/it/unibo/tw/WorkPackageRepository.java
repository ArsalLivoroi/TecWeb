package it.unibo.tw;

import it.unibo.tw.db.*;
import java.util.*;
import java.sql.*;

public class WorkPackageRepository{
	private DataSource dataSource;
	static final String TABLE = "work_package";
	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );	
	static final String insert= 
		"INSERT " +
			"INTO "+ TABLE + " ( " +
				"id_workpackage," +
				"nome_wp," +
				"titolo," +
				"descrizione" +
			") "+
		"VALUESE (?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_idWorkPackage = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE id_workpackage = ? ";
				
	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_nomeWP = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE nome_wp = ?";
				
	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = 
			"UPDATE " + TABLE + " " +
				"SET " + 
				"id_workpackage = ?," +
				"nome_wp = ?," +
				"titolo = ?," +
				"descrizione = ?" +
				"WHERE  id_workpackage = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_idWorkPackage = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_workpackage = ?";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_nomeWP = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE nome_wp = ?";
				
	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_workpackage INT NOT NULL, "+
				"nome_wp VARCHAR(100) NOT NULL, "+
				"titolo VARCHAR(100) NOT NULL, "+
				"descrizione VARCHAR(100) NOT NULL, "+
				"FOREING KEY(id_progetto) REFERENCES progetto(id_progetto) "+
				"UNIQUE(nome_wp), "+
				"PRIMARY KEY(id_workpackage) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
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
			
 
	public  WorkPackageRepository(int databaseType) {
		dataSource = new DataSource(databaseType);
	} 

	
	//Create
	public boolean create(WorkPackage workPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(workPackage == null)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			//if (connection.getMetaData().supportsTransactions()) // se si vuole verificare e gestire il supporto alle transazioni
			connection.setAutoCommit(false); // start transaction block
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(insert);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.setInt(1,workPackage.getIdWorkPackage());
			statement.setString(2,workPackage.getNomeWP());
			statement.setString(3,workPackage.getTitolo());
			statement.setString(4,workPackage.getDescrizione());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			connection.commit(); //esegui transazione
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			if (connection != null) {
				try {
					System.err.print("Transaction is being rolled back");
					connection.rollback();
				} catch(SQLException excep) {
					throw new PersistenceException(excep.getMessage());
				}
			}
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
				connection.setAutoCommit(true); //una buona prassi è ripristinare l'auto-commit
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	
	//Read By
	public WorkPackage readByIdWorkPackage(int idWorkPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		WorkPackage result = null;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(read_by_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idWorkPackage);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new WorkPackage();
				result.setIdWorkPackage(rs.getInt("idWorkPackage"));
				result.setNomeWP(rs.getString("nomeWP"));
				result.setTitolo(rs.getString("titolo"));
				result.setDescrizione(rs.getString("descrizione"));
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	public WorkPackage readByNomeWP(String nomeWP) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		WorkPackage result = null;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeWP == null || nomeWP.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(read_by_nomeWP);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,nomeWP);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new WorkPackage();
				result.setIdWorkPackage(rs.getInt("idWorkPackage"));
				result.setNomeWP(rs.getString("nomeWP"));
				result.setTitolo(rs.getString("titolo"));
				result.setDescrizione(rs.getString("descrizione"));
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Update
	public boolean update(WorkPackage workPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(workPackage == null)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			//if (connection.getMetaData().supportsTransactions()) // se si desidera verificare e gestire il supporto alle transazioni
			connection.setAutoCommit(false); // start transaction block
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(update);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,workPackage.getIdWorkPackage());
			statement.setString(2,workPackage.getNomeWP());
			statement.setString(3,workPackage.getTitolo());
			statement.setString(4,workPackage.getDescrizione());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			connection.commit(); //esegui transazione
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			if (connection != null) {
				try {
					System.err.print("Transaction is being rolled back");
					connection.rollback();
				} catch(SQLException excep) {
					throw new PersistenceException(excep.getMessage());
				}
			}
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
				connection.setAutoCommit(true); //una buona prassi è ripristinare l'auto-commit
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	
	//Delete By
	public boolean deleteByIdWorkPackage(int idWorkPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(delete_by_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idWorkPackage);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	public boolean deleteByNomeWP(String nomeWP) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeWP == null || nomeWP.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(delete_by_nomeWP);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,nomeWP);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Create Table
	public boolean createTable() throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection connection = null;
		Statement statement = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.execute(create);
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Drop Table
	public boolean drop() throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection connection = null;
		Statement statement = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.execute(drop);
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Find By
	public Set<WorkPackage> findWorkPackagesByTitolo(String titolo) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(titolo == null || titolo.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_work_package_by_titolo);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, titolo);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackage entity = new WorkPackage();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				entity.setProgetto(new ProgettoRepository(DataSource.DB2).readByIdProgetto(rs.getInt("IdProgetto")));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	public Set<WorkPackage> findWorkPackagesByDescrizione(String descrizione) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(descrizione == null || descrizione.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_work_package_by_descrizione);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, descrizione);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackage entity = new WorkPackage();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				entity.setProgetto(new ProgettoRepository(DataSource.DB2).readByIdProgetto(rs.getInt("IdProgetto")));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	public Set<WorkPackage> findWorkPackagesByIdProgetto(int idProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idProgetto < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_work_package_by_idProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackage entity = new WorkPackage();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				entity.setProgetto(new ProgettoRepository(DataSource.DB2).readByIdProgetto(rs.getInt("IdProgetto")));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			throw new PersistenceException(e.getMessage());
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

}