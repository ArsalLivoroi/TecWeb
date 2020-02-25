package generatore.problema;

import java.util.*;

public class Unique {

	private Set<Attributo> attributi;
	
	public Unique() {
		this.attributi = new LinkedHashSet<Attributo>();
	}
		
	public Set<Attributo> getAttributi() {
		return attributi;
	}
	
	public void addAttributo(Attributo attributo) {
		this.attributi.add(attributo);
	}
	
	@Override
	public String toString() {
		String res = "--\n";
		for(Attributo a : attributi)
			res+=a.toString()+"\n";
		return res+"--\n";
	}
	
}
