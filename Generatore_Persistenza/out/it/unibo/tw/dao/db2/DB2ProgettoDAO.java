package it.unibo.tw.dao.db2;

import java.util.*;
import java.sql.*;
import it.unibo.tw.dao.*;

public class DB2ProgettoDAO implements ProgettoDAO{
	
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
				"PRIMARY KEY(idProgetto) " +
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
			
	
	//Create
	@Override
	public boolean create(ProgettoDTO progetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(progetto == null)
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
			statement.setInt(1,progetto.getIdProgetto());
			statement.setString(2,progetto.getCodiceProgetto());
			statement.setString(3,progetto.getNomeProgetto());
			statement.setInt(4,progetto.getAnnoInizio());
			statement.setInt(5,progetto.getDurata());
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
	public ProgettoDTO readByIdProgetto(int idProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		ProgettoDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idProgetto < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_idProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new ProgettoDTO();
				result.setIdProgetto(rs.getInt("id_progetto"));
				result.setCodiceProgetto(rs.getString("codice_progetto"));
				result.setNomeProgetto(rs.getString("nome_progetto"));
				result.setAnnoInizio(rs.getInt("anno_inizio"));
				result.setDurata(rs.getInt("durata"));
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
	public ProgettoDTO readByCodiceProgetto(String codiceProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		ProgettoDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(codiceProgetto == null || codiceProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_codiceProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,codiceProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new ProgettoDTO();
				result.setIdProgetto(rs.getInt("id_progetto"));
				result.setCodiceProgetto(rs.getString("codice_progetto"));
				result.setNomeProgetto(rs.getString("nome_progetto"));
				result.setAnnoInizio(rs.getInt("anno_inizio"));
				result.setDurata(rs.getInt("durata"));
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
	public boolean update(ProgettoDTO progetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(progetto == null)
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
			statement.setInt(1,progetto.getIdProgetto());
			statement.setString(2,progetto.getCodiceProgetto());
			statement.setString(3,progetto.getNomeProgetto());
			statement.setInt(4,progetto.getAnnoInizio());
			statement.setInt(5,progetto.getDurata());
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
	public boolean deleteByIdProgetto(int idProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idProgetto < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_idProgetto);
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
	public boolean deleteByCodiceProgetto(String codiceProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(codiceProgetto == null || codiceProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_codiceProgetto);
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
	public Set<ProgettoDTO> findProgettiByNomeProgetto(String nomeProgetto) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<ProgettoDTO> result = new HashSet<ProgettoDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nomeProgetto == null || nomeProgetto.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_progetto_by_nomeProgetto);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, nomeProgetto);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				ProgettoDTO entity = new DB2ProgettoDTOProxy();
				entity.setIdProgetto(rs.getInt("id_progetto"));
				entity.setCodiceProgetto(rs.getString("codice_progetto"));
				entity.setNomeProgetto(rs.getString("nome_progetto"));
				entity.setAnnoInizio(rs.getInt("anno_inizio"));
				entity.setDurata(rs.getInt("durata"));
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
	public Set<ProgettoDTO> findProgettiByAnnoInizio(int annoInizio) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<ProgettoDTO> result = new HashSet<ProgettoDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(annoInizio < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_progetto_by_annoInizio);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, annoInizio);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				ProgettoDTO entity = new DB2ProgettoDTOProxy();
				entity.setIdProgetto(rs.getInt("id_progetto"));
				entity.setCodiceProgetto(rs.getString("codice_progetto"));
				entity.setNomeProgetto(rs.getString("nome_progetto"));
				entity.setAnnoInizio(rs.getInt("anno_inizio"));
				entity.setDurata(rs.getInt("durata"));
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
	public Set<ProgettoDTO> findProgettiByDurata(int durata) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<ProgettoDTO> result = new HashSet<ProgettoDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(durata < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_progetto_by_durata);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, durata);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				ProgettoDTO entity = new DB2ProgettoDTOProxy();
				entity.setIdProgetto(rs.getInt("id_progetto"));
				entity.setCodiceProgetto(rs.getString("codice_progetto"));
				entity.setNomeProgetto(rs.getString("nome_progetto"));
				entity.setAnnoInizio(rs.getInt("anno_inizio"));
				entity.setDurata(rs.getInt("durata"));
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