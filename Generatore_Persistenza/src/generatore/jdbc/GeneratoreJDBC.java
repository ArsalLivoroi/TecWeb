package generatore.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.*;

import generatore.global.Bean;
import generatore.global.Classe;
import generatore.global.Utils;
import generatore.jdbc.MappingJDBC;
import generatore.problema.Attributo;
import generatore.problema.ClasseProblema;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public class GeneratoreJDBC {

	private Set<ClasseProblema> classiProblema;
	private Set<MappingJDBC> mapping;
	//private Set<MetodoFind> proxyMapping;

	public GeneratoreJDBC(Set<ClasseProblema> classiProblema) {
		//proxyMapping = new LinkedHashSet<MetodoFind>();
		this.classiProblema=classiProblema;
		this.mapping=new HashSet<MappingJDBC>();
		for(ClasseProblema c: classiProblema) {
			MappingJDBC mp = new MappingJDBC();
			mp.setClasseProblema(c);
			mapping.add(mp);
		}

		impostaBean();
		impostaJDBC();
	}


	
	//-------Repository------------------
	
	private void generaRepository() {
		File outputDirectory = new File("./src/it/unibo/tw/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resourcesJDBC/Repository.ftl";
		GeneratoreFileJDBC_FTL GD;
		try {
			GD = new GeneratoreFileJDBC_FTL();

			for(MappingJDBC mp : mapping) {
				Repository repository = mp.getRepository();
				if(repository != null) {
					repository.inizializzaMMQ();
					GD.generateJavaJDBCFiles(repository, input, repository.getNomeRepository() + ".java", outputDirectory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void impostaJDBC() {
		for(ClasseProblema c: classiProblema) {
			Repository rep = new Repository(c.getNome(), c.getNomePlurale(), c.getNomeTabella());
			rep.setPrimaryKeys(c.getPrimaryKeys());
			rep.setAttributi(c.getAttributi());
			rep.setUnique(c.getUnique());


			if(rep.getPrimaryKeys().size()>1)
				for(Attributo atr: rep.getPrimaryKeys())
					rep.addMetodoFind(new MetodoFind<Repository>(rep, rep, atr, false));
			for(Unique un: rep.getUnique()) {
				if(un.getAttributi().size()>1)
					for(Attributo atr: un.getAttributi())
						rep.addMetodoFind(new MetodoFind<Repository>(rep, rep, atr, false));
			}
			for(Attributo atr: rep.getAttributiWithoutUnique())
				rep.addMetodoFind(new MetodoFind<Repository>(rep, rep, atr, false));

			this.get(c).setRepository(rep);
		}

		Set<MappingJDBC> map = new LinkedHashSet<MappingJDBC>() ;
		map.addAll(mapping);

		for(MappingJDBC mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			if(c!=null)
				for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
					Riferimento<Repository> relazione = new Riferimento<Repository>(mp.getRepository(), this.get(r.getTo()).getRepository(), r.getTipoRelazione(), r.getTipoFetch(), r.thereIsDirectReferences());
					switch (relazione.getTipoRelazione()) {
					case "1n":
						impostaRepository1N(relazione );
						break;
					case "n1":
						impostaRepositoryN1(relazione );
						break;
					case "nm":
						impostaRepositoryNM(relazione);
						break;
					case "11":
						impostaRepositoryN1(relazione );
						break;	
					default :
						System.err.println("nome relazione errato!");
					}
				}
		}
		
	}
	
	
	private void impostaRepositoryN1(Riferimento<Repository> relazione) {
		// TODO Auto-generated method stub
		
	}

	private MappingJDBC getMappingJDBC(String nome) {
		Repository b=null;
		for(MappingJDBC mp: mapping) {
			b=mp.getRepository();
			if(b!=null && b.getNome().equals(nome)) {
				return mp;
			}
		}
		return null;
	}
	

	private String nomeMapping(Classe from, Classe to) {
		return from.getNome() + to.getNome() +"Mapping";
	}

	private String nomeTabellaMapping(Classe from, Classe to) {
		return from.getNome()+ "_"+ to.getNome() +"_Mapping";
	}

	private void impostaRepositoryNM(Riferimento<Repository> relazione) {
		Repository fromClasse = relazione.getFrom();
		Repository toClasse = relazione.getTo();

		boolean esiste = (esisteRepository(nomeMapping(fromClasse, toClasse)) || esisteRepository(nomeMapping(toClasse, fromClasse)));

		Repository classeMapping;
		MappingJDBC mp;
		if(!esiste) {
			String nome = nomeMapping(fromClasse, toClasse);
			classeMapping = new Repository(nome, nome, nomeTabellaMapping(fromClasse, toClasse));
			mp = (new MappingJDBC());
			mp.setRepository(classeMapping);
			mapping.add(mp);




		}else {
			mp = getMappingJDBC(nomeMapping(fromClasse, toClasse));
			if(mp == null)
				mp = getMappingJDBC(nomeMapping(toClasse, fromClasse));
			String nome = mp.getRepository().getNome();
			classeMapping = new Repository(nome, nome, nomeTabellaMapping(fromClasse, toClasse));
			mp.setRepository(classeMapping);
		}

		for(Attributo a : fromClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);
		for(Attributo a : toClasse.getPrimaryKeys())
			classeMapping.addPrimaryKey(a);

		new Riferimento<Repository>(classeMapping, toClasse, "n1", null, true);
		new Riferimento<Repository>(classeMapping, fromClasse, "n1", null, true);

		//FIND
		//for(Attributo atr: fromClasse.getPrimaryKeys())
		MetodoFind<Repository> mf = new MetodoFind<Repository>(toClasse, fromClasse, fromClasse.getPrimaryKey(),relazione.getIsLazyLoad());
		mf.setPossessore(classeMapping);
		classeMapping.addMetodoFind(mf);
		//System.out.println(relazione.thereIsDirectReferences());
		//if(relazione.thereIsDirectReferences())
			//proxyMapping.add(mf);
		
		
		for(Unique un: fromClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<Repository>(toClasse, fromClasse, atr,false));

		for(Attributo atr: toClasse.getPrimaryKeys())
			classeMapping.addMetodoFind(new MetodoFind<Repository>(fromClasse, toClasse, atr,false));
		for(Unique un: toClasse.getUnique())
			if(un.getAttributi().size()>1)
				for(Attributo atr: un.getAttributi())
					classeMapping.addMetodoFind(new MetodoFind<Repository>(fromClasse, toClasse, atr,false));


		relazione.clear();
		
	}



	private void impostaRepository1N(Riferimento<Repository> relazione) {
		if(relazione.thereIsDirectReferences()) {
			//TODO attenzione: getPrimaryKey in caso di classi con più primarykey genererà un file che risulterà incorretto secondo le specifiche del es;
			MetodoFind<Repository> mf = new MetodoFind<Repository>(relazione.getTo(), relazione.getFrom(), relazione.getFrom().getPrimaryKey(),relazione.getIsLazyLoad());
			mf.setPossessore(relazione.getFrom());
			relazione.getFrom().addMetodoFind(mf);
			//if(relazione.thereIsDirectReferences())
				//proxyMapping.add(mf);
			//relazione.getFromClasse().addDb2References(relazione.getToClasse());
		}
	}

	
	private boolean esisteRepository(String nome) {
		boolean res=false;
		Repository b=null;
		for(MappingJDBC mp: mapping) {
			b=mp.getRepository();
			if(b!=null && b.getNome().equals(nome)) {
				return true;
			}
		}
		return res;
	}
	
	//-------END Repository------------------


	//-------BEAN------------------
	private void generaBean() {
		File outputDirectory = new File("./src/it/unibo/tw/");
		//outputDirectory.getParentFile().mkdirs();
		String input = "./resources/Bean.ftl";
		GeneratoreFileJDBC_FTL GB;
		try {
			GB = new GeneratoreFileJDBC_FTL();

			for(MappingJDBC mp : mapping) {
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

		Set<MappingJDBC> map = new LinkedHashSet<MappingJDBC>() ;
		map.addAll(mapping);

		for(MappingJDBC mp: map) {
			ClasseProblema c = mp.getClasseProblema();
			for(Riferimento<? extends Classe> r: c.getRiferimenti()) {
				Riferimento<Bean> relazione = new Riferimento<Bean>(mp.getBean(), this.get(r.getTo()).getBean(), r.getTipoRelazione(), r.getTipoFetch(), r.thereIsDirectReferences());
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
	
	public MappingJDBC get(Classe c) {
		MappingJDBC res=null;
		for(MappingJDBC mp: mapping) {
			if(mp.contiene(c)) {
				res=mp;
				break;
			}
		}
		return res;
	}
	

	private void impostaBeanNM(Riferimento<Bean> riferimento) {
		Bean from = riferimento.getFrom();
		Bean to = riferimento.getTo();

		boolean esiste = (esisteBean(nomeMapping(from, to)) || esisteBean(nomeMapping(to, from)));

		if(!riferimento.thereIsDirectReferences()) {
			if(!esiste) {
				String nomeClasseMapping = nomeMapping(from, to);
				Bean classeMapping = new Bean(nomeClasseMapping, nomeClasseMapping, nomeClasseMapping);
				//classeMapping.isLazyLoading(false);
				MappingJDBC mp = (new MappingJDBC());
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
		
	}

	private void impostaBeanN1(Riferimento<Bean> riferimento) {
		if(!riferimento.thereIsDirectReferences()) {
			Attributo a = riferimento.getTo().getPrimaryKey();	
			riferimento.getFrom().addAttributo(a);

			//#TODO  capire se serve la navigabilità diretta (tramite riferimento ) o basta l'id;
			riferimento.clear();  
		}	
		
	}

	private void impostaBean1N(Riferimento<Bean> riferimento) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean esisteBean(String nome) {
		boolean res=false;
		Bean b=null;
		for(MappingJDBC mp: mapping) {
			b=mp.getBean();
			if(b!=null && b.getNome().equals(nome)) {
				return true;
			}
		}
		return res;
	}
	//-----END BEAN---------------------

	private void impostaHaveUML() {
		for(MappingJDBC mp: mapping) {
			Repository db = mp.getRepository();
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

	public void generate() {
		impostaHaveUML();

		Utils.deleteFolder(new File("./src/it"));
		File outputDirectory = new File("./src/it/unibo/tw/db");
		outputDirectory.mkdirs();
		
		generaBean();
		generaRepository();
		generaDataSource();
		generaException();

		generaTest();
		
	}


	private void generaException() {
		File outputDirectory = new File("./src/it/unibo/tw/db");
		String input = "./resourcesJDBC/PersistenceException.ftl";
		GeneratoreFileJDBC_FTL GD;
		try {
			GD = new GeneratoreFileJDBC_FTL();
			GD.generateJavaFile(input, "PersistenceException.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void generaDataSource() {
		File outputDirectory = new File("./src/it/unibo/tw/db");
		String input = "./resourcesJDBC/DataSource.ftl";
		GeneratoreFileJDBC_FTL GD;
		try {
			GD = new GeneratoreFileJDBC_FTL();
			GD.generateJavaFile(input, "DataSource.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}


	private void generaTest() {
		List<Repository> repositories = new LinkedList<Repository>();
		for(MappingJDBC mp : mapping) {
			Repository repository = mp.getRepository();
			if(repository != null)
				repositories.add(repository);
		}	
		File outputDirectory = new File("./src/it/unibo/tw/");
		String input = "./resourcesJDBC/JDBCTest.ftl";
		GeneratoreFileJDBC_FTL GD;
		try {
			GD = new GeneratoreFileJDBC_FTL();
			GD.generateJavaJDBCTest(repositories, input, "JDBCTest.java", outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
