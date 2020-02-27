package it.unibo.tw.dao.db2;

import java.util.*;
import java.sql.*;
import it.unibo.tw.dao.*;

public class DB2PartnerDAO implements PartnerDAO{
	
	static final String TABLE = "partner";
	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );	
	static final String insert= 
		"INSERT " +
			"INTO "+ TABLE + " ( " +
				"id_partner," +
				"sigla_partner," +
				"nome" +
			") "+
		"VALUESE (?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_idPartner = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE id_partner = ? ";
				
	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_siglaPartner = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE sigla_partner = ?";
				
	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = 
			"UPDATE " + TABLE + " " +
				"SET " + 
				"id_partner = ?," +
				"sigla_partner = ?," +
				"nome = ?" +
				"WHERE  id_partner = ? ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_idPartner = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE id_partner = ?";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_siglaPartner = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE sigla_partner = ?";
				

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
				"id_partner INT NOT NULL, "+
				"sigla_partner VARCHAR(100) NOT NULL, "+
				"nome VARCHAR(100) NOT NULL, "+
				"UNIQUE(sigla_partner), "+
				"PRIMARY KEY(idPartner) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
		
	// QUERY ----		
	static final String find_partner_by_nome = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE nome = ? ";
			
	
	//Create
	@Override
	public boolean create(PartnerDTO partner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(partner == null)
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
			statement.setInt(1,partner.getIdPartner());
			statement.setString(2,partner.getSiglaPartner());
			statement.setString(3,partner.getNome());
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
	public PartnerDTO readByIdPartner(int idPartner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		PartnerDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_idPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idPartner);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new PartnerDTO();
				result.setIdPartner(rs.getInt("id_partner"));
				result.setSiglaPartner(rs.getString("sigla_partner"));
				result.setNome(rs.getString("nome"));
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
	public PartnerDTO readBySiglaPartner(String siglaPartner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		PartnerDTO result = null;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(siglaPartner == null || siglaPartner.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(read_by_siglaPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,siglaPartner);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new PartnerDTO();
				result.setIdPartner(rs.getInt("id_partner"));
				result.setSiglaPartner(rs.getString("sigla_partner"));
				result.setNome(rs.getString("nome"));
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
	public boolean update(PartnerDTO partner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(partner == null)
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
			statement.setInt(1,partner.getIdPartner());
			statement.setString(2,partner.getSiglaPartner());
			statement.setString(3,partner.getNome());
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
	public boolean deleteByIdPartner(int idPartner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_idPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idPartner);
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
	public boolean deleteBySiglaPartner(String siglaPartner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(siglaPartner == null || siglaPartner.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_siglaPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,siglaPartner);
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
	public Set<PartnerDTO> findPartnersByNome(String nome) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<PartnerDTO> result = new HashSet<PartnerDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nome == null || nome.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_partner_by_nome);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, nome);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				PartnerDTO entity = new DB2PartnerDTOProxy();
				entity.setIdPartner(rs.getInt("id_partner"));
				entity.setSiglaPartner(rs.getString("sigla_partner"));
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