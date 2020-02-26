package it.unibo.tw;

import it.unibo.tw.db.*;
import java.util.*;
import java.sql.*;

public class WorkPackagePartnerMappingRepository{
	private DataSource dataSource;
	static final String TABLE = "Partner_WorkPackage_Mapping";
	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );	
	static final String insert= 
		"INSERT " +
			"INTO "+ TABLE + " ( " +
				"id_partner," +
				"id_workpackage" +
			") "+
		"VALUESE (?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_idPartner_idWorkPackage = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_partner = ? AND id_workpackage = ?";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_partner INT NOT NULL, "+
				"id_workpackage INT NOT NULL, "+
				"FOREING KEY(id_workpackage) REFERENCES work_package(id_workpackage) "+
				"FOREING KEY(id_partner) REFERENCES partner(id_partner) "+
				"PRIMARY KEY(id_partner, id_workpackage) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	// QUERY ----		
	static final String find_work_package_by_idPartner = 
			" SELECT * "+
			" FROM "+ TABLE + ", work_package "+
			" WHERE Partner_WorkPackage_Mapping.id_workpackage = work_package.id_workpackage"+
			" AND id_partner = ? ";
			
	static final String find_partner_by_idWorkPackage = 
			" SELECT * "+
			" FROM "+ TABLE + ", partner "+
			" WHERE Partner_WorkPackage_Mapping.id_partner = partner.id_partner"+
			" AND id_workpackage = ? ";
			
 
	public  WorkPackagePartnerMappingRepository(int databaseType) {
		dataSource = new DataSource(databaseType);
	} 

	
	//Create
	public boolean create(int idPartner, int idWorkPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0 || idWorkPackage < 0)
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
			statement.setInt(1,idPartner);
			statement.setInt(2,idWorkPackage);
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
	public boolean deleteByIdPartnerIdWorkPackage(int idPartner, int idWorkPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0 || idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(delete_by_idPartner_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idPartner);
			statement.setInt(2,idWorkPackage);
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
	public Set<WorkPackage> findWorkPackagesByIdPartner(int idPartner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackage> result = new HashSet<WorkPackage>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_work_package_by_idPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idPartner);
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
	
	public Set<Partner> findPartnersByIdWorkPackage(int idWorkPackage) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<Partner> result = new HashSet<Partner>();
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
			statement = connection.prepareStatement(find_partner_by_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idWorkPackage);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				Partner entity = new Partner();
				entity.setIdPartner(rs.getInt("idPartner"));
				entity.setSiglaPartner(rs.getString("siglaPartner"));
				entity.setNome(rs.getString("nome"));
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