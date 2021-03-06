package generatore.hibernare;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import generatore.dao.DB2DAO;
import generatore.dao.MappingDAO;
import generatore.global.Bean;
import generatore.global.Classe;
import generatore.global.Utils;
import generatore.problema.Attributo;
import generatore.problema.ClasseProblema;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public class GeneratoreHIBERNATE {
	private Set<ClasseProblema> classiProblema;
	private Set<MappingHIBERNATE> mapping;

	public GeneratoreHIBERNATE(Set<ClasseProblema> classiProblema) {
		//proxyMapping = new LinkedHashSet<MetodoFind>();
		this.classiProblema=classiProblema;
		this.mapping=new HashSet<MappingHIBERNATE>();
		for(ClasseProblema c: classiProblema) {
			MappingHIBERNATE mp = new MappingHIBERNATE();
			mp.setClasseProblema(c);
			mapping.add(mp);
		}

		impostaBean();
		impostaHIBERNATE();
	}

//	private String nomeMapping(Classe from, Classe to) {
//		return from.getNome() + to.getNome() +"Mapping";
//	}
	
	//-------Manager------------------
	
	private void generaManager() {
		File outputDirectory = new File("./out/it/unibo/tw/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resources/HIBERNATE/Manager.ftl";
		GeneratoreFileHIBERNATE_FTL GD;
		try {
			GD = new GeneratoreFileHIBERNATE_FTL();

			for(MappingHIBERNATE mp : mapping) {
				Manager manager = mp.getManager();
				if(manager != null) {
					manager.inizializzaMMQ();
					GD.generateJavaManagerFiles(manager, input, manager.getNomeManager() + ".java", outputDirectory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void impostaHIBERNATE() {
		for(ClasseProblema c: classiProblema) {
			Manager rep = new Manager(c.getNome(), c.getNomePlurale(), c.getNomeTabella());
			rep.setPrimaryKeys(c.getPrimaryKeys());
			rep.setAttributi(c.getAttributi());
			rep.setUnique(c.getUnique());


			if(rep.getPrimaryKeys().size()>1)
				for(Attributo atr: rep.getPrimaryKeys())
					rep.addMetodoFind(new MetodoFind<Manager>(rep, rep, atr, false));
			for(Unique un: rep.getUnique()) {
				if(un.getAttributi().size()>1)
					for(Attributo atr: un.getAttributi())
						rep.addMetodoFind(new MetodoFind<Manager>(rep, rep, atr, false));
			}
			for(Attributo atr: rep.getAttributiWithoutUnique())
				rep.addMetodoFind(new MetodoFind<Manager>(rep, rep, atr, false));

			this.get(c).setManager(rep);
		}

		Set<MappingHIBERNATE> map = new LinkedHashSet<MappingHIBERNATE>() ;
		map.addAll(mapping);

		for(MappingHIBERNATE mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			if(c!=null)
				for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
					Riferimento<Manager> relazione = new Riferimento<Manager>(mp.getManager(), this.get(r.getTo()).getManager(), r.getTipoRelazione(), r.getTipoFetch(), r.getThereIsDirectReferences());
					switch (relazione.getTipoRelazione()) {
					case "1n":
						impostaManager1N(relazione );
						break;
					case "n1":
						impostaManagerN1(relazione );
						break;
					case "nm":
						impostaManagerNM(relazione);
						break;
					case "11":
						impostaManagerN1(relazione );
						break;	
					default :
						System.err.println("nome relazione errato!");
					}
				}
		}
		
	}
	
	
	private void impostaManagerN1(Riferimento<Manager> relazione) {
		
		
	}

	private MappingHIBERNATE getMappingHIBERNATE(String nome) {
		Manager b=null;
		for(MappingHIBERNATE mp: mapping) {
			b=mp.getManager();
			if(b!=null && b.getNome().equals(nome)) {
				return mp;
			}
		}
		return null;
	}
	
	
	private boolean esisteManager(String nome) {
		boolean res=false;
		Manager b=null;
		for(MappingHIBERNATE mp: mapping) {
			b=mp.getManager();
			if(b!=null && b.getNome().equals(nome)) {
				return true;
			}
		}
		return res;
	}
	
	private String nomeMapping(Classe from, Classe to) {
		return from.getNome() + to.getNome() +"Mapping";
	}


	private String nomeTabellaMapping(Classe from, Classe to) {
		return from.getNome() +"_"+ to.getNome();// +"_Mapping";
	}

	private void impostaManagerNM(Riferimento<Manager> relazione) {
		//TODO creare le tamelle di mapping
//		MetodoFind<Manager> mf = new MetodoFind<Manager>(relazione.getTo(), relazione.getFrom(), relazione.getFrom().getPrimaryKey(),false);
//		mf.setPossessore(relazione.getTo());
//		relazione.getTo().addMetodoFind(mf);
		
		
		
		Manager fromClasse = relazione.getFrom();
		Manager toClasse = relazione.getTo();

		boolean esiste = (esisteManager(nomeMapping(fromClasse, toClasse)) || esisteManager(nomeMapping(toClasse, fromClasse)));

		Manager classeMapping;
		MappingHIBERNATE mp;
		String nomeTabella;
		if(!esiste) {
			String nome = nomeMapping(fromClasse, toClasse);
			nomeTabella = nomeTabellaMapping(fromClasse, toClasse);
			classeMapping = new Manager(nome, nome, nomeTabella);
			mp = (new MappingHIBERNATE());
			mp.setManager(classeMapping);
			mapping.add(mp);
		}else {
			mp = getMappingHIBERNATE(nomeMapping(fromClasse, toClasse));
			if(mp == null)
				mp = getMappingHIBERNATE(nomeMapping(toClasse, fromClasse));
			String nome = mp.getManager().getNome();
			nomeTabella = mp.getManager().getNomeTabella();
			classeMapping = new Manager(nome, nome, nomeTabella);
			mp.setManager(classeMapping);
		}
		relazione.setNomeTabella(nomeTabella);

		for(Attributo a : fromClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);
		for(Attributo a : toClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);

		new Riferimento<Manager>(classeMapping, toClasse, "n1", null, true);
		new Riferimento<Manager>(classeMapping, fromClasse, "n1", null, true);

		//FIND
		//for(Attributo atr: fromClasse.getPrimaryKeys())
		MetodoFind<Manager> mf = new MetodoFind<Manager>(toClasse, fromClasse, fromClasse.getPrimaryKey(),relazione.getIsLazyLoad());
		mf.setPossessore(classeMapping);
		classeMapping.addMetodoFind(mf);
		//System.out.println(relazione.thereIsDirectReferences());
//		if(relazione.getThereIsDirectReferences())
//			proxyMapping.add(mf);
		
		
		for(Unique un: fromClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<Manager>(toClasse, fromClasse, atr,relazione.getIsLazyLoad()));

		for(Attributo atr: toClasse.getPrimaryKeys())
			classeMapping.addMetodoFind(new MetodoFind<Manager>(fromClasse, toClasse, atr,relazione.getIsLazyLoad()));
		for(Unique un: toClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<Manager>(fromClasse, toClasse, atr,relazione.getIsLazyLoad()));

		//relazione.clear();
	}



	private void impostaManager1N(Riferimento<Manager> relazione) {
			//TODO attenzione: getPrimaryKey in caso di classi con pi� primarykey generer� un file che risulter� incorretto secondo le specifiche del es;
			MetodoFind<Manager> mf = new MetodoFind<Manager>(relazione.getTo(), relazione.getFrom(), relazione.getFrom().getPrimaryKey(),false);
			mf.setPossessore(relazione.getTo());
			relazione.getTo().addMetodoFind(mf);

	}

	
	//-------END Manager------------------

	
	
	private void impostaHaveUML() {
		for(MappingHIBERNATE mp: mapping) {
			Manager db = mp.getManager();
			if(db!=null) {
				//System.out.println("____"+);
				if(get(db).getBean()!=null) {
					db.setHaveUML(true);
				}else {
					db.setHaveUML(false);
				}
			}
		}
	}
	
	//-------BEAN------------------
	private void generaBean() {
		File outputDirectory = new File("./out/it/unibo/tw/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resources/Bean.ftl";
		GeneratoreFileHIBERNATE_FTL GB;
		try {
			GB = new GeneratoreFileHIBERNATE_FTL();

			for(MappingHIBERNATE mp : mapping) {
				Bean bean = mp.getBean();
				if(bean != null)
					GB.generateJavaBeanFiles(bean, input, bean.getNomeBean() + ".java", outputDirectory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void impostaBean() {
		for(ClasseProblema c: classiProblema) {
			Bean bean = new Bean(c.getNome(), c.getNomePlurale(), c.getNome());
			bean.setPrimaryKeys(c.getPrimaryKeys());
			bean.setAttributi(c.getAttributi());
			bean.setUnique(c.getUnique());
			this.get(c).setBean(bean);
		}

		Set<MappingHIBERNATE> map = new LinkedHashSet<MappingHIBERNATE>() ;
		map.addAll(mapping);

		for(MappingHIBERNATE mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
				Riferimento<Bean> relazione = new Riferimento<Bean>(mp.getBean(), this.get(r.getTo()).getBean(), r.getTipoRelazione(), r.getTipoFetch(), r.getThereIsDirectReferences());
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
	
	public MappingHIBERNATE get(Classe c) {
		MappingHIBERNATE res=null;
		for(MappingHIBERNATE mp: mapping) {
			if(mp.contiene(c)) {
				res=mp;
				break;
			}
		}
		return res;
	}
	

	private void impostaBeanNM(Riferimento<Bean> riferimento) {
		/*Bean from = riferimento.getFrom();
		Bean to = riferimento.getTo();

		boolean esiste = (esisteBean(nomeMapping(from, to)) || esisteBean(nomeMapping(to, from)));

		if(!riferimento.thereIsDirectReferences()) {
			if(!esiste) {
				String nomeClasseMapping = nomeMapping(from, to);
				Bean classeMapping = new Bean(nomeClasseMapping, nomeClasseMapping, nomeClasseMapping);
				//classeMapping.isLazyLoading(false);
				MappingHIBERNATE mp = (new MappingHIBERNATE());
				mp.setBean(classeMapping);
				mapping.add(mp);

				for(Attributo a : from.getPrimaryKeys())
					classeMapping.addPrimaryKey(a);
				for(Attributo a : to.getPrimaryKeys())
					classeMapping.addPrimaryKey(a);
			}
			//Riferimento<Bean> from_to = new Riferimento<Bean>(from, to, "1n", relazione.getTipoFetch(), relazione.thereIsDirectReferences());
			riferimento.clear();
		}
		*/
	}

	private void impostaBeanN1(Riferimento<Bean> riferimento) {
		/*if(!riferimento.thereIsDirectReferences()) {
			Attributo a = riferimento.getTo().getPrimaryKey();	
			riferimento.getFrom().addAttributo(a);

			//#TODO  capire se serve la navigabilit� diretta (tramite riferimento ) o basta l'id;
			riferimento.clear();  
		}	
		*/
	}

	private void impostaBean1N(Riferimento<Bean> riferimento) {
		// TODO Auto-generated method stub
		
	}
	
//	private boolean esisteBean(String nome) {
//		boolean res=false;
//		Bean b=null;
//		for(MappingHIBERNATE mp: mapping) {
//			b=mp.getBean();
//			if(b!=null && b.getNome().equals(nome)) {
//				return true;
//			}
//		}
//		return res;
//	}
	//-----END BEAN---------------------
	
	//-------File Config----------
	//------END File Config---------
	
	
	public void generate() {
		impostaHaveUML();
		
		Utils.deleteFolder(new File("./out"));
		File outputDirectory = new File("./out/it/unibo/tw");
		outputDirectory.mkdirs();
		
		generaBean();
		generaManager();
		generaCFG_XML();
		generaHBM_XML();

		generaTest();
		
	}

	private void generaHBM_XML() {
		File outputDirectory = new File("./out/it/unibo/tw/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resources/HIBERNATE/HBM_XML.ftl";
		GeneratoreFileHIBERNATE_FTL GD;
		try {
			GD = new GeneratoreFileHIBERNATE_FTL();

			for(MappingHIBERNATE mp : mapping) {
				Manager manager = mp.getManager();
				if(manager != null && manager.getHaveUML()) {
					manager.inizializzaMMQ();
					GD.generateJavaManagerFiles(manager, input, manager.getNomeBean() + ".hbm.xml", outputDirectory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void generaCFG_XML() {
		List<Manager> managers = new LinkedList<Manager>();
		for(MappingHIBERNATE mp : mapping) {
			Manager manager = mp.getManager();
			if(manager != null)
				managers.add(manager);
		}	
		File outputDirectory = new File("./out/it/unibo/tw/");
		String input = "./resources/HIBERNATE/CFG_XML.ftl";
		GeneratoreFileHIBERNATE_FTL GD;
		try {
			GD = new GeneratoreFileHIBERNATE_FTL();
			GD.generateJavaHIBERNATETest(managers, input, "hibernate.cfg.xml", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void generaTest() {
		List<Manager> managers = new LinkedList<Manager>();
		for(MappingHIBERNATE mp : mapping) {
			Manager manager = mp.getManager();
			if(manager != null)
				managers.add(manager);
		}	
		File outputDirectory = new File("./out/it/unibo/tw/");
		String input = "./resources/HIBERNATE/HIBERNATETest.ftl";
		GeneratoreFileHIBERNATE_FTL GD;
		try {
			GD = new GeneratoreFileHIBERNATE_FTL();
			GD.generateJavaHIBERNATETest(managers, input, "TestHIB.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
}
