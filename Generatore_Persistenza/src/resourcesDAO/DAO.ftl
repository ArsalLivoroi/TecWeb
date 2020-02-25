package it.unibo.tw.dao;

import java.util.*;


public interface ${db2dao.nomeDAO}{
<#macro metodo_tlf mmq>
	public ${mmq.tipoRitorno} ${mmq.nomeMetodo}(${mmq.byAttributi});   
	
</#macro>
	
	// --- CRUD -------------------
	
<@metodo_tlf db2dao.insert />
	
<#if db2dao.haveUML>
<#list db2dao.reads as read>
<@metodo_tlf read />
</#list>
</#if>

<#if !db2dao.nome?contains("Mapping")>
<@metodo_tlf db2dao.update />
</#if>	
	
<#list db2dao.deletes as delete>
<@metodo_tlf delete />
</#list>

	// ---Table -------------------	
<@metodo_tlf db2dao.createtable />

<@metodo_tlf db2dao.droptable />

	// ----Find -------------------
<#if db2dao.finds?has_content>
<#list db2dao.finds as find>
<@metodo_tlf find />
</#list>
</#if>

}