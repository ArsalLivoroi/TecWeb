package it.unibo.tw.dao.db2;

import java.util.*;
import it.unibo.tw.dao.*;

public class ${source.nomeProxy} extends ${source.nomeBean} {

	private static final long serialVersionUID = 1L;

	public ${source.nomeProxy}() {
		super();
	}
	
<#list metodiFind as metodoFind>
	<#if metodoFind.isLazy>
	@Override
	public Set<${metodoFind.classeTarget.nomeBean?cap_first}> get${metodoFind.classeTarget.nomePlurale?cap_first}(){
		if(!lista${metodoFind.classeTarget.nomePlurale?cap_first}IsAlreadyLoaded()){
			${metodoFind.possessore.nomeDAO?cap_first} ${metodoFind.possessore.nome?uncap_first} = new ${metodoFind.possessore.nomeDB2DAO}();
			lista${metodoFind.classeTarget.nomePlurale?cap_first}IsAlreadyLoaded(true);
			super.set${metodoFind.classeTarget.nomePlurale?cap_first}(${metodoFind.possessore.nome?uncap_first}.find${metodoFind.classeTarget.nomePlurale?cap_first}By${metodoFind.classeSource.primaryKey.nome?cap_first}(this.get${metodoFind.classeSource.primaryKey.nome?cap_first}()));
		}			
		return super.get${metodoFind.classeTarget.nomePlurale?cap_first}();
	}
	
	</#if>
</#list>
}