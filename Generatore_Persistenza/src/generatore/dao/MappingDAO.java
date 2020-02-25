package generatore.dao;

import generatore.global.Bean;
import generatore.global.Classe;
import generatore.problema.ClasseProblema;

public class MappingDAO {

	private ClasseProblema classeProblema;
	private Bean beanDTO;
	private DB2DAO db2dao;
	//private Proxy proxy;
	//private Dao abstractDAO;
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MappingDAO other = (MappingDAO) obj;
		if (beanDTO != null && beanDTO == other.getBeanDTO() )
			return true;
		if (db2dao != null && db2dao == other.getDB2DAO() )
			return true;
		if (classeProblema != null && classeProblema == other.getClasseProblema() )
			return true;
		
		return false;
	}
	
	public <T extends Classe>boolean contiene(T c) {
		if(c==null)
			return false;
		if(c.equals(classeProblema))
			return true;
		if(c.equals(beanDTO))
			return true;
		if(c.equals(db2dao))
			return true;
		//if(c.equals(proxy))
			//return true;
		return false;
	}


	public ClasseProblema getClasseProblema() {
		return classeProblema;
	}


	public void setClasseProblema(ClasseProblema classeProblema) {
		this.classeProblema = classeProblema;
	}
//
//
//	public Proxy getProxy() {
//		return proxy;
//	}
//
//
//	public void setProxy(Proxy proxy) {
//		this.proxy = proxy;
//	}


	public Bean getBeanDTO() {
		return beanDTO;
	}


	public void setBeanDTO(Bean beanDTO) {
		this.beanDTO = beanDTO;
	}


	public DB2DAO getDB2DAO() {
		return db2dao;
	}


	public void setDB2DAO(DB2DAO tabella) {
		this.db2dao = tabella;
	}
}
