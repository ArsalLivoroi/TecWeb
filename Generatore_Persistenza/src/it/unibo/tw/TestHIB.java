package it.unibo.tw;

import java.util.*;
import java.io.*;
import it.unibo.tw.db.*;
import java.sql.*;

public class TestHIB {
	
	static PrintWriter pw=null;
	
	private static PiattoProvaManager piattoProvaManager;	
	private static IngredienteProvaManager ingredienteProvaManager;	
	
	public static void main(String[] args) {

		Class.forName("com.ibm.db2.jcc.DB2Driver");
		String url = "jdbc:db2://diva.deis.unibo.it:50000/tw_stud";
		
		String username = "INSERISCI_MATRICOLA_FORMATO_00123456";
		String password = "INSERISCI_QUI_LA_TUA_PASSWORD_DI_DB2";
		Connection conn = DriverManager.getConnection(url, username, password);
		
		
		// PiattoProva
		piattoProvaManager = new PiattoProvaManager(conn);
		piattoProvaManager.drop();
		piattoProvaManager.createTable();
		
		PiattoProva piattoProva;
		Set<PiattoProva> piattiProva = new HashSet<PiattoProva>();
		piattoProva = new PiattoProva();
		piattoProva.setIdPiattoProva(1);
		piattoProva.setNomePiatto("nomePiatto_1");
		piattoProva.setClassificazionePiatto("classificazionePiatto_1");
		piattoProva.setCalorie(1);
		piattoProvaManager.insert(piattoProva);
		piattiProva.add(piattoProva);
		
		piattoProva = new PiattoProva();
		piattoProva.setIdPiattoProva(2);
		piattoProva.setNomePiatto("nomePiatto_2");
		piattoProva.setClassificazionePiatto("classificazionePiatto_2");
		piattoProva.setCalorie(2);
		piattoProvaManager.insert(piattoProva);
		piattiProva.add(piattoProva);
		
		piattoProva = new PiattoProva();
		piattoProva.setIdPiattoProva(3);
		piattoProva.setNomePiatto("nomePiatto_3");
		piattoProva.setClassificazionePiatto("classificazionePiatto_3");
		piattoProva.setCalorie(3);
		piattoProvaManager.insert(piattoProva);
		piattiProva.add(piattoProva);
		
		
		// IngredienteProva
		ingredienteProvaManager = new IngredienteProvaManager(conn);
		ingredienteProvaManager.drop();
		ingredienteProvaManager.createTable();
		
		IngredienteProva ingredienteProva;
		Set<IngredienteProva> ingredientiprova = new HashSet<IngredienteProva>();
		ingredienteProva = new IngredienteProva();
		ingredienteProva.setIdIngredienteProva(1);
		ingredienteProva.setNomeIngrediente("nomeIngrediente_1");
		ingredienteProva.setQuantita(1);
		ingredienteProvaManager.insert(ingredienteProva);
		ingredientiprova.add(ingredienteProva);
		
		ingredienteProva = new IngredienteProva();
		ingredienteProva.setIdIngredienteProva(2);
		ingredienteProva.setNomeIngrediente("nomeIngrediente_2");
		ingredienteProva.setQuantita(2);
		ingredienteProvaManager.insert(ingredienteProva);
		ingredientiprova.add(ingredienteProva);
		
		ingredienteProva = new IngredienteProva();
		ingredienteProva.setIdIngredienteProva(3);
		ingredienteProva.setNomeIngrediente("nomeIngrediente_3");
		ingredienteProva.setQuantita(3);
		ingredienteProvaManager.insert(ingredienteProva);
		ingredientiprova.add(ingredienteProva);
		
		

		for(PiattoProva piattoProva1:  piattiProva){
			piattoProva1.setIngredientiprova(ingredientiprova);
			piattoProvaManager.update(piattoProva1);
		}	
		for(IngredienteProva ingredienteProva1:  ingredientiprova){
			ingredienteProva1.setPiattiProva(piattiProva);
			ingredienteProvaManager.update(ingredienteProva1);
		}	
		
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
		Set<PiattoProva> piattiProva=new LinkedHashSet<PiattoProva>();	
		Set<IngredienteProva> ingredientiprova=new LinkedHashSet<IngredienteProva>();	

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