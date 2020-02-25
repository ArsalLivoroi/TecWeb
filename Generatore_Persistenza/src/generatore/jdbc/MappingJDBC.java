package generatore.jdbc;

import generatore.global.Bean;
import generatore.global.Classe;
import generatore.problema.ClasseProblema;

public class MappingJDBC {

	
	private ClasseProblema classeProblema;
	private Bean bean;
	private Repository repository;
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
		MappingJDBC other = (MappingJDBC) obj;
		if (bean != null && bean == other.getBean() )
			return true;
		if (repository != null && repository == other.getRepository() )
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
		if(c.equals(bean))
			return true;
		if(c.equals(repository))
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


	public Bean getBean() {
		return bean;
	}


	public void setBean(Bean beanDTO) {
		this.bean = beanDTO;
	}


	public Repository getRepository() {
		return repository;
	}


	public void setRepository(Repository tabella) {
		this.repository = tabella;
	}
}
