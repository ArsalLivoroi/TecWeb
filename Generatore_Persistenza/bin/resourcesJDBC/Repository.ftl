package it.unibo.tw;

import it.unibo.tw.db.*;
import java.util.*;
import java.sql.*;

public class ${repository.nomeRepository}{
	private DataSource dataSource;
	static final String TABLE = "${repository.nomeTabella}";
<#-- 
<#list repository.attributi as attributo>
	static final String ${attributo.nomeColumn?upper_case}= "${attributo.nomeColumn}";
</#list>
 -->	
	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( id, name, description, ...) VALUES ( ?,?, ... );	
	static final String insert= 
		"INSERT " +
			"INTO "+ TABLE + " ( " +
			<#list repository.allAttributi as attributo>
				"${attributo.nomeColumn}<#if attributo?has_next>,</#if>" +
			</#list>	
			") "+
		"VALUESE (<#list repository.allAttributi as attributo>?<#if attributo?has_next>,</#if></#list>) ";
<#if repository.haveUML>
	<#list repository.primaryKeys as primaryKey>

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_${primaryKey.nome} = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE ${primaryKey.nomeColumn} = ? ";
				
	</#list>	
	<#list repository.unique as unique>
	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_<#list unique.attributi as attributo>${attributo.nome}<#if attributo?has_next>_</#if></#list> = 
			"SELECT * " +
				"FROM " + TABLE + " " +
				"WHERE <#list unique.attributi as attributo>${attributo.nomeColumn} = ?<#if attributo?has_next> AND </#if></#list>";
				
	</#list>
</#if>
<#if !repository.nome?contains("Mapping")>
	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = 
			"UPDATE " + TABLE + " " +
				"SET " + 
	<#list repository.allAttributi as attributo>				
				"${attributo.nomeColumn} = ?<#if attributo?has_next>,</#if>" +
	</#list>				
				"WHERE <#list repository.primaryKeys as primaryKey> ${primaryKey.nomeColumn} = ? <#if primaryKey?has_next>AND</#if></#list>";
</#if>	

	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_<#list repository.primaryKeys as attributo>${attributo.nome}<#if attributo?has_next>_</#if></#list> = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE <#list repository.primaryKeys as attributo>${attributo.nomeColumn} = ?<#if attributo?has_next> AND </#if></#list>";

<#list repository.unique as unique>
	// SELECT * FROM table WHERE idcolumn = ?;
	static String delete_by_<#list unique.attributi as attributo>${attributo.nome}<#if attributo?has_next>_</#if></#list> = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE <#list unique.attributi as attributo>${attributo.nomeColumn} = ?<#if attributo?has_next> AND </#if></#list>";
				
</#list>
	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
<#list repository.primaryKeys as primaryKey>
				"${primaryKey.nomeColumn} ${primaryKey.tipoDB?upper_case} NOT NULL, "+
</#list>
<#-- 		
<#list repository.unique as unique>
	<#list unique.attributi as attributo>
				"${attributo.nomeColumn} ${attributo.tipoDB?upper_case} NOT NULL, "+
	</#list>			
</#list>
-->
<#list repository.attributi as attributo>
				"${attributo.nomeColumn} ${attributo.tipoDB?upper_case} NOT NULL, "+
</#list>
<#list repository.riferimenti as riferimento>
				"FOREING KEY(<#list riferimento.to.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>,</#if></#list>) REFERENCES ${riferimento.to.nomeTabella}(<#list riferimento.to.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>, </#if></#list>) "+
</#list>
<#list repository.unique as unique>
				"UNIQUE(<#list unique.attributi as attributo>${attributo.nomeColumn}<#if attributo?has_next>, </#if></#list>), "+
</#list>				
				"PRIMARY KEY(<#list repository.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>, </#if></#list>) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	// QUERY ----		
<#list repository.metodiFind as metodoFind>
	static final String find_${metodoFind.classeTarget.nomeTabella}_by_${metodoFind.attributo.nome} = 
			" SELECT * "+
			<#if repository.nomeTabella!=metodoFind.classeTarget.nomeTabella>
			" FROM "+ TABLE + ", ${metodoFind.classeTarget.nomeTabella} "+
			" WHERE ${repository.nomeTabella}.${metodoFind.classeTarget.primaryKey.nomeColumn} = ${metodoFind.classeTarget.nomeTabella}.${metodoFind.classeTarget.primaryKey.nomeColumn}"+
			" AND ${metodoFind.attributo.nomeColumn} = ? ";
			<#else>
			" FROM "+ TABLE + 
			" WHERE ${metodoFind.attributo.nomeColumn} = ? ";
			</#if>
			
</#list>
 <#-- -->
 
	public  ${repository.nomeRepository}(int databaseType) {
		dataSource = new DataSource(databaseType);
	} 

<#macro metodo_tlf mmq>
	public ${mmq.tipoRitorno} ${mmq.nomeMetodo}(${mmq.byAttributi}) throws PersistenceException {
		// --- 1. Dichiarazione della variabile per il risultato ---\r\n" + 
		<#list mmq.m1 as s>
		${s}
		</#list>
		// --- 2. Controlli preliminari sui dati in ingresso ---      
		<#-- //#TODO# if ( code == null || code < 0 ) ( nome.isEmpty() || nome==null ) ( obj == null ) ... {-->
		<#list mmq.m2 as s>
		${s}
		</#list>
		try {
			// --- 3. Apertura della connessione ---   
		<#list mmq.m3 as s>
			${s}
		</#list>      
			// --- 4. Tentativo di accesso al db e impostazione del risultato ---            
			// --- a. Crea (se senza parametri) o prepara (se con parametri) lo statement      
			<#list mmq.m4a as s>
			${s}
			</#list>    
			// --- b. Pulisci e imposta i parametri (se ve ne sono)
			<#list mmq.m4b as s>
			${s}
			</#list>        
			// --- c. Esegui l'azione sul database ed estrai il risultato (se atteso)      
			<#list mmq.m4c as s>
			${s}
			</#list>        
			// --- d. Cicla sul risultato (se presente) per accedere ai valori di ogni sua tupla      
			<#list mmq.m4d as s>
			${s}
			</#list>
			// --- e. Rilascia la struttura dati del risultato      
			<#list mmq.m4e as s>
			${s}
			</#list>
			// --- f. Rilascia la struttura dati dello statement      
			<#list mmq.m4f as s>
			${s}
			</#list>
		}      
		// --- 5. Gestione di eventuali eccezioni ---      
		catch (Exception e) {      
		<#list mmq.m5 as s>
			${s}
		</#list>
		}      
		// --- 6. Rilascio, SEMPRE E COMUNQUE, la connessione prima di restituire il controllo al chiamante      
		finally {  
			try {    
				if (statement != null)
					statement.close();
				if (connection!= null)
					connection.close();
				<#list mmq.m6 as s>
				${s}
				</#list>
			}catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}      
		// --- 7. Restituzione del risultato (eventualmente di fallimento)      
		<#list mmq.m7 as s>
		${s}
		</#list>
	}
	
</#macro>
	
	//Create
<@metodo_tlf repository.insert />
	
<#if repository.haveUML>
	//Read By
<#list repository.reads as read>
<@metodo_tlf read />
</#list>
</#if>

<#if !repository.nome?contains("Mapping")>
	//Update
<@metodo_tlf repository.update />
</#if>	
	
	//Delete By
<#list repository.deletes as delete>
<@metodo_tlf delete />
</#list>

	//Create Table
<@metodo_tlf repository.createtable />

	//Drop Table
<@metodo_tlf repository.droptable />

<#if repository.finds?has_content>
	//Find By
<#list repository.finds as find>
<@metodo_tlf find />
</#list>
</#if>

}