package generatore.dao;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import generatore.global.Bean;
import generatore.global.Classe;
import generatore.global.Utils;
import generatore.problema.Attributo;
import generatore.problema.ClasseProblema;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public class GeneratoreDAO {
	private Set<ClasseProblema> classiProblema;
	private Set<MappingDAO> mapping;
	private Set<MetodoFind<DB2DAO>> proxyMapping;

	public GeneratoreDAO(Set<ClasseProblema> classiProblema) {
		proxyMapping = new LinkedHashSet<MetodoFind<DB2DAO>>();
		this.classiProblema=classiProblema;
		this.mapping=new HashSet<MappingDAO>();
		for(ClasseProblema c: classiProblema) {
			MappingDAO mp = new MappingDAO();
			mp.setClasseProblema(c);
			mapping.add(mp);
		}

		impostaBean();
		impostaDAO();
	}

	public MappingDAO get(Classe c) {
		MappingDAO res=null;
		for(MappingDAO mp: mapping) {
			if(mp.contiene(c)) {
				res=mp;
				break;
			}
		}
		return res;
	}

	public void generate() {
		impostaHaveUML();

		Utils.deleteFolder(new File("./out/it"));
		File outputDirectory = new File("./out/it/unibo/tw/dao/db2/");
		outputDirectory.mkdirs();
		//Utils.deleteFolder(new File("./test/it"));
		//File testDirectory = new File("./test/it/unibo/tw/dao/");
		//testDirectory.mkdirs();
		
		generaBean();
		generaDB2Dao();
		generaDAO();
		generaProxy();

		generaDB2DAOFactory();		
		generaDAOFactory();
		generaTest();
	}


	private void generaTest() {
		List<DB2DAO> daos = new LinkedList<DB2DAO>();
		for(MappingDAO mp : mapping) {
			DB2DAO db2Dao = mp.getDB2DAO();
			if(db2Dao != null)
				daos.add(db2Dao);
		}	
		File outputDirectory = new File("./out/it/unibo/tw/");
		String input = "./resourcesDAO/DAOTest.ftl";
		GeneratoreFileDAO_FTL GD;
		try {
			GD = new GeneratoreFileDAO_FTL();
			GD.generateJavaDAOFactory(daos, input, "DAOTest.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	//Factory--------
	private void generaDAOFactory() {
		List<DB2DAO> daos = new LinkedList<DB2DAO>();
		for(MappingDAO mp : mapping) {
			DB2DAO db2Dao = mp.getDB2DAO();
			if(db2Dao != null)
				daos.add(db2Dao);
		}	
		File outputDirectory = new File("./out/it/unibo/tw/dao/db2/");
		String input = "./resourcesDAO/DB2DAOFactory.ftl";
		GeneratoreFileDAO_FTL GD;
		try {
			GD = new GeneratoreFileDAO_FTL();
			GD.generateJavaDAOFactory(daos, input, "DB2DAOFactory.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generaDB2DAOFactory() {

		List<DB2DAO> daos = new LinkedList<DB2DAO>();
		for(MappingDAO mp : mapping) {
			DB2DAO db2Dao = mp.getDB2DAO();
			if(db2Dao != null)
				daos.add(db2Dao);
		}	
		File outputDirectory = new File("./out/it/unibo/tw/dao/");
		String input = "./resourcesDAO/DAOFactory.ftl";
		GeneratoreFileDAO_FTL GD;
		try {
			GD = new GeneratoreFileDAO_FTL();
			GD.generateJavaDAOFactory(daos, input, "DAOFactory.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//END Factory--------
	private void impostaHaveUML() {
		for(MappingDAO mp: mapping) {
			DB2DAO db = mp.getDB2DAO();
			if(db!=null) {
				//System.out.println("____"+);
				if(get(db).getBeanDTO()!=null) {
					db.setHaveUML(true);
				}else {
					db.setHaveUML(false);
				}
			}
		}
	}

	//-------DAO------------------

	private MappingDAO getMappingDB2Dao(String nome) {
		DB2DAO b=null;
		for(MappingDAO mp: mapping) {
			b=mp.getDB2DAO();
			if(b!=null && b.getNome().equals(nome)) {
				return mp;
			}
		}
		return null;
	}
	private boolean esisteDB2DAO(String nome) {
		boolean res=false;
		DB2DAO b=null;
		for(MappingDAO mp: mapping) {
			b=mp.getDB2DAO();
			if(b!=null && b.getNome().equals(nome)) {
				return true;
			}
		}
		return res;
	}

	private void generaDB2Dao() {
		File outputDirectory = new File("./out/it/unibo/tw/dao/db2/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resourcesDAO/DB2DAO.ftl";
		GeneratoreFileDAO_FTL GD;
		try {
			GD = new GeneratoreFileDAO_FTL();

			for(MappingDAO mp : mapping) {
				DB2DAO db2Dao = mp.getDB2DAO();
				if(db2Dao != null) {
					db2Dao.inizializzaMMQ();
					GD.generateJavaDB2DAOFiles(db2Dao, input, db2Dao.getNomeDB2DAO() + ".java", outputDirectory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generaDAO() {
		File outputDirectory = new File("./out/it/unibo/tw/dao/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resourcesDAO/DAO.ftl";
		GeneratoreFileDAO_FTL GD;
		try {
			GD = new GeneratoreFileDAO_FTL();
			for(MappingDAO mp : mapping) {
				DB2DAO db2Dao = mp.getDB2DAO();
				if(db2Dao != null)
					GD.generateJavaDB2DAOFiles(db2Dao, input, db2Dao.getNomeDAO() + ".java", outputDirectory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void impostaDAO() {
		for(ClasseProblema c: classiProblema) {
			DB2DAO db2dao = new DB2DAO(c.getNome(), c.getNomePlurale(), c.getNomeTabella());
			db2dao.setPrimaryKeys(c.getPrimaryKeys());
			db2dao.setAttributi(c.getAttributi());
			db2dao.setUnique(c.getUnique());


			if(db2dao.getPrimaryKeys().size()>1)
				for(Attributo atr: db2dao.getPrimaryKeys())
					db2dao.addMetodoFind(new MetodoFind<DB2DAO>(db2dao, db2dao, atr, false));
			for(Unique un: db2dao.getUnique()) {
				if(un.getAttributi().size()>1)
					for(Attributo atr: un.getAttributi())
						db2dao.addMetodoFind(new MetodoFind<DB2DAO>(db2dao, db2dao, atr, false));
			}
			for(Attributo atr: db2dao.getAttributiWithoutUnique())
				db2dao.addMetodoFind(new MetodoFind<DB2DAO>(db2dao, db2dao, atr, c.isLazyLoading()));

			this.get(c).setDB2DAO(db2dao);
		}

		Set<MappingDAO> map = new LinkedHashSet<MappingDAO>() ;
		map.addAll(mapping);

		for(MappingDAO mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			if(c!=null)
				for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
					Riferimento<DB2DAO> relazione = new Riferimento<DB2DAO>(mp.getDB2DAO(), this.get(r.getTo()).getDB2DAO(), r.getTipoRelazione(), r.getTipoFetch(), r.thereIsDirectReferences());
					switch (relazione.getTipoRelazione()) {
					case "1n":
						impostaDB2DAO1N(relazione );
						break;
					case "n1":
						impostaDB2DAON1(relazione );
						break;
					case "nm":
						impostaDB2DAONM(relazione);
						break;
					case "11":
						impostaDB2DAON1(relazione );
						break;	
					default :
						System.err.println("nome relazione errato!");
					}
				}
		}
	}
	

	private String nomeMapping(Classe from, Classe to) {
		return from.getNome() + to.getNome() +"Mapping";
	}


	private String nomeTabellaMapping(Classe from, Classe to) {
		return from.getNome() +"_"+ to.getNome() +"_Mapping";
	}

	private void impostaDB2DAONM(Riferimento<DB2DAO> relazione) {
		DB2DAO fromClasse = relazione.getFrom();
		DB2DAO toClasse = relazione.getTo();

		boolean esiste = (esisteDB2DAO(nomeMapping(fromClasse, toClasse)) || esisteDB2DAO(nomeMapping(toClasse, fromClasse)));

		DB2DAO classeMapping;
		MappingDAO mp;
		if(!esiste) {
			String nome = nomeMapping(fromClasse, toClasse);
			classeMapping = new DB2DAO(nome, nome, nomeTabellaMapping(fromClasse, toClasse));
			mp = (new MappingDAO());
			mp.setDB2DAO(classeMapping);
			mapping.add(mp);




		}else {
			mp = getMappingDB2Dao(nomeMapping(fromClasse, toClasse));
			if(mp == null)
				mp = getMappingDB2Dao(nomeMapping(toClasse, fromClasse));
			String nome = mp.getDB2DAO().getNome();
			classeMapping = new DB2DAO(nome, nome, nomeTabellaMapping(fromClasse, toClasse));
			mp.setDB2DAO(classeMapping);
		}

		for(Attributo a : fromClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);
		for(Attributo a : toClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);

		new Riferimento<DB2DAO>(classeMapping, toClasse, "n1", null, true);
		new Riferimento<DB2DAO>(classeMapping, fromClasse, "n1", null, true);

		//FIND
		//for(Attributo atr: fromClasse.getPrimaryKeys())
		MetodoFind<DB2DAO> mf = new MetodoFind<DB2DAO>(toClasse, fromClasse, fromClasse.getPrimaryKey(),relazione.getIsLazyLoad());
		mf.setPossessore(classeMapping);
		classeMapping.addMetodoFind(mf);
		//System.out.println(relazione.thereIsDirectReferences());
		if(relazione.thereIsDirectReferences())
			proxyMapping.add(mf);
		
		
		for(Unique un: fromClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<DB2DAO>(toClasse, fromClasse, atr,relazione.getIsLazyLoad()));

		for(Attributo atr: toClasse.getPrimaryKeys())
			classeMapping.addMetodoFind(new MetodoFind<DB2DAO>(fromClasse, toClasse, atr,relazione.getIsLazyLoad()));
		for(Unique un: toClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<DB2DAO>(fromClasse, toClasse, atr,relazione.getIsLazyLoad()));


		relazione.clear();
	}

	private void impostaDB2DAON1(Riferimento<DB2DAO> relazione) {
		if(!relazione.thereIsDirectReferences()) {
			Attributo a = relazione.getTo().getPrimaryKey();	
			relazione.getFrom().addAttributo(a);

			//#TODO  capire se serve la navigabilità diretta (tramite riferimento ) o basta l'id;
			relazione.clear();  
		}	
	}

	private void impostaDB2DAO1N(Riferimento<DB2DAO> relazione) {
		if(relazione.thereIsDirectReferences()) {
			//TODO attenzione: getPrimaryKey in caso di classi con più primarykey genererà un file che risulterà incorretto secondo le specifiche del es;
			MetodoFind<DB2DAO> mf = new MetodoFind<DB2DAO>(relazione.getTo(), relazione.getFrom(), relazione.getFrom().getPrimaryKey(),relazione.getIsLazyLoad());
			mf.setPossessore(relazione.getTo());
			relazione.getTo().addMetodoFind(mf);
			if(relazione.thereIsDirectReferences())
				proxyMapping.add(mf);
			//relazione.getFromClasse().addDb2References(relazione.getToClasse());
		}
	}
	//-----END DAO---------------------


	//-------BEAN------------------
	private void generaBean() {
		File outputDirectory = new File("./out/it/unibo/tw/dao/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resources/Bean.ftl";
		GeneratoreFileDAO_FTL GB;
		try {
			GB = new GeneratoreFileDAO_FTL();

			for(MappingDAO mp : mapping) {
				Bean bean = mp.getBeanDTO();
				if(bean != null)
					GB.generateJavaBeanFiles(bean, input, bean.getNomeBean() + ".java", outputDirectory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generaProxy() {
		Set<MetodoFind<DB2DAO>> metodiFind = new LinkedHashSet<MetodoFind<DB2DAO>>();
		for(MappingDAO mp : mapping) {
			DB2DAO db2dao = mp.getDB2DAO();
			if(db2dao != null)
				for(MetodoFind<DB2DAO> mf : db2dao.getMetodiFind()) {
					mf.setPossessore(db2dao);
					if(mf.getIsLazy())
						metodiFind.add(mf);
				}
		}


		//		Map<DB2DAO, Set<MetodoFind>> result = 
		//				mapping
		//				.stream()
		//				.filter(mp -> mp.getDB2DAO()!=null)
		//				.map(mp -> {mp.getDB2DAO();})
		//				.forEach();
		//				.collect(Collectors.groupingBy());

		//proxyMapping.stream().filter(MetodoFind::getIsLazy).forEach(mf->System.out.println(mf));
		Map<DB2DAO, Set<MetodoFind<DB2DAO>>> result =
				proxyMapping.stream().filter(MetodoFind<DB2DAO>::getIsLazy)
				.collect(Collectors.groupingBy(MetodoFind<DB2DAO>::getClasseSource, Collectors.toSet()));

		File outputDirectory = new File("./out/it/unibo/tw/dao/db2/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resourcesDAO/DB2DTOProxy.ftl";
		GeneratoreFileDAO_FTL GB;
		try {
			GB = new GeneratoreFileDAO_FTL();
			for(DB2DAO d: result.keySet())		
				GB.generateJavaProxyFiles(d, result.get(d), input, d.getNomeProxy() + ".java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void impostaBean() {
		for(ClasseProblema c: classiProblema) {
			Bean bean = new Bean(c.getNome(), c.getNomePlurale(), c.getNome()+"DTO");
			bean.setPrimaryKeys(c.getPrimaryKeys());
			bean.setAttributi(c.getAttributi());
			bean.setUnique(c.getUnique());
			this.get(c).setBeanDTO(bean);
		}

		Set<MappingDAO> map = new LinkedHashSet<MappingDAO>() ;
		map.addAll(mapping);

		for(MappingDAO mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
				Riferimento<Bean> relazione = new Riferimento<Bean>(mp.getBeanDTO(), this.get(r.getTo()).getBeanDTO(), r.getTipoRelazione(), r.getTipoFetch(), r.thereIsDirectReferences());
				switch (relazione.getTipoRelazione()) {
				case "1n":
					impostaBean1N(relazione );
					break;
				case "n1":
					impostaBeanN1(relazione );
					break;
				case "nm":
					impostaBeanNM(relazione);
					break;
				case "11":
					impostaBeanN1(relazione );
					break;	
				default :
					System.err.println("nome relazione errato!");
				}
			}
		}
	}

	private void impostaBeanNM(Riferimento<Bean> relazione) {
		Bean from = relazione.getFrom();
		Bean to = relazione.getTo();

		boolean esiste = (esisteBean(nomeMapping(from, to)) || esisteBean(nomeMapping(to, from)));

		if(!relazione.thereIsDirectReferences()) {
			if(!esiste) {
				String nomeClasseMapping = nomeMapping(from, to);
				Bean classeMapping = new Bean(nomeClasseMapping, nomeClasseMapping, nomeClasseMapping+"DTO");
				//classeMapping.isLazyLoading(false);
				MappingDAO mp = (new MappingDAO());
				mp.setBeanDTO(classeMapping);
				mapping.add(mp);

				for(Attributo a : from.getPrimaryKeys())
					classeMapping.addPrimaryKey(a);
				for(Attributo a : to.getPrimaryKeys())
					classeMapping.addPrimaryKey(a);
			}
			//Riferimento<Bean> from_to = new Riferimento<Bean>(from, to, "1n", relazione.getTipoFetch(), relazione.thereIsDirectReferences());
			relazione.clear();
		}	

	}

	private void impostaBeanN1(Riferimento<Bean> riferimento) {
		if(!riferimento.thereIsDirectReferences()) {
			Attributo a = riferimento.getTo().getPrimaryKey();	
			riferimento.getFrom().addAttributo(a);

			//#TODO  capire se serve la navigabilità diretta (tramite riferimento ) o basta l'id;
			riferimento.clear();  
		}			
	}

	private void impostaBean1N(Riferimento<Bean> relazione) {

	}
/*
	private MappingDAO getMappingBeanDao(String nome) {
		Bean b=null;
		for(MappingDAO mp: mapping) {
			b=mp.getBeanDTO();
			if(b!=null && b.getNome().equals(nome)) {
				return mp;
			}
		}
		return null;
	}
*/
	private boolean esisteBean(String nome) {
		boolean res=false;
		Bean b=null;
		for(MappingDAO mp: mapping) {
			b=mp.getBeanDTO();
			if(b!=null && b.getNome().equals(nome)) {
				return true;
			}
		}
		return res;
	}
	//-----END BEAN---------------------

}
