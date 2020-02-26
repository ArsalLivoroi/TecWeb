package it.unibo.tw;

import java.util.*;
import java.io.*;
import it.unibo.tw.db.*;

public class JDBCTest {
	
	static PrintWriter pw=null;
	
	private static WorkPackageRepository workPackageRepository;	
	private static WorkPackagePartnerMappingRepository workPackagePartnerMappingRepository;	
	private static ProgettoRepository progettoRepository;	
	private static PartnerRepository partnerRepository;	
	
	public static void main(String[] args) {
		
		// WorkPackage
		workPackageRepository = new WorkPackageRepository(DataSource.DB2);
		workPackageRepository.drop();
		workPackageRepository.createTable();
		
		WorkPackage workPackage;
		workPackage = new WorkPackage();
		workPackage.setIdWorkPackage(1);
		workPackage.setNomeWP("nomeWP_1");
		workPackage.setTitolo("titolo_1");
		workPackage.setDescrizione("descrizione_1");
		workPackageRepository.create(workPackage);
		
		workPackage = new WorkPackage();
		workPackage.setIdWorkPackage(2);
		workPackage.setNomeWP("nomeWP_2");
		workPackage.setTitolo("titolo_2");
		workPackage.setDescrizione("descrizione_2");
		workPackageRepository.create(workPackage);
		
		workPackage = new WorkPackage();
		workPackage.setIdWorkPackage(3);
		workPackage.setNomeWP("nomeWP_3");
		workPackage.setTitolo("titolo_3");
		workPackage.setDescrizione("descrizione_3");
		workPackageRepository.create(workPackage);
		
		
		// Progetto
		progettoRepository = new ProgettoRepository(DataSource.DB2);
		progettoRepository.drop();
		progettoRepository.createTable();
		
		Progetto progetto;
		progetto = new Progetto();
		progetto.setIdProgetto(1);
		progetto.setCodiceProgetto("codiceProgetto_1");
		progetto.setNomeProgetto("nomeProgetto_1");
		progetto.setAnnoInizio(1);
		progetto.setDurata(1);
		progettoRepository.create(progetto);
		
		progetto = new Progetto();
		progetto.setIdProgetto(2);
		progetto.setCodiceProgetto("codiceProgetto_2");
		progetto.setNomeProgetto("nomeProgetto_2");
		progetto.setAnnoInizio(2);
		progetto.setDurata(2);
		progettoRepository.create(progetto);
		
		progetto = new Progetto();
		progetto.setIdProgetto(3);
		progetto.setCodiceProgetto("codiceProgetto_3");
		progetto.setNomeProgetto("nomeProgetto_3");
		progetto.setAnnoInizio(3);
		progetto.setDurata(3);
		progettoRepository.create(progetto);
		
		
		// Partner
		partnerRepository = new PartnerRepository(DataSource.DB2);
		partnerRepository.drop();
		partnerRepository.createTable();
		
		Partner partner;
		partner = new Partner();
		partner.setIdPartner(1);
		partner.setSiglaPartner("siglaPartner_1");
		partner.setNome("nome_1");
		partnerRepository.create(partner);
		
		partner = new Partner();
		partner.setIdPartner(2);
		partner.setSiglaPartner("siglaPartner_2");
		partner.setNome("nome_2");
		partnerRepository.create(partner);
		
		partner = new Partner();
		partner.setIdPartner(3);
		partner.setSiglaPartner("siglaPartner_3");
		partner.setNome("nome_3");
		partnerRepository.create(partner);
		
		

		// WorkPackagePartnerMapping
		workPackagePartnerMappingRepository = new WorkPackagePartnerMappingRepository(DataSource.DB2);
		workPackagePartnerMappingRepository.drop();
		workPackagePartnerMappingRepository.createTable();
		workPackagePartnerMappingRepository.create(1,1);
		workPackagePartnerMappingRepository.create(1,2);
		workPackagePartnerMappingRepository.create(1,3);
		workPackagePartnerMappingRepository.create(2,1);
		workPackagePartnerMappingRepository.create(2,2);
		workPackagePartnerMappingRepository.create(2,3);
		workPackagePartnerMappingRepository.create(3,1);
		workPackagePartnerMappingRepository.create(3,2);
		workPackagePartnerMappingRepository.create(3,3);
		
		
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
		Set<WorkPackage> workPackages=new LinkedHashSet<WorkPackage>();	
		Set<Progetto> progetti=new LinkedHashSet<Progetto>();	
		Set<Partner> partners=new LinkedHashSet<Partner>();	

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