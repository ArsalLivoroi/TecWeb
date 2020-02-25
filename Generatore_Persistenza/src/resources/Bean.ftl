package it.unibo.tw<#if bean.nomeBean?contains("DTO")>.dao</#if>;

import java.io.Serializable;
<#if bean.riferimenti?has_content>
import java.util.*;
</#if>

public class ${bean.nomeBean?cap_first} implements Serializable{

	private static final long serialVersionUID = 1L;

<#list bean.primaryKeys as attributo>
	private ${attributo.tipo} ${attributo.nome?uncap_first};
</#list>
<#list bean.attributi as attributo>
	private ${attributo.tipo} ${attributo.nome?uncap_first};
</#list>


<#list bean.riferimenti as riferimento>
<#if riferimento.tipoRelazione=="1n" || riferimento.tipoRelazione=="nm" >
	private Set<${riferimento.to.nomeBean?cap_first}> ${riferimento.to.nomePlurale?uncap_first}; ${riferimento.commento}
	<#if riferimento.isLazyLoad && bean.nomeBean?contains("DTO")>
	private boolean lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded;
	</#if>
<#elseif riferimento.tipoRelazione=="n1" || riferimento.tipoRelazione=="11">
	private ${riferimento.to.nomeBean?cap_first} ${riferimento.to.nome?uncap_first};
</#if>
<#else>
</#list>
	
	// --- costruttore ----------

	public ${bean.nomeBean?cap_first}(){<#if !bean.riferimenti?has_content >}</#if>
<#list bean.riferimenti as riferimento>
<#if riferimento.tipoRelazione=="1n" || riferimento.tipoRelazione=="nm" >
		this.${riferimento.to.nomePlurale?uncap_first} = new HashSet<${riferimento.to.nomeBean?cap_first}>();
	<#if riferimento.isLazyLoad && bean.nomeBean?contains("DTO")>
		this.lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded=false;
	</#if>
<#elseif riferimento.tipoRelazione=="n1" || riferimento.tipoRelazione=="11">
		this.${riferimento.to.nome?uncap_first} = new ${riferimento.to.nomeBean?cap_first}();
</#if>	
</#list>
<#if bean.riferimenti?has_content >
	}
</#if>

	public ${bean.nomeBean?cap_first}(<#list bean.allAttributi as attributo>${attributo.tipo} ${attributo.nome?uncap_first}<#if attributo?has_next>, </#if></#list>){
		this();
<#list bean.allAttributi as attributo>
		this.${attributo.nome?uncap_first} = ${attributo.nome?uncap_first};
</#list>
	}

	// --- getters and setters --------------

<#list bean.allAttributi as attributo>
	public ${attributo.tipo} get${attributo.nome?cap_first}(){
		return ${attributo.nome?uncap_first};
	}
	public void set${attributo.nome?cap_first}(${attributo.tipo} ${attributo.nome?uncap_first}){
		this.${attributo.nome?uncap_first} = ${attributo.nome?uncap_first};
	}
</#list>

<#list bean.riferimenti as riferimento>
<#if riferimento.tipoRelazione=="1n" || riferimento.tipoRelazione=="nm" >
	public Set<${riferimento.to.nomeBean?cap_first}> get${riferimento.to.nomePlurale?cap_first}(){
		return ${riferimento.to.nomePlurale?uncap_first};
	}
	public void set${riferimento.to.nomePlurale?cap_first}(Set<${riferimento.to.nomeBean?cap_first}> ${riferimento.to.nomePlurale?uncap_first}){
		this.${riferimento.to.nomePlurale?uncap_first} = ${riferimento.to.nomePlurale?uncap_first};
	}
	<#if riferimento.isLazyLoad && bean.nomeBean?contains("DTO")>
	public boolean lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded(){
		return this.lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded;
	}
	public void lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded(boolean loaded){
		this.lista${riferimento.to.nomePlurale?cap_first}IsAlreadyLoaded = loaded;
	}
	</#if>
<#elseif riferimento.tipoRelazione=="n1" || riferimento.tipoRelazione=="11">
	public ${riferimento.to.nomeBean?cap_first} get${riferimento.to.nome?cap_first}(){
		return ${riferimento.to.nome?uncap_first};
	}
	public void set${riferimento.to.nome?cap_first}(${riferimento.to.nomeBean?cap_first} ${riferimento.to.nome?uncap_first}){
		this.${riferimento.to.nome?uncap_first} = ${riferimento.to.nome?uncap_first};
	}	
</#if>
</#list>

	// --- utilities ----------------------------
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		${bean.nomeBean?cap_first} other = (${bean.nomeBean?cap_first}) obj;
<#list bean.primaryKeys as primaryKey>
		if (this.${primaryKey.nome?uncap_first} != other.${primaryKey.nome?uncap_first})
			return false;
</#list>
		return true;
	}
	
	@Override
	public String toString() {		
		return "${bean.nome} {"+
			<#list bean.allAttributi as attributo>
			"${attributo.nome?uncap_first}="+ ${attributo.nome?uncap_first} +<#if attributo?has_next>","+</#if>
			</#list>
			"}";
	}
}