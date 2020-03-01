package it.unibo.tw;

import it.unibo.tw.dao.db2.*;
import it.unibo.tw.dao.*;
import java.util.*;
import java.io.*;

public class DAOTest {
	
	static PrintWriter pw=null;
	
	public static final int DAO = DAOFactory.DB2;
	
	private static WorkPackageDAO workPackageDAO;	
	private static WorkPackagePartnerMappingDAO workPackagePartnerMappingDAO;	
	private static ProgettoDAO progettoDAO;	
	private static PartnerDAO partnerDAO;	
	
	public static void main(String[] args) {
	
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAO);
		
		// WorkPackage
		workPackageDAO = daoFactory.getWorkPackageDAO();
		workPackageDAO.drop();
		workPackageDAO.createTable();
		
		WorkPackageDTO workPackage;
		Set<WorkPackageDTO> workPackages = new HashSet<WorkPackageDTO>();
		workPackage = new WorkPackageDTO();
		workPackage.setIdWorkPackage(1);
		workPackage.setNomeWP("nomeWP_1");
		workPackage.setTitolo("titolo_1");
		workPackage.setDescrizione("descrizione_1");
		workPackageDAO.create(workPackage);
		workPackages.add(workPackage);
		
		workPackage = new WorkPackageDTO();
		workPackage.setIdWorkPackage(2);
		workPackage.setNomeWP("nomeWP_2");
		workPackage.setTitolo("titolo_2");
		workPackage.setDescrizione("descrizione_2");
		workPackageDAO.create(workPackage);
		workPackages.add(workPackage);
		
		workPackage = new WorkPackageDTO();
		workPackage.setIdWorkPackage(3);
		workPackage.setNomeWP("nomeWP_3");
		workPackage.setTitolo("titolo_3");
		workPackage.setDescrizione("descrizione_3");
		workPackageDAO.create(workPackage);
		workPackages.add(workPackage);
		
		
		// Progetto
		progettoDAO = daoFactory.getProgettoDAO();
		progettoDAO.drop();
		progettoDAO.createTable();
		
		ProgettoDTO progetto;
		Set<ProgettoDTO> progetti = new HashSet<ProgettoDTO>();
		progetto = new ProgettoDTO();
		progetto.setIdProgetto(1);
		progetto.setCodiceProgetto("codiceProgetto_1");
		progetto.setNomeProgetto("nomeProgetto_1");
		progetto.setAnnoInizio(1);
		progetto.setDurata(1);
		progettoDAO.create(progetto);
		progetti.add(progetto);
		
		progetto = new ProgettoDTO();
		progetto.setIdProgetto(2);
		progetto.setCodiceProgetto("codiceProgetto_2");
		progetto.setNomeProgetto("nomeProgetto_2");
		progetto.setAnnoInizio(2);
		progetto.setDurata(2);
		progettoDAO.create(progetto);
		progetti.add(progetto);
		
		progetto = new ProgettoDTO();
		progetto.setIdProgetto(3);
		progetto.setCodiceProgetto("codiceProgetto_3");
		progetto.setNomeProgetto("nomeProgetto_3");
		progetto.setAnnoInizio(3);
		progetto.setDurata(3);
		progettoDAO.create(progetto);
		progetti.add(progetto);
		
		
		// Partner
		partnerDAO = daoFactory.getPartnerDAO();
		partnerDAO.drop();
		partnerDAO.createTable();
		
		PartnerDTO partner;
		Set<PartnerDTO> partners = new HashSet<PartnerDTO>();
		partner = new PartnerDTO();
		partner.setIdPartner(1);
		partner.setSiglaPartner("siglaPartner_1");
		partner.setNome("nome_1");
		partnerDAO.create(partner);
		partners.add(partner);
		
		partner = new PartnerDTO();
		partner.setIdPartner(2);
		partner.setSiglaPartner("siglaPartner_2");
		partner.setNome("nome_2");
		partnerDAO.create(partner);
		partners.add(partner);
		
		partner = new PartnerDTO();
		partner.setIdPartner(3);
		partner.setSiglaPartner("siglaPartner_3");
		partner.setNome("nome_3");
		partnerDAO.create(partner);
		partners.add(partner);
		
		

		// WorkPackagePartnerMapping
		workPackagePartnerMappingDAO = daoFactory.getWorkPackagePartnerMappingDAO();
		workPackagePartnerMappingDAO.drop();
		workPackagePartnerMappingDAO.createTable();
		workPackagePartnerMappingDAO.create(1,1);
		workPackagePartnerMappingDAO.create(1,2);
		workPackagePartnerMappingDAO.create(1,3);
		workPackagePartnerMappingDAO.create(2,1);
		workPackagePartnerMappingDAO.create(2,2);
		workPackagePartnerMappingDAO.create(2,3);
		workPackagePartnerMappingDAO.create(3,1);
		workPackagePartnerMappingDAO.create(3,2);
		workPackagePartnerMappingDAO.create(3,3);
		

		for(ProgettoDTO progetto1:  progetti){
			progetto1.setWorkPackages(workPackages);
			progettoDAO.update(progetto1);
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
		Set<WorkPackageDTO> workPackages=new LinkedHashSet<WorkPackageDTO>();	
		Set<ProgettoDTO> progetti=new LinkedHashSet<ProgettoDTO>();	
		Set<PartnerDTO> partners=new LinkedHashSet<PartnerDTO>();	

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