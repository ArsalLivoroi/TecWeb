package generatore.hibernare;

import generatore.global.Bean;
import generatore.global.Classe;
import generatore.problema.ClasseProblema;

public class MappingHIBERNATE {

	private ClasseProblema classeProblema;
	private Bean bean;
	private Manager manager;
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
		MappingHIBERNATE other = (MappingHIBERNATE) obj;
		if (bean != null && bean == other.getBean() )
			return true;
		if (manager != null && manager == other.getManager() )
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
		if(c.equals(manager))
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


	public Manager getManager() {
		return manager;
	}


	public void setManager(Manager tabella) {
		this.manager = tabella;
	}
	
}
