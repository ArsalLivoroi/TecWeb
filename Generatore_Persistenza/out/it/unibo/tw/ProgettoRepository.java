package it.unibo.tw;

import it.unibo.tw.db.*;
import java.util.*;
import java.sql.*;

public class ProgettoRepository{
	private DataSource dataSource;
	static final String TABLE = "progetto";
	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );	
	static final String insert= 
		"INSERT " +
			"INTO "+ TABLE + " ( " +
				"id_progetto," +
				"codice_progetto," +
				"nome_progetto," +
				"anno_inizio," +
				"durata" +
			") "+
		"VALUESE (?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_idProgetto = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE id_progetto = ? ";
				
	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_codiceProgetto = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE codice_progetto = ?";
				
	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = 
			"UPDATE " + TABLE + " " +
				"SET " + 
				"id_progetto = ?," +
				"codice_progetto = ?," +
				"nome_progetto = ?," +
				"anno_inizio = ?," +
				"durata = ?" +
				"WHERE  id_progetto = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_idProgetto = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_progetto = ?";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_codiceProgetto = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE codice_progetto = ?";
				
	// -------------------------------------------------------------------------------------

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
			
 
	public  ProgettoRepository(int databaseType) {
		dataSource = new DataSource(databaseType);
	} 

	
	//Create
	public boolean create(Progetto progetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(progetto == null)
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
			statement.setInt(1,progetto.getIdProgetto());
			statement.setString(2,progetto.getCodiceProgetto());
			statement.setString(3,progetto.getNomeProgetto());
			statement.setInt(4,progetto.getAnnoInizio());
			statement.setInt(5,progetto.getDurata());
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
	public Progetto readByIdProgetto(int idProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Progetto result = null;
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
			statement = connection.prepareStatement(read_by_idProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new Progetto();
				result.setIdProgetto(rs.getInt("idProgetto"));
				result.setCodiceProgetto(rs.getString("codiceProgetto"));
				result.setNomeProgetto(rs.getString("nomeProgetto"));
				result.setAnnoInizio(rs.getInt("annoInizio"));
				result.setDurata(rs.getInt("durata"));
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
	
	public Progetto readByCodiceProgetto(String codiceProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Progetto result = null;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(codiceProgetto == null || codiceProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(read_by_codiceProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,codiceProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new Progetto();
				result.setIdProgetto(rs.getInt("idProgetto"));
				result.setCodiceProgetto(rs.getString("codiceProgetto"));
				result.setNomeProgetto(rs.getString("nomeProgetto"));
				result.setAnnoInizio(rs.getInt("annoInizio"));
				result.setDurata(rs.getInt("durata"));
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
	public boolean update(Progetto progetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(progetto == null)
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
			statement.setInt(1,progetto.getIdProgetto());
			statement.setString(2,progetto.getCodiceProgetto());
			statement.setString(3,progetto.getNomeProgetto());
			statement.setInt(4,progetto.getAnnoInizio());
			statement.setInt(5,progetto.getDurata());
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
	public boolean deleteByIdProgetto(int idProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
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
			statement = connection.prepareStatement(delete_by_idProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idProgetto);
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
	
	public boolean deleteByCodiceProgetto(String codiceProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(codiceProgetto == null || codiceProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(delete_by_codiceProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,codiceProgetto);
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
	public Set<Progetto> findProgettiByNomeProgetto(String nomeProgetto) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<Progetto> result = new HashSet<Progetto>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeProgetto == null || nomeProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_progetto_by_nomeProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, nomeProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				Progetto entity = new Progetto();
				entity.setIdProgetto(rs.getInt("idProgetto"));
				entity.setCodiceProgetto(rs.getString("codiceProgetto"));
				entity.setNomeProgetto(rs.getString("nomeProgetto"));
				entity.setAnnoInizio(rs.getInt("annoInizio"));
				entity.setDurata(rs.getInt("durata"));
				entity.setWorkPackages(new WorkPackageRepository(DataSource.DB2).findWorkPackagesByIdProgetto(rs.getInt("IdWorkPackage")));
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
	
	public Set<Progetto> findProgettiByAnnoInizio(int annoInizio) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<Progetto> result = new HashSet<Progetto>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(annoInizio < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_progetto_by_annoInizio);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, annoInizio);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				Progetto entity = new Progetto();
				entity.setIdProgetto(rs.getInt("idProgetto"));
				entity.setCodiceProgetto(rs.getString("codiceProgetto"));
				entity.setNomeProgetto(rs.getString("nomeProgetto"));
				entity.setAnnoInizio(rs.getInt("annoInizio"));
				entity.setDurata(rs.getInt("durata"));
				entity.setWorkPackages(new WorkPackageRepository(DataSource.DB2).findWorkPackagesByIdProgetto(rs.getInt("IdWorkPackage")));
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
	
	public Set<Progetto> findProgettiByDurata(int durata) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<Progetto> result = new HashSet<Progetto>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(durata < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_progetto_by_durata);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, durata);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				Progetto entity = new Progetto();
				entity.setIdProgetto(rs.getInt("idProgetto"));
				entity.setCodiceProgetto(rs.getString("codiceProgetto"));
				entity.setNomeProgetto(rs.getString("nomeProgetto"));
				entity.setAnnoInizio(rs.getInt("annoInizio"));
				entity.setDurata(rs.getInt("durata"));
				entity.setWorkPackages(new WorkPackageRepository(DataSource.DB2).findWorkPackagesByIdProgetto(rs.getInt("IdWorkPackage")));
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