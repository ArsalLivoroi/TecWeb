package it.unibo.tw.dao.db2;

import java.util.*;
import java.sql.*;
import it.unibo.tw.dao.*;

public class DB2WorkPackagePartnerMappingDAO implements WorkPackagePartnerMappingDAO{
	
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
				"PRIMARY KEY(idPartner, idWorkPackage) " +
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
			
	
	//Create
	@Override
	public boolean create(int idPartner, int idWorkPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0 || idWorkPackage < 0)
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
			statement.setInt(1,idPartner);
			statement.setInt(2,idWorkPackage);
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
	public boolean deleteByIdPartnerIdWorkPackage(int idPartner, int idWorkPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		boolean result = false;
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0 || idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(delete_by_idPartner_idWorkPackage);
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
	public Set<WorkPackageDTO> findWorkPackagesByIdPartner(int idPartner) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<WorkPackageDTO> result = new HashSet<WorkPackageDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idPartner < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_work_package_by_idPartner);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idPartner);
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			ResultSet rs = statement.executeQuery();
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			while( rs.next() ) {
				WorkPackageDTO entity = new DB2WorkPackageDTOProxy();
				entity.setIdWorkPackage(rs.getInt("id_workpackage"));
				entity.setNomeWP(rs.getString("nome_wp"));
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
	public Set<PartnerDTO> findPartnersByIdWorkPackage(int idWorkPackage) {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		Set<PartnerDTO> result = new HashSet<PartnerDTO>();
		Connection conn = null;
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		if(idWorkPackage < 0)
			return result;
		try {
			// --- 3. Apertura della connessione ---  
			conn = DB2DAOFactory.createConnection(); 
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---      
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			PreparedStatement statement = conn.prepareStatement(find_partner_by_idWorkPackage);
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			statement.clearParameters();
			statement.setInt(1, idWorkPackage);
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