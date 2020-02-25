<?xml version="1.0"?>

<!DOCTYPE hibernate-mapping PUBLIC
"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
	<class name="it.unibo.tw.beans.${manager.nomeBean}" table="${manager.nomeTabella}">

<#if manager.primaryKeys?size gt 1>
		<composite-id>
	<#list manager.primaryKeys as attributo>
			<key-property name="${attributo.nome}" column="${attributo.nomeColumn}" />
	</#list>
		</composite-id>
<#else>
		<id name="${manager.primaryKey.nome}" column="${manager.primaryKey.nomeColumn}">
		<#if manager.primaryKey.nome?contains("id")>
			<generator class="increment"/>
		<#else>
			<generator class="native"/>	
		</#if>
		</id>
</#if>


<#list manager.unique as unique>
	<#if (unique.attributi?size > 1)>
		<properties name="${unique.attributi[0].nome}" unique="true"  >
		<#list unique.attributi as attributo>
			<property name="${attributo.nome}" column="${attributo.nomeColumn}" not-null="true"/>
		</#list>
		</properties>
	<#elseif unique.attributi?has_content>
		<property  name="${unique.attributi[0].nome}" column="${unique.attributi[0].nomeColumn}" unique="true" not-null="true"/>
	</#if>
</#list>

<#list manager.attributiWithoutUnique as attributo>
		<property name="${attributo.nome}" column="${attributo.nomeColumn}" not-null="true"/>
</#list>	

<#list manager.riferimenti as riferimento>
	<#if riferimento.tipoRelazione?contains("1n")>
		<set name="${riferimento.to.nomePlurale?uncap_first}" inverse="true" lazy="${riferimento.isLazyLoad?c}" fetch="select" >
			<key column="${riferimento.from.primaryKey.nomeColumn}" />
			<one-to-many class="it.unibo.tw.beans.${riferimento.to.nome}" />
		</set>
	<#elseif riferimento.tipoRelazione?contains("n1")>
		<many-to-one name="${riferimento.to.primaryKey.nome}" class="it.unibo.tw.beans.${riferimento.to.nome}" column="${riferimento.to.primaryKey.nomeColumn}" />
	<#elseif riferimento.tipoRelazione?contains("nm")>
		<set name="${riferimento.to.nomePlurale?uncap_first}" inverse="true" lazy="${riferimento.isLazyLoad?c}" fetch="select" >
			<key column="${riferimento.from.primaryKey.nomeColumn}" />
			<many-to-many column="${riferimento.to.primaryKey.nomeColumn}" class="it.unibo.tw.beans.${riferimento.to.nome}" />
		</set>
	<#elseif riferimento.tipoRelazione?contains("11")>
	</#if>
</#list>
		
	</class>
</hibernate-mapping>