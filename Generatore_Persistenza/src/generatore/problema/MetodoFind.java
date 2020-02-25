package generatore.problema;

import generatore.global.Classe;

public class MetodoFind<T extends Classe> {
	private T classeTarget, classeSource;
	private Attributo attributoSource;
	private boolean isLazy;
	private T possessore;//che schifo
	
	public MetodoFind(T classeTarget, T classeSource, Attributo attributoSource,boolean isLazy) {
		this.classeTarget = classeTarget;
		this.classeSource = classeSource;
		this.attributoSource = attributoSource;
		this.isLazy=isLazy;
	}
	
	public boolean getIsLazy() {
		return isLazy;
	}

	public void setLazy(boolean isLazy) {
		this.isLazy = isLazy;
	}

	public T getClasseTarget() {
		return classeTarget;
	}

	public T getClasseSource() {
		return classeSource;
	}

	public Attributo getAttributo() {
		return attributoSource;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetodoFind<?> other = (MetodoFind<?>) obj;
		if (classeSource.equals(other.getClasseSource()) && classeTarget.equals(other.getClasseTarget()) && attributoSource.equals(other.getAttributo()) )
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return this.getPossessore().getNome().toString() + " -> " +
				this.getClasseTarget().getNome() + " - " + 
				this.classeSource.getNome() + ": " + 
				this.attributoSource.getNome();
	}

	public T getPossessore() {
		return possessore;
	}

	public void setPossessore(T possessore) {
		this.possessore = possessore;
	}
}
