package generatore.problema;

import javax.rmi.CORBA.Util;

import generatore.Generatore;
import generatore.global.Classe;
import generatore.global.Utils;

public class Riferimento<T extends Classe> {

	private String tipoRelazione;
	private T from, to;
	
	private String tipoFetch; //Opzionale
	private boolean thereIsDirectReferences;//sarebbe meglio un oggetto Navigabilità che include String:tipoFetch e Boolean:riferimento
											//per separare meglio le responsabilità, 
	
	private Attributo attributo;
	private String commento ="";
	//private boolean isLazyLoad;
	
	
	public Riferimento(T fromClasse, T toClasse, String tipoRelazione, String tipoFetch,
			boolean thereIsDirectReferences) {
		checkTipoRelazione(tipoRelazione);
		this.tipoRelazione = tipoRelazione.toLowerCase();
		this.from = fromClasse;
		this.to = toClasse;
		if(thereIsDirectReferences)
			if(tipoFetch!=null)
				this.tipoFetch = tipoFetch;
			else {
				if(this.tipoRelazione == "1n" || this.tipoRelazione == "nm") {
					this.tipoFetch = Generatore.LAZY_LOAD;
					commento="//non essendo specificato è considerando che la relazione e a molti, si è deciso di adottare il Lazy-Loading";
				}else {
					this.tipoFetch = Generatore.EAGER;	
				}
			}
		else {
//			if(tipoFetch!=null)
//				System.err.println("Attenzione! TipoFetch non nullo, ma navigabilità assente");
//			else
				this.tipoFetch = null;
		}
		//this.thereIsDirectReferences = thereIsDirectReferences;
		
//		if(this.tipoRelazione.equals("n1") || this.tipoRelazione.equals("11")) 
//			this.thereIsDirectReferences = false;
//		else
			this.thereIsDirectReferences = thereIsDirectReferences;
			
		attributo=new Attributo(Utils.capFirst(toClasse.getNome()), toClasse.getNome());
		from.addRiferimento(this);
	}
	
	public String getCommento() {
		return commento;
	}

	private void checkTipoRelazione(String tipoRelazione) {
		if(!(tipoRelazione == "11" ||tipoRelazione == "1n" ||tipoRelazione == "n1" ||tipoRelazione == "nm")) {
			System.err.println("Attenzione!!!------------------------------");
			System.err.println("Tipo Relazione Amissibili: 11 1n n1 nm");
		}
	}

	public void clear() {
		from.remove(this);
	}
	
	public boolean getIsLazyLoad() {
		if(this.getTipoFetch() != null && this.getTipoFetch().equals(Generatore.LAZY_LOAD))
			return true;
		return false;
	}
	public boolean isEager() {
		if(this.getTipoFetch() != null && this.getTipoFetch().equals(Generatore.EAGER))
			return true;
		return false;
	}
	
	public Attributo getAttributo() {
		//impostaAttributo();
		return attributo;
	}

	public String getTipoRelazione() {
		return tipoRelazione;
	}

	public T getFrom() {
		return from;
	}

	public T getTo() {
		return to;
	}

	public String getTipoFetch() {
		return tipoFetch;
	}
	
	public boolean getThereIsDirectReferences() {
		return thereIsDirectReferences;
	}

//	public  void impostaAttributo() {
//		if(this.thereIsDirectReferences) {
//			if(this.tipoRelazione.equals("1n"))
//				attributo = new Attributo("Set<"+this.to.getNome()+">", this.to.getNomePlurale().substring(0, 1).toLowerCase() + this.to.getNomePlurale().substring(1));
//			else if(this.tipoRelazione.equals("n1")) {
//				attributo = new Attributo(this.to.getNome(), this.to.getNome().substring(0, 1).toLowerCase() + this.to.getNome().substring(1));
//				//for(Attributo art : toClasse.getPrimaryKeys())
//
//				/*
//				Attributo art=toClasse.getPrimaryKey();
//				fromClasse.addAttributo(new Attributo(art.getTipo(), art.getNome().substring(0, 1).toLowerCase() + art.getNome().substring(1) + this.toClasse.getNome()));
//				attributo = null;
//				*/
//				//attributo = new Attributo(art.getTipo(), art.getNome().substring(0, 1).toLowerCase() + art.getNome().substring(1) + toClasse.getNome());
//			}else if(this.tipoRelazione.equals("nm")) { 
//				attributo = new Attributo("Set<"+this.to.getNome()+">", this.to.getNomePlurale().substring(0, 1).toLowerCase() + this.to.getNomePlurale().substring(1));
//				//this.classeRiferimentoDB = fromClasse;
//			}else System.out.println(this.tipoRelazione);
//		}
//	}
	
	
/*	public Riferimento relazioneContraria() {
		String tipo="";
		if(this.tipoRelazione == "1n")
			tipo = "n1";
		if(this.tipoRelazione == "n1")
			tipo = "1n";
		if(this.tipoRelazione == "nm")
			tipo = "nm";
		if(this.tipoRelazione == "11")
			tipo = "11";
		return new Riferimento(toClasse, fromClasse, tipo, null, thereIsDirectReferences);
	}
	
	public boolean isOposite(Riferimento relazione) {
		return (this.toClasse.equals(relazione.getFromClasse())&&this.fromClasse.equals(relazione.getToClasse()));
	}
	
	public boolean collega(ClasseProblema fromClasse, ClasseProblema toClasse) {
		if(fromClasse.equals(this.fromClasse) && toClasse.equals(this.toClasse))
			return true;
		return false;
	}
	
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Riferimento<?> other = (Riferimento<?>) obj;
		if (from == other.from && to == other.to)
			return true;

		return false;
	}
	
	@Override
	public String toString(){
		return from.getNome()+ "-" + 
				to.getNome() +" "+ 
				/*this.attributo.getNome()*/ " : "+tipoRelazione; 
	}

}
