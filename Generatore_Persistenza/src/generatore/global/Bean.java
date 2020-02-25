package generatore.global;

import generatore.problema.ClasseProblema;

public class Bean extends ClasseProblema{

	private String nomeBean;
	//private String nomeProxy;


	public Bean(String nome, String nomePlurale, String nomeBean) {
		super(nome, nomePlurale);
		this.nomeBean = Utils.capFirst(nomeBean);
//		if(nomeBean.contains("DTO"))
//			this.nomeProxy="DB2"+nomeBean+"Proxy";
//		else
//			this.nomeProxy=nomeBean+"Proxy";
	}
	
	public String getNomeBean() {
		return nomeBean;
	}

	
//	public String getNomeProxy() {
//		return nomeProxy;
//	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bean other = (Bean) obj;
		if (this.getNome() != other.getNome())
			return false;
		return true;
	}


	
}
