package it.unibo.tw.dao.db2;

import java.util.*;
import java.sql.*;
import it.unibo.tw.dao.*;

public class DB2WorkPackageDAO implements WorkPackageDAO{
	
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
				"PRIMARY KEY(idWorkPackage) " +
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
			
	
	//Create
	@Override
	public boolean create(WorkPackageDTO workPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(workPackage == null)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			//if (conn.getMetaData().supportsTransactions()) // se si vuole verificare e gestire il supporto alle transazioni
			conn.setAutoCommit(false); // start transaction block
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(insert);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.setInt(1,workPackage.getIdWorkPackage());
			statement.setString(2,workPackage.getNomeWP());
			statement.setString(3,workPackage.getTitolo());
			statement.setString(4,workPackage.getDescrizione());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			conn.commit(); //esegui transazione
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			if (conn != null) {
				try {
					System.err.print("Transaction is being rolled back");
					conn.rollback();
				} catch(SQLException excep) {
					excep.printStackTrace();
				}
			}
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	
	//Read By
	@Override
	public WorkPackageDTO readByIdWorkPackage(int idWorkPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		WorkPackageDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idWorkPackage);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new WorkPackageDTO();
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
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	@Override
	public WorkPackageDTO readByNomeWP(String nomeWP) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		WorkPackageDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeWP == null || nomeWP.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_nomeWP);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,nomeWP);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new WorkPackageDTO();
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
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Update
	@Override
	public boolean update(WorkPackageDTO workPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(workPackage == null)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			//if (conn.getMetaData().supportsTransactions()) // se si vuole verificare e gestire il supporto alle transazioni
			conn.setAutoCommit(false); // start transaction block
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(update);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,workPackage.getIdWorkPackage());
			statement.setString(2,workPackage.getNomeWP());
			statement.setString(3,workPackage.getTitolo());
			statement.setString(4,workPackage.getDescrizione());
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			statement.executeUpdate();
			conn.commit(); //esegui transazione
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			if (conn != null) {
				try {
					System.err.print("Transaction is being rolled back");
					conn.rollback();
				} catch(SQLException excep) {
					excep.printStackTrace();
				}
			}
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	
	//Delete By
	@Override
	public boolean deleteByIdWorkPackage(int idWorkPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_idWorkPackage);
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
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	@Override
	public boolean deleteByNomeWP(String nomeWP) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeWP == null || nomeWP.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_nomeWP);
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
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Create Table
	@Override
	public boolean createTable() {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			Statement stmt = conn.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			stmt.execute(create);
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			stmt.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Drop Table
	@Override
	public boolean drop() {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			Statement stmt = conn.createStatement();
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			stmt.execute(drop);
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			result = true;
			// --- e. Rilascia la struttura dati del risultato      
			// --- f. Rilascia la struttura dati dello statement      
			stmt.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

	//Find By
	@Override
	public Set<WorkPackageDTO> findWorkPackagesByTitolo(String titolo) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackageDTO> result = new HashSet<WorkPackageDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(titolo == null || titolo.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_work_package_by_titolo);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, titolo);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackageDTO entity = new DB2WorkPackageDTOProxy();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	@Override
	public Set<WorkPackageDTO> findWorkPackagesByDescrizione(String descrizione) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackageDTO> result = new HashSet<WorkPackageDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(descrizione == null || descrizione.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_work_package_by_descrizione);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, descrizione);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackageDTO entity = new DB2WorkPackageDTOProxy();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	
	@Override
	public Set<WorkPackageDTO> findWorkPackagesByIdProgetto(int idProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackageDTO> result = new HashSet<WorkPackageDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idProgetto < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_work_package_by_idProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackageDTO entity = new DB2WorkPackageDTOProxy();
				entity.setIdWorkPackage(rs.getInt("idWorkPackage"));
				entity.setNomeWP(rs.getString("nomeWP"));
				entity.setTitolo(rs.getString("titolo"));
				entity.setDescrizione(rs.getString("descrizione"));
				result.add(entity);
			}
			// --- e. Rilascia la struttura dati del risultato      
			rs.close();
			// --- f. Rilascia la struttura dati dello statement      
			statement.close();
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
			e.printStackTrace();
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {      
			DB2DAOFactory.closeConnection(conn);
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		return result;
	}
	

}