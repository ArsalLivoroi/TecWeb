package it.unibo.tw;

import it.unibo.tw.db.*;
import java.util.*;
import java.sql.*;

public class PartnerRepository{
	private DataSource dataSource;
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
				"PRIMARY KEY(id_partner) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	// QUERY ----		
	static final String find_partner_by_nome = 
			" SELECT * "+
			" FROM "+ TABLE + 
			" WHERE nome = ? ";
			
 
	public  PartnerRepository(int databaseType) {
		dataSource = new DataSource(databaseType);
	} 

	
	//Create
	public boolean create(Partner partner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(partner == null)
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
			statement.setInt(1,partner.getIdPartner());
			statement.setString(2,partner.getSiglaPartner());
			statement.setString(3,partner.getNome());
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
	public Partner readByIdPartner(int idPartner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Partner result = null;
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
			statement = connection.prepareStatement(read_by_idPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1,idPartner);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new Partner();
				result.setIdPartner(rs.getInt("idPartner"));
				result.setSiglaPartner(rs.getString("siglaPartner"));
				result.setNome(rs.getString("nome"));
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
	
	public Partner readBySiglaPartner(String siglaPartner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Partner result = null;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(siglaPartner == null || siglaPartner.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(read_by_siglaPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1,siglaPartner);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			if ( rs.next() ) {
				result = new Partner();
				result.setIdPartner(rs.getInt("idPartner"));
				result.setSiglaPartner(rs.getString("siglaPartner"));
				result.setNome(rs.getString("nome"));
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
	public boolean update(Partner partner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(partner == null)
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
			statement.setInt(1,partner.getIdPartner());
			statement.setString(2,partner.getSiglaPartner());
			statement.setString(3,partner.getNome());
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
	public boolean deleteByIdPartner(int idPartner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
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
			statement = connection.prepareStatement(delete_by_idPartner);
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
	
	public boolean deleteBySiglaPartner(String siglaPartner) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(siglaPartner == null || siglaPartner.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(delete_by_siglaPartner);
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
	public Set<Partner> findPartnersByNome(String nome) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<Partner> result = new HashSet<Partner>();
		PreparedStatement statement = null;
		Connection connection = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(nome == null || nome.isEmpty() )
			return result;
		try {
			// --- 3. Apertura della connessione ---   
			connection = this.dataSource.getConnection();
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			statement = connection.prepareStatement(find_partner_by_nome);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setString(1, nome);
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