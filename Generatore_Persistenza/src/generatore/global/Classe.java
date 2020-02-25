package generatore.global;

import java.util.LinkedHashSet;
import java.util.Set;

import generatore.problema.Attributo;
import generatore.problema.Riferimento;
import generatore.problema.Unique;

public abstract class Classe {

	private String nome, nomePlurale, nomeTabella;


	private Set<Attributo> primaryKeys;
	private Set<Attributo> attributi;
	private Set<Unique> unique;	
	
	private Set<Riferimento<? extends Classe>> riferimenti;
	
	
	public Classe(String nome, String nomePlurale) {
		nome=Utils.capitalizeWord(nome);
		this.nome = Utils.capFirst(nome).replaceAll("\\s", "");
		this.nomeTabella = nome.replaceAll("\\s", "_").toLowerCase();
		this.nomePlurale = Utils.capFirst(nomePlurale).replaceAll("\\s", "");
		
		this.attributi = new LinkedHashSet<Attributo>();
		this.primaryKeys = new LinkedHashSet<Attributo>();
		this.riferimenti = new LinkedHashSet<Riferimento<?>>();
		this.unique = new LinkedHashSet<Unique>();
	}
	
	public void setNomeTabella(String nomeTabella) {
		this.nomeTabella = nomeTabella;
	}
	
	public Set<Attributo> getAttributiWithoutUnique() {
		Set<Attributo> res= new LinkedHashSet<Attributo>();
		res.addAll(attributi);
		for(Unique u : unique) {
			res.removeAll(u.getAttributi());
		}
		return res;
	}
	
	public Set<Attributo> getAllAttributi(){
		Set<Attributo> res= new LinkedHashSet<Attributo>();
		res.addAll(primaryKeys);
		for(Unique u : unique) {//TODO inutile
			res.addAll(u.getAttributi());
		}
		res.addAll(attributi);
		return res;
	}
	
	public String getNome() {
		return nome;
	}
	public String getNomeTabella() {
		return nomeTabella;
	}

	public Set<Attributo> getAttributi() {
		return attributi;
	}
	public void addAttributo(Attributo attributo) {
		//this.attributiAggiunti.add(attributo);
		this.attributi.add(attributo);
	}
	
	public Attributo getPrimaryKey() {
		if(primaryKeys.isEmpty())
			this.primaryKeys.add(new Attributo("int", "id "+this.nome));
		return primaryKeys.iterator().next();
	}
	
	public Set<Attributo> getPrimaryKeys() {
		if(primaryKeys.isEmpty())
			this.primaryKeys.add(new Attributo("int", "id "+this.nome));
		return primaryKeys;
	}
	
	public void addPrimaryKey(Attributo attributo) {
		this.primaryKeys.add(attributo);
	}
	
	public Set<Unique> getUnique() {
		return unique;
	}
	public void addUnique(Unique unique) {
		this.unique.add(unique);
		for(Attributo atr: unique.getAttributi())
			this.addAttributo(atr);
	}
	
	//public Set<Riferimento<ClasseProblema>> getRiferimenti() {
	//	return riferimenti;
	//}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Classe other = (Classe) obj;
		if (nome != other.nome)
			return false;
		return true;
	}
	
	public String getNomePlurale() {
		return this.nomePlurale;
	}
	
//	public abstract void addRiferimento(Riferimento<?> riferimento);

	public Set<Riferimento<? extends Classe>> getRiferimenti() {
		return riferimenti;
	}

	public <T extends Riferimento<? extends Classe>> void addRiferimento(T riferimento) {
		this.riferimenti.add(riferimento);		
	}
	
/*
	public void addRiferimento(Classe toClasse, String tipoRelazione, String tipoFetch,
			boolean thereIsDirectReferences) {
		new Riferimento<?>(this, toClasse, tipoRelazione, tipoFetch, thereIsDirectReferences);		
	}
*/		
		
	public <T extends Classe> boolean haRiferimentoA(T toClasse) {
		for(Riferimento<? extends Classe> r: riferimenti)
			if(r.getTo().equals(toClasse))
				return true;
		return false;
	}

	
	@Override
	public String toString() {
		String res=this.getNome()+":\n";
		for(Attributo a :primaryKeys)
			res+="\t"+a+"\n";
		LinkedHashSet<Attributo> na = new LinkedHashSet<Attributo>();
		na.addAll(attributi);
		na.removeAll(primaryKeys);
		for(Unique u :unique) {
			na.removeAll(u.getAttributi());
			res+="\t[";
			for(Attributo a :u.getAttributi())
				res+=""+a+";";
			res+="]U\n";
		}
		for(Attributo a : na)
			res+="\t"+a+"\n";
		return res;
	}


	public void setPrimaryKeys(Set<Attributo> primaryKeys) {
		this.primaryKeys=primaryKeys;	
	}

	public void setAttributi(Set<Attributo> attributi) {
		this.attributi=attributi;	
	}

	public void setUnique(Set<Unique> unique) {
		this.unique=unique;
	}

	public <T extends Classe> void remove(Riferimento<T> riferimento) {
		riferimenti.remove(riferimento);
	}
	
}
