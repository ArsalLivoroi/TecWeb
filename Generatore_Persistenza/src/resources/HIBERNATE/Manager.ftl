package it.unibo.tw;


import java.sql.*;
import java.util.*;

import org.hibernate.*; 
import org.hibernate.cfg.Configuration;

public class ${manager.nomeManager}{
	private Connection connection;
	private SessionFactory factory;
	static final String TABLE = "${manager.nomeTabella}";
<#-- 
<#list manager.attributi as attributo>
	static final String ${attributo.nomeColumn?upper_case}= "${attributo.nomeColumn}";
</#list>
 -->	

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = 
		" CREATE " +
			" TABLE " + TABLE +" ( " +
<#list manager.primaryKeys as primaryKey>
				"${primaryKey.nomeColumn} ${primaryKey.tipoDB?upper_case} NOT NULL, "+
</#list>
<#--  
<#list manager.unique as unique>
	<#list unique.attributi as attributo>
				"${attributo.nomeColumn} ${attributo.tipoDB?upper_case} NOT NULL, "+
	</#list>			
</#list>
-->
<#list manager.attributi as attributo>
				"${attributo.nomeColumn} ${attributo.tipoDB?upper_case} NOT NULL, "+
</#list>
<#list manager.riferimenti as riferimento>
<#if riferimento?contains("n1")||riferimento?contains("11")>
<#if riferimento.thereIsDirectReferences && !manager.nome?contains("Mapping")>
				"${riferimento.to.primaryKey.nomeColumn} ${riferimento.to.primaryKey.tipoDB?upper_case} NOT NULL, "+
</#if>
				"FOREING KEY(<#list riferimento.to.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>,</#if></#list>) REFERENCES ${riferimento.to.nomeTabella}(<#list riferimento.to.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>, </#if></#list>) "+
</#if>
</#list>
<#list manager.unique as unique>
				"UNIQUE(<#list unique.attributi as attributo>${attributo.nomeColumn}<#if attributo?has_next>, </#if></#list>), "+
</#list>				
				"PRIMARY KEY(<#list manager.primaryKeys as primaryKey>${primaryKey.nomeColumn}<#if primaryKey?has_next>, </#if></#list>) " +
			") ";

	static String drop = 
		"DROP TABLE " + TABLE ;
		
	
	// delete
	static String delete_by_<#list manager.primaryKeys as attributo>${attributo.nome}<#if attributo?has_next>_</#if></#list> = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE <#list manager.primaryKeys as attributo>${attributo.nomeColumn} = ?<#if attributo?has_next> AND </#if></#list>";

<#list manager.unique as unique>
	static String delete_by_<#list unique.attributi as attributo>${attributo.nome}<#if attributo?has_next>_</#if></#list> = 
			"DELERE " +
				"FROM " + TABLE + " " +
				"WHERE <#list unique.attributi as attributo>${attributo.nomeColumn} = ?<#if attributo?has_next> AND </#if></#list>";
				
</#list>
		
	// QUERY ----		
<#list manager.metodiFind as metodoFind>
	static final String find_${metodoFind.classeTarget.nomeTabella}_by_${metodoFind.attributo.nome} = 
			" SELECT * "+
			<#if manager.nomeTabella!=metodoFind.classeTarget.nomeTabella>
			" FROM "+ TABLE + ", ${metodoFind.classeTarget.nomeTabella} "+
			" WHERE ${manager.nomeTabella}.${metodoFind.classeTarget.primaryKey.nomeColumn} = ${metodoFind.classeTarget.nomeTabella}.${metodoFind.classeTarget.primaryKey.nomeColumn}"+
			" AND ${metodoFind.attributo.nomeColumn} = ? ";
			<#else>
			" FROM "+ TABLE + 
			" WHERE ${metodoFind.attributo.nomeColumn} = ? ";
			</#if>
			
</#list>

 <#-- -->
 
	public  ${manager.nomeManager}(Connection connection) {
		this.connection = connection;
		this.factory = new Configuration().configure().buildSessionFactory();;
	} 

<#macro metodo_tlf mmq>
	public ${mmq.tipoRitorno} ${mmq.nomeMetodo}(${mmq.byAttributi}){
		<#list mmq.m1 as s>
		${s}
		</#list>
		<#list mmq.m2 as s>
		${s}
		</#list>
		try { 
		<#list mmq.m3 as s>
			${s}
		</#list>           
			<#list mmq.m4a as s>
			${s}
			</#list>    
			<#list mmq.m4b as s>
			${s}
			</#list>           
			<#list mmq.m4c as s>
			${s}
			</#list>            
			<#list mmq.m4d as s>
			${s}
			</#list>    
			<#list mmq.m4e as s>
			${s}
			</#list>   
			<#list mmq.m4f as s>
			${s}
			</#list>
		}        
		catch (Exception e) {      
			<#list mmq.m5 as s>
			${s}
			</#list>
         	e.printStackTrace();
		}        
		finally {  
		<#list mmq.m6 as s>
			${s}
		</#list>
		}         
		<#list mmq.m7 as s>
		${s}
		</#list>
	}
	
</#macro>

<#if !manager.nome?contains("Mapping")>	
	//Create
<@metodo_tlf manager.insert />

	//Update
<@metodo_tlf manager.update />
</#if>	

	//Create Table
<@metodo_tlf manager.createtable />

	//Drop Table
<@metodo_tlf manager.droptable />
	
<#--   
<#if manager.haveUML>
	//Read By
<#list manager.reads as read>
<@metodo_tlf read />
</#list>
</#if>
-->

	//Delete
<#list manager.deletes as delete>
<@metodo_tlf delete />
</#list>


<#if manager.finds?has_content>
	//Find By
<#list manager.finds as find>
	@SuppressWarnings("unchecked")
<@metodo_tlf find />
</#list>
</#if>
}