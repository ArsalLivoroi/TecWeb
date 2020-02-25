package generatore.hibernare;

import java.util.LinkedHashSet;
import java.util.Set;

import generatore.global.Classe;
import generatore.global.Utils;
import generatore.problema.Attributo;
import generatore.problema.MappingMetodoQuery;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public class Manager extends Classe{

	//private String nomeTabella;
	private String nomeManager;
	//private String nomeDAO;
	private String nomeBean;
	private Set<MetodoFind<Manager>> metodiFind;
	private MappingMetodoQuery insert,  update, createtable, droptable;
	private Set<MappingMetodoQuery> reads, deletes, finds;
	private boolean haveUML;
	
	public Manager(String nome, String nomePlurale, String nomeTabella) {
		super(nome, nomePlurale);
		
		setNomeTabella(nomeTabella);
		this.setNomeManager(nome+"Manager");
		//this.nomeDAO=nome+"DAO";
		this.nomeBean=nome;
		//this.nomeProxy="DB2"+nomeBean+"Proxy";
		metodiFind = new LinkedHashSet<MetodoFind<Manager>>(); 
		reads = new LinkedHashSet<MappingMetodoQuery>(); 
		deletes = new LinkedHashSet<MappingMetodoQuery>(); 
		finds = new LinkedHashSet<MappingMetodoQuery>(); 
	}
	
	public Set<MetodoFind<Manager>> getMetodiFind() {
		return metodiFind;
	}
	
	public void addMetodoFind(MetodoFind<Manager> mf) {
		metodiFind.add(mf);
	}
	
	public void inizializzaMMQ() {
		createtable=UtilsHIBERNATE.inizializzaMMQCreate(this);
		droptable=UtilsHIBERNATE.inizializzaMMQDrop(this);
		update=UtilsHIBERNATE.inizializzaMMQUpdate(this,"update");
		insert=UtilsHIBERNATE.inizializzaMMQInsert(this);
		generaMMQReads();
		generaMMQDeletes();
		generaMMQFinds();
	}
	
	private void generaMMQFinds() {
		for(MetodoFind<Manager> mf : this.getMetodiFind()) {
			String nomeQuery = "find_"+mf.getClasseTarget().getNomeTabella()+"_by_"+mf.getAttributo().getNome();
			String nomeMetodoQuery = "find"+mf.getClasseTarget().getNomePlurale()+"By"+mf.getAttributo().getNomeUp();
			LinkedHashSet<Attributo> a = (new LinkedHashSet<Attributo>());
			a.add(mf.getAttributo());
			finds.add(UtilsHIBERNATE.inizializzaMMQFind(this, mf, nomeQuery, nomeMetodoQuery, a));

		}
	}

	private void generaMMQDeletes() {
		String nomeQuery = "delete_by";
		String nomeMetodoQuery = "deleteBy";
		for(Attributo atr : this.getPrimaryKeys()) {
			nomeQuery+="_"+atr.getNome();
			nomeMetodoQuery+=atr.getNomeUp();
		}
		deletes.add(UtilsHIBERNATE.inizializzaMMQUpdate(this,"delete"));
		deletes.add(UtilsHIBERNATE.inizializzaMMQDelete(this, nomeQuery, nomeMetodoQuery, this.getPrimaryKeys()));
		
		for(Unique un: this.getUnique()) {
			nomeQuery = "delete_by";
			nomeMetodoQuery = "deleteBy";
			for(Attributo atr : un.getAttributi()) {
				nomeQuery+="_"+atr.getNome();
				nomeMetodoQuery+=atr.getNomeUp();
			}
			deletes.add(UtilsHIBERNATE.inizializzaMMQDelete(this, nomeQuery, nomeMetodoQuery, un.getAttributi()));		
		}
	}

	private void generaMMQReads() {
		String nomeQuery = "read_by";
		String nomeMetodoQuery = "readBy";
		for(Attributo atr : this.getPrimaryKeys()) {
			nomeQuery+="_"+atr.getNome();
			nomeMetodoQuery+=atr.getNomeUp();
		}
		reads.add(UtilsHIBERNATE.inizializzaMMQRead(this, nomeQuery, nomeMetodoQuery, this.getPrimaryKeys()));
		
		for(Unique un: this.getUnique()) {
			nomeQuery = "read_by";
			nomeMetodoQuery = "readBy";
			for(Attributo atr : un.getAttributi()) {
				nomeQuery+="_"+atr.getNome();
				nomeMetodoQuery+=atr.getNomeUp();
			}
			reads.add(UtilsHIBERNATE.inizializzaMMQRead(this, nomeQuery, nomeMetodoQuery, un.getAttributi()));		
		}
	}
	

	public String getNomeBean() {
		return nomeBean;
	}

//	public String getNomeTabella() {
//		return nomeTabella;
//	}


	public MappingMetodoQuery getInsert() {
		return insert;
	}

	public MappingMetodoQuery getUpdate() {
		return update;
	}

	public MappingMetodoQuery getCreatetable() {
		return createtable;
	}

	public MappingMetodoQuery getDroptable() {
		return droptable;
	}

	public Set<MappingMetodoQuery> getReads() {
		return reads;
	}

	public Set<MappingMetodoQuery> getDeletes() {
		return deletes;
	}

	public Set<MappingMetodoQuery> getFinds() {
		return finds;
	}

	public String getNomeOggetto() {
		return Utils.uncapFirst(this.getNome());
	}
	
	public void setHaveUML(boolean haveUML) {
		this.haveUML=haveUML;
	}
	
	public boolean getHaveUML() {
		return haveUML;
	}

	public boolean isLazyLoading() {
		for(Riferimento<?> r : this.getRiferimenti())
			if(r.getIsLazyLoad())
				return true;
		return false;
	}

	public String getNomeManager() {
		return nomeManager;
	}

	public void setNomeManager(String nomeManager) {
		this.nomeManager = nomeManager;
	}
	
}
