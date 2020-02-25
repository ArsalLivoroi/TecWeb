package generatore.problema;

import java.util.*;

public class Problema {

	private String tecnologia;
	
	private Set<ClasseProblema> classi;

	public Problema(String tecnologia) {
		this.tecnologia = tecnologia.toLowerCase();
		this.classi = new LinkedHashSet<ClasseProblema>();
	}
	
	public String getTecnologia() {
		return tecnologia;
	}
	
	public Set<ClasseProblema> getClassi() {
		return classi;
	}
	
	public void addClasseProblema(ClasseProblema classe) {
		this.classi.add(classe);
	}
	
	public void check() {
		List<ClasseProblema> l = new ArrayList<ClasseProblema>();
		l.addAll(classi);
		for(int i=0; i<classi.size();i++)
			for(int j=0; j<classi.size();j++)
				if(i!=j) {
					if(l.get(i).getNome().equals(l.get(j).getNome())||
							l.get(i).getNomePlurale().equals(l.get(j).getNomePlurale())||
							l.get(i).getNomePlurale().equals(l.get(j).getNome())||
							l.get(i).getNome().equals(l.get(j).getNomePlurale()))
						System.err.println("Attenzione ai nomi "+l.get(i).getNome()+" "+l.get(i).getNomePlurale());
				}
	}
	
}
