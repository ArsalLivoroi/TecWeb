package it.unibo.tw;

import it.unibo.tw.dao.db2.*;
import it.unibo.tw.dao.*;
import java.util.*;
import java.io.*;

public class DAOTest {
	
	static PrintWriter pw=null;
	
	public static final int DAO = DAOFactory.DB2;
	
<#list daos as classe>
	private static ${classe.nomeDAO} ${classe.nomeDAO?uncap_first};	
</#list>	
	
	public static void main(String[] args) {
	
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAO);
	<#-- POPOLA_TABELLE -->
<#macro popola_tabelle classe>
		// ${classe.nome}
		${classe.nomeDAO?uncap_first} = daoFactory.get${classe.nomeDAO}();
		${classe.nomeDAO?uncap_first}.drop();
		${classe.nomeDAO?uncap_first}.createTable();
		
		${classe.nomeBean} ${classe.nome?uncap_first};
<#list [1,2,3] as x>
		${classe.nome?uncap_first} = new ${classe.nomeBean}();
	<#list classe.primaryKeys as attributo>
		${classe.nome?uncap_first}.set${attributo.getNomeUp()}(${x});
	</#list>
	<#list classe.attributi as attributo>
		<#if attributo.tipo?contains("String")>
		${classe.nome?uncap_first}.set${attributo.nome?cap_first}("${attributo.nome}_${x}");
		<#elseif attributo.tipo?contains("int")>
		${classe.nome?uncap_first}.set${attributo.nome?cap_first}(${x});
		<#elseif attributo.tipo?contains("fload")||attributo.tipo?contains("long")||attributo.tipo?contains("double")>
		${classe.nome?uncap_first}.set${attributo.nome?cap_first}(${x}.0);
		<#elseif attributo.tipo?contains("Date")>
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		${classe.nome?uncap_first}.set${attributo.nome?cap_first}(format.parse ( "2009-${x}-${x+5}" ));
		<#else>
		${classe.nome?uncap_first}.set${attributo.nome?cap_first}(${attributo.tipo}_${x});
		</#if>
	</#list>
		${classe.nomeDAO?uncap_first}.create(${classe.nome?uncap_first});
		
</#list>
		
</#macro>
		
<#list daos as classe>
<#if !classe.nome?contains("Mapping")>
<@popola_tabelle classe/>
</#if>
</#list>

	<#-- POPOLA_TABELLE_MAPPING -->
<#macro popola_tabelle_mapping classe>
		// ${classe.nome}
		${classe.nomeDAO?uncap_first} = daoFactory.get${classe.nomeDAO}();
		${classe.nomeDAO?uncap_first}.drop();
		${classe.nomeDAO?uncap_first}.createTable();
	<#list [1,2,3] as x>
	<#list [1,2,3] as y>
		${classe.nomeDAO?uncap_first}.create(${x},${y});
	</#list>
	</#list>
		
</#macro>
<#list daos as classe>
<#if classe.nome?contains("Mapping")>
<@popola_tabelle_mapping classe/>
</#if>
</#list>
		<#-- METODI_RICHIESTI -->
		
		//Scrivi su file
		try {
			pw = new PrintWriter(new FileWriter(NOME_FILE));
			
			
			// METODO_RICHIESTO_1
			String outPut = METODO_RICHIESTO1();
			pw.println(outPut);
			System.out.println(outPut + "\n");
			System.out.println();

			// METODO_RICHIESTO_2
			String outPut = METODO_RICHIESTO2();
			pw.println(outPut);
			System.out.println(outPut + "\n");
			System.out.println();

			
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String METODO_RICHIESTO(){
	/*
			Set<ProgettoDTO> progetti = new LinkedHashSet<ProgettoDTO>();
			Set<WorkPackageDTO> WorkPackages=new LinkedHashSet<WorkPackageDTO>();
			Set<PartnerDTO> partners=new LinkedHashSet<PartnerDTO>();
			
			progetti = progettoDAO.findProgettiByDurata(3);
			for(ProgettoDTO p : progetti)
				WorkPackages.addAll(workPackageDAO.findWorkPackagesByIdProgetto(p.getIdProgetto()));
			for(WorkPackageDTO wp: WorkPackages)
				partners.addAll(workPackagePartnerMappingDAO.findPartnersByIdWorkPackage(wp.getIdWorkPackage()));
			
			StringBuilder result = new StringBuilder();
			for(PartnerDTO p:partners)
				result.append(p+"\n");
				
				
			//oppure
		Set<WorkPackageDTO> workPackages=new LinkedHashSet<WorkPackageDTO>();	
		Set<ProgettoDTO> progetti=new LinkedHashSet<ProgettoDTO>();	
		Set<PartnerDTO> partners=new LinkedHashSet<PartnerDTO>();	

		partners = partnerDAO.findPartnersBySiglaPartner("UNIBO");
		Set<WorkPackageDTO> ls = partners.stream()
		.map(p->workPackagePartnerMappingDAO.findWorkPackagesByIdPartner(p.getIdPartner()))
		.collect(LinkedHashSet<WorkPackageDTO>::new, Set<WorkPackageDTO>::addAll, Set<WorkPackageDTO>::addAll);
		
		Map<String, Long> a = ls.stream()
		.map(m->progettoDAO.readByIdProgetto(m.getIdProgetto()))
		.collect(Collectors.toSet())
		.stream()
		.collect(Collectors.groupingBy(ProgettoDTO::getNomeProgetto,Collectors.counting()));
				
	*/
<#list daos as classe>
<#if !classe.nome?contains("Mapping")>
		Set<${classe.nomeBean}> ${classe.nomePlurale?uncap_first}=new LinkedHashSet<${classe.nomeBean}>();	
</#if>
</#list>

		CLASSE_SOURCE_PLURALE = CLASSE_SOURCE.FIND_CLASSE_SOURCE_BY_ATTRIBUTO_SOURCE(VALORE_ATTRIBUTO);
		for(CLASSE_SOURCE NOME1 : CLASSE_SOURCE_PLURALE)
				CLASSE_TARGET1_PLURALE.addAll(CLASSE_TARGET1_DAO.FIND_CLASSE_TARGET1_BY_ATTRIBUTO_TARGET1(NOME1.getID()));
		for(CLASSE_SOURCE NOME2 : CLASSE_SOURCE_PLURALE)
				CLASSE_TARGET2_PLURALE.addAll(CLASSE_TARGET2_DAO.FIND_CLASSE_TARGET2_BY_ATTRIBUTO_TARGET2(NOME2.getID()));
				
		StringBuilder result = new StringBuilder();
			for(PartnerDTO p:partners)
				result.append(p+"\n");
		
		return result.toString();
	}
}