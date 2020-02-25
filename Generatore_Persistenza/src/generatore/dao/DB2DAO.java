package generatore.dao;

import java.util.LinkedHashSet;
import java.util.Set;

import generatore.global.Utils;
import generatore.problema.Attributo;
import generatore.problema.ClasseProblema;
import generatore.problema.MappingMetodoQuery;
import generatore.problema.MetodoFind;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public class DB2DAO extends ClasseProblema{

	//private String nomeTabella;
	private String nomeDB2DAO;
	private String nomeDAO;
	private String nomeBean;
	private Set<MetodoFind<DB2DAO>> metodiFind;
	private MappingMetodoQuery insert,  update, createtable, droptable;
	private Set<MappingMetodoQuery> reads, deletes, finds;
	private boolean haveUML;
	

	private String nomeProxy;

	public Set<MetodoFind<DB2DAO>> getMetodiFind() {
		return metodiFind;
	}
	
	public void addMetodoFind(MetodoFind<DB2DAO> mf) {
		metodiFind.add(mf);
	}
	
	public DB2DAO(String nome, String nomePlurale,String nomeTabella) {
		super(nome, nomePlurale);
		//this.nomeTabella=nome.toLowerCase();
		setNomeTabella(nomeTabella);
		this.nomeDB2DAO="DB2"+nome+"DAO";
		this.nomeDAO=nome+"DAO";
		this.nomeBean=nome+"DTO";
		this.nomeProxy="DB2"+nomeBean+"Proxy";
		metodiFind = new LinkedHashSet<MetodoFind<DB2DAO>>(); 
		reads = new LinkedHashSet<MappingMetodoQuery>(); 
		deletes = new LinkedHashSet<MappingMetodoQuery>(); 
		finds = new LinkedHashSet<MappingMetodoQuery>(); 
	}
	
	public void inizializzaMMQ() {
		createtable=UtilsDAO.inizializzaMMQCreate(this);
		droptable=UtilsDAO.inizializzaMMQDrop(this);
		update=UtilsDAO.inizializzaMMQUpdate(this);
		insert=UtilsDAO.inizializzaMMQInsert(this);
		generaMMQReads();
		generaMMQDeletes();
		generaMMQFinds();
	}
	
	private void generaMMQFinds() {
		for(MetodoFind<DB2DAO> mf : this.getMetodiFind()) {
			String nomeQuery = "find_"+mf.getClasseTarget().getNomeTabella()+"_by_"+mf.getAttributo().getNome();
			String nomeMetodoQuery = "find"+mf.getClasseTarget().getNomePlurale()+"By"+mf.getAttributo().getNomeUp();
			LinkedHashSet<Attributo> a = (new LinkedHashSet<Attributo>());
			a.add(mf.getAttributo());
			finds.add(UtilsDAO.inizializzaMMQFind(this, mf, nomeQuery, nomeMetodoQuery, a));

		}
	}

	private void generaMMQDeletes() {
		String nomeQuery = "delete_by";
		String nomeMetodoQuery = "deleteBy";
		for(Attributo atr : this.getPrimaryKeys()) {
			nomeQuery+="_"+atr.getNome();
			nomeMetodoQuery+=atr.getNomeUp();
		}
		deletes.add(UtilsDAO.inizializzaMMQDelete(this, nomeQuery, nomeMetodoQuery, this.getPrimaryKeys()));
		
		for(Unique un: this.getUnique()) {
			nomeQuery = "delete_by";
			nomeMetodoQuery = "deleteBy";
			for(Attributo atr : un.getAttributi()) {
				nomeQuery+="_"+atr.getNome();
				nomeMetodoQuery+=atr.getNomeUp();
			}
			deletes.add(UtilsDAO.inizializzaMMQDelete(this, nomeQuery, nomeMetodoQuery, un.getAttributi()));		
		}
	}

	private void generaMMQReads() {
		String nomeQuery = "read_by";
		String nomeMetodoQuery = "readBy";
		for(Attributo atr : this.getPrimaryKeys()) {
			nomeQuery+="_"+atr.getNome();
			nomeMetodoQuery+=atr.getNomeUp();
		}
		reads.add(UtilsDAO.inizializzaMMQRead(this, nomeQuery, nomeMetodoQuery, this.getPrimaryKeys()));
		
		for(Unique un: this.getUnique()) {
			nomeQuery = "read_by";
			nomeMetodoQuery = "readBy";
			for(Attributo atr : un.getAttributi()) {
				nomeQuery+="_"+atr.getNome();
				nomeMetodoQuery+=atr.getNomeUp();
			}
			reads.add(UtilsDAO.inizializzaMMQRead(this, nomeQuery, nomeMetodoQuery, un.getAttributi()));		
		}
	}
	

	public String getNomeBean() {
		return nomeBean;
	}
	
	public String getNomeDB2DAO() {
		return nomeDB2DAO;
	}

	public String getNomeDAO() {
		return nomeDAO;
	}

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

	public String getNomeProxy() {
		return nomeProxy;
	}

	public void setNomeProxy(String nomeProxy) {
		this.nomeProxy = nomeProxy;
	}

	
}
